package com.lzk.toolboxes.controller;

import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.IpUtils;
import com.lzk.toolboxes.utils.PdfToImageUtil;
import com.lzk.toolboxes.utils.UuidUtil;
import com.lzk.toolboxes.utils.excel.ExcelUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/getMyIP")
    public String getMyIP(HttpServletRequest request){
        return UuidUtil.get32UUID();
    }

    @PostMapping("/pdfToImage")
    public void pdfToImage(HttpServletRequest request, HttpServletResponse response){
        try {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            PdfToImageUtil.pdfToImage(file.getInputStream(),request.getParameter("type"),response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/imageToPdf")
    public void imageToPdf(HttpServletRequest request,HttpServletResponse response){
        try {
            PdfToImageUtil.imageToPdf(request.getInputStream(),response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
