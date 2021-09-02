package com.lzk.toolboxes.controller;

import com.lzk.toolboxes.utils.CodeGenerator;
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

    @GetMapping("/gen/{table}")
    public void genCode(@PathVariable("table") String table) throws Exception {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.genCode(table, true, true);
    }

}
