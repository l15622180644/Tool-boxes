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
 * @date 2022/11/2 12:00
 */
public class SexConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(value!=null){
            switch (value){
                case 0:
                    return new WriteCellData<String>("男");
                case 1:
                    return new WriteCellData<String>("女");
                default:
                    return new WriteCellData<String>("");
            }
        }
        return null;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(cellData.getType() != CellDataTypeEnum.EMPTY){
            if(cellData.getType() == CellDataTypeEnum.STRING){
                switch (cellData.getStringValue()){
                    case "男":
                        return 0;
                    case "女":
                        return 1;
                    case "":
                        return null;
                    default:
                        throw new RuntimeException("性别转换异常");
                }
            }else {
                throw new RuntimeException("性别转换异常");
            }
        }
        return null;
    }

}
