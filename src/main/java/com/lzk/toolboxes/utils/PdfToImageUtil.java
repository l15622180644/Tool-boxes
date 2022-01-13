package com.lzk.toolboxes.utils;

import cn.hutool.core.img.ImgUtil;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipOutputStream;


public class PdfToImageUtil {

    private static final String rootPath = "D:/File";

    private PdfToImageUtil(){};

    //pdfbox包
    public static void pdfToImage(InputStream in, String type,OutputStream out){
        PDDocument document = null;
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        String path = rootPath + "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE);
        File filePath = new File(path);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        List<File> files = new ArrayList<>();
        try {
            document = PDDocument.load(in);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            //文档页数
            int pages = document.getNumberOfPages();
            for(int i=0;i<pages;i++){
                String fileUrl = path + "/" + UuidUtil.get32UUID() + "." + type;
                File file = new File(fileUrl);
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 300);//dpi（像素）越高越清晰，300生成2480*3507
                fileOutputStream = new FileOutputStream(file);
                ImageIO.write(bufferedImage,type,fileOutputStream);
                ImageUtils.handleDpiOfJPEG(file,300,300);//设置dpi，300可将图片物理尺寸设置成A4
                files.add(file);
            }
            ZipUtils.toZip(files,out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(zipOutputStream!=null){
                    zipOutputStream.close();
                }
                out.close();
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
                in.close();
                if(document!=null){
                    document.close();
                }
                files.forEach(v->v.delete());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //pdfbox包
    public static void imageToPdf(InputStream in, OutputStream out){
        PDDocument pdDocument = null;
        PDPageContentStream contentStream = null;
        try {

            pdDocument = new PDDocument();

            int read;
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((read = in.read(bytes,0,bytes.length))!=-1){
                byteArrayOutputStream.write(bytes,0,read);
            }
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdDocument, byteArrayOutputStream.toByteArray(), "");
            //设置页面尺寸
            PDRectangle pdRectangle = PDRectangle.A4;
            PDPage page = new PDPage(pdRectangle);
            pdDocument.addPage(page);
            //设置图片尺寸
            contentStream = new PDPageContentStream(pdDocument,page);
            contentStream.drawImage(pdImage, 0, 0,pdRectangle.getWidth(),pdRectangle.getHeight());
            contentStream.close();

            pdDocument.save(out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                if(pdDocument!=null){
                    pdDocument.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //itextpdf包
    public static void imageToPdf(String source, String target) {
        Document document = new Document();
        // 设置文档页边距
        document.setMargins(0, 0, 0, 0);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(target);
            PdfWriter.getInstance(document, fos);
            // 打开文档
            document.open();
            // 获取图片的宽高
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(source);

            float imageHeight = image.getScaledHeight();
            float imageWidth = image.getScaledWidth();
            // 设置页面宽高与图片一致
            com.itextpdf.text.Rectangle rectangle = new com.itextpdf.text.Rectangle(imageWidth, imageHeight);

            document.setPageSize(rectangle);
            // 图片居中
            image.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
            // 新建一页添加图片
            document.newPage();
            document.add(image);
        } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            // 关闭文档
            document.close();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws IOException, BadElementException {
    }

}



