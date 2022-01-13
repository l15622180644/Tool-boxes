package com.lzk.toolboxes.utils;


import cn.hutool.core.img.ImgUtil;
import com.itextpdf.text.BadElementException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.w3c.dom.Element;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {

    /**
     *  下面是一些常用分辨率下A4纸在屏幕上的像素尺寸：
     *
     * 分辨率是72像素/英寸时，A4纸的尺寸的图像的像素是595×842；
     *
     * 分辨率是96像素/英寸时，A4纸的尺寸的图像的像素是794×1123；(默认)
     *
     * 分辨率是120像素/英寸时，A4纸的尺寸的图像的像素是1487×2105；
     *
     * 分辨率是150像素/英寸时，A4纸的尺寸的图像的像素是1240×1754；
     *
     * 分辨率是300像素/英寸时，A4纸的尺寸的图像的像素是2480×3508；
     *
     *
     *
     * 其他的大小，一般标准印刷300dpi时：
     *
     * A4纸的尺寸的图像的像素是2480×3508；
     *
     * A3纸的尺寸的图像的像素是4960×3508；
     *
     * B3纸的尺寸的图像的像素是3248×4300；
     *
     * B4纸的尺寸的图像的像素是3248×2150。
     */



    //处理JPEG格式
    public static void handleDpiOfJPEG(File file,int x,int y){
        String name = file.getName();
        String suffix = name.substring(name.lastIndexOf(".")+1);
        if(!suffix.equalsIgnoreCase("jpg") && !suffix.equalsIgnoreCase("jpeg")){
            System.out.println("该文件不支持修改DPI："+name);
            return;
        }
        FileOutputStream fileOutputStream = null;
        try {
            BufferedImage image = ImageIO.read(file);
            fileOutputStream = new FileOutputStream(file);
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
            jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
            jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
            jpegEncodeParam.setQuality(0.75f,false);
            jpegEncodeParam.setXDensity(x);
            jpegEncodeParam.setYDensity(y);
            jpegEncoder.encode(image,jpegEncodeParam);
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void getImageInfoByCommonsImaging(String path) {
        try {
            ImageInfo image = Imaging.getImageInfo(new File(path));
            System.out.println("-----------------start-通过commons-imaging获取图片数据----------------");
            System.out.println(image);
            System.out.println("-----------------end-通过commons-imaging获取图片数据----------------");
            int xDpi = image.getPhysicalWidthDpi();
            int yDpi = image.getPhysicalHeightDpi();
            System.out.println("xDpi:" + xDpi + ",yDpi:" + yDpi + ",type:" + image.getMimeType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scale(String s1,String s2,int width,int height) throws IOException, BadElementException {
        ImgUtil.scale(new File(s1),new File(s2),width,height,null);
    }


    public static void convertType(File file,String convertType,OutputStream out) throws IOException {
        try {
            String path = file.getPath();
            BufferedImage bufferedImage= ImageIO.read(file);
            String suffix = path.substring(path.lastIndexOf(".")+1);
            if(suffix.equalsIgnoreCase(convertType)) {
                ImageIO.write(bufferedImage,suffix,out);
                return;
            }
            // create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            String fileName = path.substring(0,path.lastIndexOf("."));
            ImageIO.write(newBufferedImage, convertType, out);
        } finally {
            if(out!=null){
                out.close();
            }
        }

//        ImgUtil.convert(new FileInputStream(file),convertType,out);
    }

    public static void main(String[] args) throws IOException, BadElementException {
        /*BufferedImage image = ImageIO.read(new File("E:\\lzk\\011210120192_02021.8.12_1.Png"));
        byte[] bytes = ImageUtils.handleDpiOfPng(image, 300);
        int temp;
        byte[] bytes1 = new byte[1024];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        FileOutputStream out = new FileOutputStream(new File("E:\\lzk\\签名测试3.png"));
        while ((temp = inputStream.read(bytes1,0,bytes1.length))!=-1){
            out.write(bytes1,0,temp);
        }
        out.flush();
        out.close();
        inputStream.close();*/

//        ImageUtils.convertType("E:\\lzk\\3.jpg","E:\\lzk\\5.tiff");


//        ImageUtils.scale("E:\\lzk\\签名测试.png","E:\\lzk\\签名测试2.png");

//        ImageUtils.process(new File("E:\\lzk\\011210033432_02021.8.12_1.Bmp"),350,350);


        ImageUtils.getImageInfoByCommonsImaging("E:\\lzk\\011217283595_02021.8.12_1.Tiff");

//        ImageUtils.convertType(new File("D:\\File\\2022\\1\\12\\a5a8c70a5d46435e8469d313cf83ca16.jpg"),"jpeg",new FileOutputStream("D:\\File\\2022\\1\\12\\1.jpeg"));

//        ImageUtils.handleDpiOfJPEG(new File("E:\\lzk\\response.bmp"),300,300);

    }
}
