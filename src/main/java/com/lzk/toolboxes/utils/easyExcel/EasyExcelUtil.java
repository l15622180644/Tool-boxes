package com.lzk.toolboxes.utils.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.lzk.toolboxes.utils.easyExcel.strategy.CustomColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author
 * @module
 * @date 2022/7/26 15:46
 */
public class EasyExcelUtil {

    private HttpServletResponse response;
    private OutputStream outputStream;
    private String fileName;
    private String[] titles = new String[]{};
    private String[] sheetNames = new String[]{};
    private Class<?>[] clazz = new Class<?>[]{};
    private Collection<?>[] data = new Collection<?>[]{};

    private EasyExcelUtil(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static EasyExcelUtil create(HttpServletResponse response) {
        try {
            EasyExcelUtil excelUtil = new EasyExcelUtil(response.getOutputStream());
            excelUtil.response = response;
            return excelUtil;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EasyExcelUtil create(OutputStream outputStream) {
        return new EasyExcelUtil(outputStream);
    }

    public EasyExcelUtil setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public EasyExcelUtil setTitle(String... title) {
        this.titles = title;
        return this;
    }

    public EasyExcelUtil setSheetName(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public EasyExcelUtil setClass(Class<?>... clazz) {
        this.clazz = clazz;
        return this;
    }

    public EasyExcelUtil setData(Collection<?>... data) {
        this.data = data;
        return this;
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

    private void setResponseHead(HttpServletResponse response) throws UnsupportedEncodingException {
        if (fileName == null || "".equals(fileName.trim())) setFileName("easyExcel");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }

    public void writeExcel() {
        try {
            if (response != null) setResponseHead(response);
            writeExcelWithSheets();
        } catch (Exception e) {
            e.printStackTrace();
            if (response != null) responseErrorMsg(response, e.getMessage());
        }
    }

    public void writeExcelWithSheets() {
        boolean flag = clazz.length == 1;
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream)
                .registerWriteHandler(getDefaultStyle())
                .registerWriteHandler(new CustomColumnWidthStyleStrategy()) //设置列宽
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 23, (short) 20)) //设置行高
                .autoCloseStream(this.response != null ? Boolean.FALSE : Boolean.TRUE) //关闭自动关流，后面需要响应错误信息
                .build()) {
            if (data.length > 0) {
                writeByData(excelWriter, flag);
            } else if (titles.length > 0) {
                writeEmptyExcel(excelWriter);
            }
        }
    }

    private void writeByData(ExcelWriter excelWriter, boolean flag) {
        for (int i = 0; i < data.length; i++) {
            if (titles.length > 0 && titles.length > i) modifyHead(i,flag);
            WriteSheet sheet = EasyExcel.writerSheet(i, sheetNames.length == 0 ? ("sheet" + (i + 1)) : sheetNames[i]).head(flag ? clazz[0] : clazz[i]).build();
            excelWriter.write(data[i], sheet);
        }
    }

    //只写头部
    private void writeEmptyExcel(ExcelWriter excelWriter) {
        modifyAnnotationParam(clazz[0], map -> {
            String[] value = (String[]) map.get("value");
            map.put("value", new String[]{titles[0], (value.length == 2 ? value[1] : value[0])});
        });
        WriteSheet sheet = EasyExcel.writerSheet(sheetNames.length == 0 ? ("sheet1") : sheetNames[0]).head(clazz[0]).build();
        excelWriter.write(new ArrayList<>(), sheet);
    }

    private void modifyHead(int index, boolean flag){
        modifyAnnotationParam(flag ? clazz[0] : clazz[index], map -> {
            String[] value = (String[]) map.get("value");
            map.put("value", new String[]{titles[index], (value.length == 2 ? value[1] : value[0])});
        });
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

    private static void responseErrorMsg(HttpServletResponse response, String message) {
        //重置 response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "failure");
        map.put("message", "导出失败：" + message);
        try {
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(map));
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
