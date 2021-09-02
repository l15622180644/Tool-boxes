package com.lzk.toolboxes.controller;

import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.excel.ExcelUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @module
 * @date 2021/8/7 9:13
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/export")
    public void export(HttpServletResponse response){
        List data = new ArrayList<>();
        for(int i=0;i<7;i++){
            Message message = new Message();
            message.setId(i);
            message.setContent("ssssss");
            message.setCreateTime(1718738324714948448L);
            message.setRead(1.123);
            message.setSenderName("nnn");
            message.setType(i);
            message.setRecipient(i);
            message.setRecipientName("llllll");
            message.setSender(90);
            data.add(message);
        }
        ExcelUtil excelUtil = new ExcelUtil(Message.class);
        try {
            excelUtil.exportExcel(response,"测试文件","测试标题",data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
