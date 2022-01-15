package com.lzk.toolboxes.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.utils.*;
import com.lzk.toolboxes.utils.excel.ExcelUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        return IpUtils.getIpAddr(request);//172.16.129.149
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

    @RequestMapping("/captcha")
    public void captcha(HttpServletResponse response){
        response.setContentType("image/jpg");
        try (ServletOutputStream outputStream = response.getOutputStream()){
            BufferedImage image = CaptchaUtils.create(200, 100, RandomCodeUtils.getRandomCode(4));
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
