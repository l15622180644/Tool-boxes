package com.lzk.toolboxes.utils.easyExcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 * @module
 * @date 2022/8/8 16:18
 */
public class TimeStampConverter implements Converter<Long> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Long value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<String>(new SimpleDateFormat("yyyy-MM-dd").format(new Date(value)));
    }

    @Override
    public Long convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        System.out.println(cellData.getStringValue());
        System.out.println(cellData.getBooleanValue());
        System.out.println(cellData.getDataFormatData());
        System.out.println(cellData.getNumberValue());
        System.out.println(cellData.getFormulaData());
        System.out.println(cellData.getType().name());
        return new SimpleDateFormat("yyyy-MM-dd").parse(cellData.getStringValue()).getTime()/1000;
    }


}
