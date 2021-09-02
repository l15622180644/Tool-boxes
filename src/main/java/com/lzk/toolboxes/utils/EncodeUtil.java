package com.lzk.toolboxes.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;


/**
 * @author
 * @module 编码工具
 * @date 2021/6/18 15:07
 */
public class EncodeUtil {

    public static void main(String[] args) {
        EncodeUtil encodeUtil = new EncodeUtil();
        System.out.println(encodeUtil.charEncoder("高达",""));
    }

    private String UTF8 = "UTF-8";

    /**
     * 字符编码
     *
     * @param s
     * @param charset
     * @return
     */
    public String charEncoder(String s, String charset) {
        String encode = "";
        try {
            encode = URLEncoder.encode(s, StringUtils.isNotBlank(charset) ? charset : UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    /**
     * 字符解码
     *
     * @param s
     * @param charset
     * @return
     */
    public String charDecode(String s, String charset) {
        String decode = "";
        try {
            decode = URLDecoder.decode(s, StringUtils.isNotBlank(charset) ? charset : UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 二进制转base64编码
     * @param bytes
     * @return
     */
    public String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64解码
     * @param s
     * @return
     */
    public byte[] base64Decode(String s){
        return Base64.getDecoder().decode(s);
    }
}
