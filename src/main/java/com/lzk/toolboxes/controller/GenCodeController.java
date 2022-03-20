package com.lzk.toolboxes.controller;

import com.lzk.toolboxes.utils.CodeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @module
 * @date 2021/5/25 17:32
 */
@RestController
public class GenCodeController {

    @GetMapping("/gen/{tableName}")
    public void genCode(@PathVariable("tableName") String tableName) throws Exception {
        if(StringUtils.isBlank(tableName)) return;
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.genCode(tableName, true, true);
    }

}
