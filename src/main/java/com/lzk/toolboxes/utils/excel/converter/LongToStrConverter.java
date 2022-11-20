package com.lzk.toolboxes.utils.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author
 * @module
 * @date 2022/11/2 10:51
 */
public class LongToStrConverter implements Converter<Long> {


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
        return new WriteCellData<String>(value.toString());
    }

    @Override
    public Long convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(cellData.getType().equals(CellDataTypeEnum.STRING)){
            return Long.parseLong(cellData.getStringValue());
        }else if(cellData.getType().equals(CellDataTypeEnum.NUMBER)){
            return cellData.getNumberValue().longValue();
        }else {
            throw new RuntimeException("Long类型转换异常");
        }
    }
}
