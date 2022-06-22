package com.lzk.toolboxes.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @module
 * @date 2021/8/7 9:09
 */
public class ExcelUtil {
    Class<?> aClass;
    Workbook workbook;
    Field[] fields;
    Map<String, CellStyle> styleMap;

    public ExcelUtil(Class aClass) {
        this.aClass = aClass;
        this.fields = aClass.getDeclaredFields();
        this.workbook = cn.hutool.poi.excel.ExcelUtil.getWriter(true).getWorkbook();
    }

    public void exportExcel(HttpServletResponse response, String fileName, String title, List data) throws IOException {
        //设置响应投头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
        exportExcel(title, data, response.getOutputStream());
    }

    private void exportExcel(String title, List data, OutputStream out) {
        setStyle();//设置样式
        try {
            Sheet sheet = workbook.getSheetAt(0);
            int titleRow = setTitle(title, sheet);
            int headRow;
            headRow = setHead(sheet, titleRow);
            setBody(data, (titleRow + headRow), sheet);
            setColumnWidth(sheet);
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建标题
     *
     * @param title
     * @param sheet
     * @return 返回构建所使用的行数
     */
    private int setTitle(String title, Sheet sheet) {
        if (title != null && !"".equals(title)) {
            int num = 0;
            for (Field field : this.fields) {
                ExcelField annotation = field.getAnnotation(ExcelField.class);
                if (annotation.isExport()) {
                    num++;
                }
            }
            CellRangeAddress range = new CellRangeAddress(0, 0, 0, num - 1);
            sheet.addMergedRegion(range);
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(title);
            cell.setCellStyle(styleMap.get("title"));
            fillBorder(Arrays.asList(range), sheet);
            return 1;
        }
        return 0;
    }

    /**
     * 构建头部（通用）
     *
     * @param sheet
     * @param num   从哪一行开始构建
     * @return 构建所使用的行数
     */
    private int setHead(Sheet sheet, int num) {
        Row row = sheet.createRow(num);
        int temp = 0;
        for (int i = 0; i < this.fields.length; i++) {
            ExcelField annotation = this.fields[i].getAnnotation(ExcelField.class);
            if (annotation.isExport()) {
                Cell cell = row.createCell(temp);
                cell.setCellValue(annotation.name());
                cell.setCellStyle(styleMap.get("title"));
                temp++;
            }
        }
        return 1;
    }


    /**
     * 构建数据行
     *
     * @param data  数据
     * @param num   从哪一行开始构建
     * @param sheet
     */
    private void setBody(List data, int num, Sheet sheet) {
        int temp = 0;
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + num);
                for (int j = 0; j < this.fields.length; j++) {
                    ExcelField annotation = this.fields[j].getAnnotation(ExcelField.class);
                    if (annotation.isExport()) {
                        Cell cell = row.createCell(temp);
                        setCellValue(cell, fields[j], data.get(i), annotation);
                        cell.setCellStyle(styleMap.get("body"));
                        temp++;
                    }
                }
                temp = 0;
            }
        }
    }

    /**
     * 通过反射获取get方法，从而获取属性值
     *
     * @param field
     * @param object
     * @return
     */
    private Object getValue(Field field, Object object) {
        try {
            Class<?> aClass = object.getClass();
            Method method = aClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            Object value = method.invoke(object);
            return value;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 设置单元格值
     *
     * @param cell
     * @param field
     * @param object
     * @param annotation
     */
    private void setCellValue(Cell cell, Field field, Object object, ExcelField annotation) {
        String genericType = field.getGenericType().toString();
        Object o = getValue(field, object);
        if (genericType.equals("class java.lang.String")) {
            cell.setCellValue((String) o);
        } else if (genericType.equals("class java.lang.Double") || genericType.equals("double")) {
            if (o != null && !"".equals(o)) {
                cell.setCellValue((Double) o);
            }
        } else if (genericType.equals("class java.lang.Boolean")) {
            if (o != null && !"".equals(o)) {
                cell.setCellValue((Boolean) o);
            }
        } else if (genericType.equals("class java.lang.Integer")
                || genericType.equals("class java.lang.Long")
                || genericType.equals("class java.lang.Float")) {
            if (o != null && !"".equals(o)) {
                cell.setCellValue(Double.parseDouble(o.toString()));
            }
        } else if (o != null) {
            cell.setCellValue(o.toString());
        }
        if (!"".equals(annotation.readConverterExp()) && o != null) {
            String convertSource = annotation.readConverterExp();
            String[] items = convertSource.split(",");
            for (String item : items) {
                String[] strings = item.split("=");
                if (strings[0].equals(o.toString())) {
                    cell.setCellValue(strings[1]);
                }
            }
        }else if(!"".equals(annotation.formatTimestamp()) && o !=null){
            SimpleDateFormat format = new SimpleDateFormat(annotation.formatTimestamp());
            Date date = new Date();
            date.setTime(Long.parseLong(o.toString()+(o.toString().length()==10?"000":"")));
            cell.setCellValue(format.format(date));
        }
    }

    private void setStyle() {
        this.styleMap = new HashMap<>();
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setTopBorderColor(IndexedColors.BLACK.index);
        titleStyle.setRightBorderColor(IndexedColors.BLACK.index);
        titleStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        titleStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleMap.put("title", titleStyle);
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setTopBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setRightBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleMap.put("body", bodyStyle);
    }

    /**
     * 设置列宽
     *
     * @param sheet
     */
    private void setColumnWidth(Sheet sheet) {
        sheet.setDefaultColumnWidth(6);
        int temp = 0;
        for (int i = 0; i < this.fields.length; i++) {
            ExcelField annotation = fields[i].getAnnotation(ExcelField.class);
            if (annotation.isExport()) {
                sheet.setColumnWidth(temp, annotation.name().getBytes().length * 256 + 3 * 256);
                temp++;
            }
        }
    }

    /**
     * 合并后的单元格边框线会显示不全，需做处理
     *
     * @param list  合并地址对象集合
     * @param sheet
     */
    private void fillBorder(List<CellRangeAddress> list, Sheet sheet) {
        list.forEach(rangeAddress -> {
            RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
        });
    }
}
