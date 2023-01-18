package com.lzk.toolboxes.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @module
 * @date 2023/1/17 15:37
 */

@Data
public class Eval {

    /** 评议项表 */
    private Long id;

    /** 评议项编号 */
    private String code;

    /** 评议项名称 */
    private String evalName;

    /** 级别(1一级、2二级) */
    private Integer level;

    /** 父级评议项id */
    private Long parentId;

    /** 排序 */
    private Integer sort;

    /** 评议内容 */
    private String content;

    /** 状态（0开启、1关闭） */
    private Integer status;

    /** 是否为总评议项（0否，1是） */
    private Integer isTotalEval;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /** 年份 */
    private String year;

    @TableField(exist = false)
    private List<Eval> children;

    @TableField(exist = false)
    private int floorsNum;
}
