package com.lzk.toolboxes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelTreeName {

    //节点名称
    private String nodeName;

    //树最底部叶子数
    private int mergeDown;

    //向右合并
    private int mergeAcross;

    public ExcelTreeName(String nodeName, int mergeDown) {
        this.nodeName = nodeName;
        this.mergeDown = mergeDown;
    }
}
