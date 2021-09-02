package com.lzk.toolboxes.utils;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author
 * @module 邮箱发送工具
 * @date 2021/6/9 19:47
 */
public class EmailUtil {

//    服务器地址、加密端口
//    QQ邮箱：SMTP服务器是smtp.qq.com，端口是465/587；
//    163邮箱：SMTP服务器是smtp.163.com，端口是465；
//    Gmail邮箱：SMTP服务器是smtp.gmail.com，端口是465/587。
    private final String HOST = "smtp.163.com";
    private final String PORT = "465";
//    发送方邮箱账号
    private final String USERNAME = "l15622180644@163.com";
//    private final String USERNAME = "2257638971@qq.com";
//    private final String PASSWORD = "lzk1565343980";
//    授权码 QQ
//    private final String PASSWORD = "fvidsammzuewebhg";
//    授权码 163
    private final String PASSWORD = "NZVROBTLGGSLVVXC";
    private MimeMessage message;

    public EmailUtil() throws MessagingException {
        //        连接到SMTP服务器端口:
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST); // SMTP主机名
        props.put("mail.smtp.port", PORT); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
//        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        props.put("mail.smtp.ssl.enable", "true");
//        获取Session实例:
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
//        设置debug模式便于调试:
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
//        设置发送方地址（一般与验证的用户相同）:
        message.setFrom(new InternetAddress(USERNAME));
        this.message = message;
    }

    /**
     * 发送普通文本邮箱
     *
     * @param recipient
     * @param title
     * @param content
     */
    public void sendTextEmail(String recipient, String title, String content) {

        try {
//        设置接收方地址:
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//        设置邮件主题:
            message.setSubject(title, "utf-8");
//        设置邮件正文:
            message.setText(content);
//        发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送html邮箱
     *
     * @param recipient
     * @param title
     * @param content
     */
    public void sendHtmlEmail(String recipient, String title, String content) {

        try {
//        设置接收方地址:
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//        设置邮件主题:
            message.setSubject(title, "utf-8");
//        设置邮件正文:
            message.setText(content, "utf-8", "html");
//        发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送带附件的邮箱
     *
     * @param recipient
     * @param title
     * @param content
     * @param filePath
     */
    public void sendAttachmentEmail(String recipient, String title, String content, String filePath) {
        FileInputStream inputStream = null;
        try {
//        设置接收方地址:
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//        设置邮件主题:
            message.setSubject(title, "utf-8");
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(textPart);
            MimeBodyPart filePart = new MimeBodyPart();
            filePart.setFileName("请你看看附件吧");
            filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(inputStream, "application/octet-stream")));
            multipart.addBodyPart(filePart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendHtmlEmailForImage(String recipient, String title, String content, String filePath) {
        FileInputStream inputStream = null;
        try {
//        设置接收方地址:
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//        设置邮件主题:
            message.setSubject(title, "utf-8");
            inputStream = new FileInputStream(new File(filePath));
            Multipart multipart = new MimeMultipart();
//        添加text:
            BodyPart textpart = new MimeBodyPart();
            textpart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(textpart);
//        添加image:
            BodyPart imagepart = new MimeBodyPart();
            imagepart.setFileName("pms_1621955784.84614979.jpg");
            imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(inputStream, "image/jpg")));
//        与HTML的<img src="cid:img01">关联:
            imagepart.setHeader("Content-ID", "<img01>");
            multipart.addBodyPart(imagepart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws MessagingException {
        EmailUtil emailUtil = new EmailUtil();
        emailUtil.sendHtmlEmailForImage("2257638971@qq.com", "大红色的富时罗素付款方式怎么写", "<h1>fvjdlaaaaaad</h1><p><img src=\"cid:img01\"></p>", "D:\\用户目录\\我的图片\\图片\\pms_1621955784.84614979.jpg");
    }


}
