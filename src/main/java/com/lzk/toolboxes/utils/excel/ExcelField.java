package com.lzk.toolboxes.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    //属性别名
    String name() default "";
    //是否导出该属性及其数据
    boolean isExport() default true;
    //内容表达转换 (如: 0=男,1=女,2=未知)
    String readConverterExp() default "";
    //格式化时间戳
    String formatTimestamp() default "";
}
