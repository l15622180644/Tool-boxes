package com.lzk.toolboxes.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzk.toolboxes.config.base.BaseResult;
import com.lzk.toolboxes.config.base.Status;
import com.lzk.toolboxes.entity.Message;
import com.lzk.toolboxes.entity.Users;
import com.lzk.toolboxes.mapper.MessageMapper;
import com.lzk.toolboxes.mapper.UsersMapper;
import com.lzk.toolboxes.utils.*;
import com.lzk.toolboxes.utils.excel.EasyExcelUtil;
import com.lzk.toolboxes.utils.excel.listener.CustomReadListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author
 * @module
 * @date 2021/8/7 9:13
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UsersMapper usersMapper;

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

    @PostMapping("/import")
    @Transactional
    public BaseResult<Object> easyExcelImport(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        List<String> errorMsg = new ArrayList<>();
        EasyExcelUtil excelUtil = EasyExcelUtil.create(Message.class);
        try (ExcelReader reader = EasyExcel.read(multipartFile.getInputStream(),Message.class,new CustomReadListener<>(Message.class,messageMapper,errorMsg,((message, analysisContext) -> {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            if(StringUtils.isNotBlank(message.getSender())){
                Users users = usersMapper.selectOne(Wrappers.lambdaQuery(Users.class).like(Users::getName, message.getSender()));
                if(users!=null) {
                    message.setSenderId(users.getId());
                }else {
                    errorMsg.add(excelUtil.createErrorMsg(rowIndex,"sender","查无此人"));
                    return false;
                }
            }
            if(StringUtils.isNotBlank(message.getRecipient())){
                Users users = usersMapper.selectOne(Wrappers.lambdaQuery(Users.class).like(Users::getName, message.getRecipient()));
                if(users!=null) {
                    message.setRecipientId(users.getId());
                }else {
                    errorMsg.add(excelUtil.createErrorMsg(rowIndex,"recipient","查无此人"));
                    return false;
                }
            }
            return true;
        }))).headRowNumber(2).build()){
            reader.read(excelUtil.readSheet(0));
            return errorMsg.isEmpty() ? BaseResult.returnResult(true) : BaseResult.error(String.join("\n",errorMsg));
        }
    }

    @RequestMapping("/export")
    public void easyExcelExport(boolean isTemplate,HttpServletResponse response) throws IOException {
        EasyExcelUtil excelUtil = EasyExcelUtil.create(Message.class).setWebFileName(response,"test");
        try (ExcelWriter excelWriter = excelUtil.excelWriter(response.getOutputStream())){
            WriteSheet sheet1 = excelUtil.writerSheet(0, "Sheet1","消息标题");
            List<Message> data = Collections.emptyList();
            if(!isTemplate) data = messageMapper.selectList(Wrappers.lambdaQuery(Message.class));
            excelWriter.write(data,sheet1);
        }
    }
}
