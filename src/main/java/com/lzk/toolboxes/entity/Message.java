package com.lzk.toolboxes.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lzk.toolboxes.utils.excel.annotation.ExcelCheck;
import com.lzk.toolboxes.utils.excel.converter.LongToStrConverter;
import com.lzk.toolboxes.utils.excel.converter.SexConverter;
import com.lzk.toolboxes.utils.excel.converter.TimeStampConverter;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @module
 * @date 2021/6/19 14:30
 */
@Data
public class Message implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "内容")
    @ExcelCheck(notNull = true)
    private String content;

    @ExcelIgnore
    private Long senderId;

    @ExcelProperty(value = "发送人")
    private String sender;

    @ExcelIgnore
    private Long recipientId;

    @ExcelProperty(value = "接收人")
    private String recipient;

    @ExcelProperty(value = "类型",converter = SexConverter.class)
    private Integer type;

    @TableField(fill = FieldFill.INSERT)
    @ExcelProperty(value = "创建时间", converter = TimeStampConverter.class)
    private Long createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelProperty(value = "更新时间", converter = TimeStampConverter.class)
    private Long updateTime;
}
