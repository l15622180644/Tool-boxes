package com.lzk.toolboxes.utils.excel.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCheck {

    boolean notNull() default false;
}
