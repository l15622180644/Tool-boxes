package com.lzk.toolboxes.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lzk.toolboxes.config.base.BaseBean;
import com.lzk.toolboxes.utils.excel.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @module
 * @date 2021/6/19 14:30
 */
@Data
//@TableName("message")
public class Message extends Model<Message> implements Serializable {
//    @TableId(value = "id")
    @ExcelField(name = "ID")
    private Integer id;
    @ExcelField(name = "内容")
    private String content;
    @ExcelField(name = "发送者")
    private Integer sender;
    @ExcelField(name = "发送者名称")
    private String senderName;
    @ExcelField(name = "接受者")
    private Integer recipient;
    @ExcelField(name = "接受者名称")
    private String recipientName;
    @ExcelField(name = "创建时间")
    private Long createTime;
    @ExcelField(name = "类型")
    private Integer type;
    @ExcelField(name = "是否已读")
    private Double read;
}
