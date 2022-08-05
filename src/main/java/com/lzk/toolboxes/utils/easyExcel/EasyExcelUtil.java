package com.lzk.toolboxes.utils.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.OutputStream;
import java.util.List;

/**
 * @author
 * @module
 * @date 2022/7/26 15:46
 */
public class EasyExcelUtil {

    public static void writeExcel(OutputStream outputStream, Class<?> aClass, List<?> data) {
        EasyExcel.write(outputStream, aClass).sheet().doWrite(data);
    }

    public static void writeExcelWithSheets(OutputStream outputStream, String[] sheetNames, Class<?> aClass, List<?>[] data) {
        writeExcelWithSheets(outputStream, sheetNames, new Class<?>[]{aClass}, data);
    }

    public static void writeExcelWithSheets(OutputStream outputStream, String[] sheetNames, Class<?>[] aClasses, List<?>[] data) {
        boolean flag = aClasses.length == 1;
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            for (int i = 0; i < sheetNames.length; i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i, sheetNames[i]).head(flag ? aClasses[0] : aClasses[i]).build();
                excelWriter.write(data[i], sheet);
            }
        }
    }

}
