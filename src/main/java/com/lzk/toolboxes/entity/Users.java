package com.lzk.toolboxes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Users implements Serializable {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private String name;
}
