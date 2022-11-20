package com.lzk.toolboxes.utils.excel.listener;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzk.toolboxes.config.base.CommonMapper;
import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.excel.EasyExcelUtil;
import com.lzk.toolboxes.utils.excel.annotation.ExcelCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author
 * @module
 * @date 2022/11/2 14:43
 */
public class CustomReadListener<T> implements ReadListener<T> {

    //每隔多少条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
    private static final int BATCH_COUNT = 1;

    private final BiFunction<T,AnalysisContext,Boolean> function;

    private final List<String> errorMsg;

    //缓存的数据
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private final CommonMapper<T> mapper;

    //是否写入数据库据
    private boolean flag = true;

    private final Class<T> tClass;
    private final Field[] fields;
    private EasyExcelUtil excelUtil;


    public CustomReadListener(Class<T> tClass,CommonMapper<T> mapper,List<String> errorMsg,BiFunction<T,AnalysisContext,Boolean> function){
        this.mapper = mapper;
        this.function = function;
        this.errorMsg = errorMsg;
        this.tClass = tClass;
        fields = tClass.getDeclaredFields();
        this.excelUtil = EasyExcelUtil.create(tClass);
    }

    //每一条数据解析都会来调
    @Override
    public void invoke(T rowData, AnalysisContext analysisContext) {
        if(check(rowData,analysisContext.readRowHolder().getRowIndex()) && function!=null && !function.apply(rowData,analysisContext))
            flag = false;
        if(flag){
            cachedDataList.add(rowData);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if(cachedDataList.size() >= BATCH_COUNT){
                saveData();
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }else {
            cachedDataList = Collections.EMPTY_LIST;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    //所有数据解析完成了 都会来调
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(flag) {
            saveData();
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    private void saveData(){
        int i = cachedDataList.size() > 0 ? mapper.insertBatchSomeColumn(cachedDataList) : 0;
    }

    //在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        exception.printStackTrace();
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        if(exception instanceof ExcelDataConvertException){
            ExcelDataConvertException ex = (ExcelDataConvertException) exception;
            /*Object o = "";
            if(ex.getCellData().getType()== CellDataTypeEnum.STRING){
                o = ex.getCellData().getStringValue();
            }else if(ex.getCellData().getType()==CellDataTypeEnum.NUMBER){
                o = ex.getCellData().getNumberValue();
            }*/
            if(errorMsg!=null) errorMsg.add(excelUtil.createErrorMsg(ex.getRowIndex(),ex.getColumnIndex(),"数据解析失败"));
        }
    }

    //存在null时立即返回false
    boolean check(T rowData,Integer rowIndex){
        for (int i=0;i<fields.length;i++) {
            Field field = fields[i];
            field.setAccessible(true);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            ExcelCheck excelCheck = field.getAnnotation(ExcelCheck.class);
            if(excelIgnore==null&&excelCheck!=null){
                if(excelCheck.notNull()){
                    Object o = null;
                    try {
                        o = field.get(rowData);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if(o==null){
                        if(errorMsg!=null) errorMsg.add(excelUtil.createErrorMsg(rowIndex,field.getName(),"不能为空"));
                        flag = false;
                        return false;
                    }else if(StringUtils.isBlank(String.valueOf(o))){
                        if(errorMsg!=null) errorMsg.add(excelUtil.createErrorMsg(rowIndex,field.getName(),"不能为空"));
                        flag = false;
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
