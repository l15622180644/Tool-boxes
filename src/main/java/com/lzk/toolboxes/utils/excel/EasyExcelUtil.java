package com.lzk.toolboxes.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;


public class EasyExcelUtil {

    Class<?> clazz;
    Map<String,Integer> fieldIndexMap;

    private EasyExcelUtil(Class<?> clazz){
        this.clazz = clazz;
        fieldIndexMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        int num = 1;
        for (int i=0;i<fields.length;i++) {
            Field field = fields[i];
            field.setAccessible(true);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if(excelIgnore==null) {
                fieldIndexMap.put(field.getName(),num);
                num++;
            }
        }
    }

    public static EasyExcelUtil create(Class<?> clazz) {
        return new EasyExcelUtil(clazz);
    }

    private HorizontalCellStyleStrategy getDefaultStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);//背景色
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setColor(IndexedColors.BLACK.index);
        headWriteFont.setBold(false);//加粗
        headWriteFont.setFontHeightInPoints((short) 12);//字体大小
        headWriteFont.setFontName("微软雅黑");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//水平居中
        contentWriteCellStyle.setWrapped(true);//自动换行
        // 设置边框样式
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 11);//字体大小
        contentWriteFont.setFontName("Calibri");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    //添加标题
    public List<List<String>> setTitle(String title){
        /*modifyAnnotationParam(clazz, map -> {
            String[] value = (String[]) map.get("value");
            map.put("value", new String[]{title, (value.length == 2 ? value[1] : value[0])});
        });*/
        List<List<String>> head = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if(excelProperty!=null && excelIgnore==null){
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(excelProperty);
                try {
                    Field annotationValues = invocationHandler.getClass().getDeclaredField("memberValues");
                    annotationValues.setAccessible(true);
                    Map map = (Map) annotationValues.get(invocationHandler);
                    String[] value = (String[]) map.get("value");
                    if(value!=null){
                        head.add(Arrays.asList(title,value.length == 2 ? value[1] : value[0]));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return head;
    }

    private void modifyAnnotationParam(Class<?> clazz, Consumer<Map> consumer) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if (excelProperty != null && excelIgnore == null) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(excelProperty);
                Map map = null;
                try {
                    Field annotationValues = invocationHandler.getClass().getDeclaredField("memberValues");
                    annotationValues.setAccessible(true);
                    map = (Map) annotationValues.get(invocationHandler);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                consumer.accept(map);
            }
        }
    }

    public ExcelWriter excelWriter(OutputStream outputStream){
        return EasyExcel.write(outputStream)
                .registerWriteHandler(getDefaultStyle())
                .registerWriteHandler(new CustomColumnWidthStyleStrategy()) //设置列宽
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 23, (short) 20)) //设置行高
                .autoCloseStream(Boolean.FALSE) //关闭自动关流，后面可响应错误信息
                .build();
    }

    public ReadSheet readSheet(int index){
        return EasyExcel.readSheet(index).build();
    }

    public WriteSheet writerSheet(int index,String name,String title){
        ExcelWriterSheetBuilder builder = EasyExcel.writerSheet(index, name);
        if(StringUtils.isNotBlank(title)) {
            builder.head(setTitle(title));
        }else{
            builder.head(clazz);
        }
        return builder.build();
    }

    public EasyExcelUtil setWebFileName(HttpServletResponse response, String fileName){
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String createErrorMsg(Integer rowIndex,String fieldName,String msg){
        return createErrorMsg(rowIndex,null,fieldName,msg);
    }

    public String createErrorMsg(Integer rowIndex,Integer columnIndex,String msg){
        return createErrorMsg(rowIndex,columnIndex,null,msg);
    }

    public String createErrorMsg(Integer rowIndex,Integer columnIndex,String fieldName,String msg){
        if(columnIndex!=null){
            return "第"+(rowIndex+1)+"行第"+(columnIndex+1)+"列 "+msg;
        }else {
            return "第"+(rowIndex+1)+"行第"+fieldIndexMap.get(fieldName)+"列 "+msg;
        }
    }

    class CustomColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

        @Override
        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            if(isHead){
                Integer length = dataLength(cellDataList, cell, isHead);
                writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(),length * 256 + 3 * 256);
            }
        }

        private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
            if (isHead) {
                return cell.getStringCellValue().getBytes().length;
            } else {
                WriteCellData<?> cellData = cellDataList.get(0);
                CellDataTypeEnum type = cellData.getType();
                if (type == null) {
                    return -1;
                } else {
                    switch(type) {
                        case STRING:
                            return cellData.getStringValue().getBytes().length;
                        case BOOLEAN:
                            return cellData.getBooleanValue().toString().getBytes().length;
                        case NUMBER:
                            return cellData.getNumberValue().toString().getBytes().length;
                        default:
                            return -1;
                    }
                }
            }
        }
    }
}
