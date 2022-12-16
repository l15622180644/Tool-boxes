package com.lzk.toolboxes.utils.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author
 * @module
 * @date 2022/11/2 10:46
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
        return new WriteCellData<String>(new SimpleDateFormat("yyyy/MM/dd").format(new Date(value * 1000)));
    }

    @Override
    public Long convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(cellData.getType().equals(CellDataTypeEnum.STRING)){
            return new SimpleDateFormat("yyyy/MM/dd").parse(cellData.getStringValue()).getTime()/1000;
        }else if(cellData.getType().equals(CellDataTypeEnum.NUMBER)){
            Date date = Date.from(LocalDate
                    .of(1900, 1, 1)
                    .plusDays(cellData.getNumberValue().longValue() - 2)
                    .atStartOfDay(ZoneOffset.ofHours(8))
                    .toInstant());
            return date.getTime() / 1000;
        }else {
            throw new RuntimeException("时间转换异常");
        }
    }

}
