package com.lzk.toolboxes.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class AesUtil {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";    //"算法/模式/补码方式"
    private static String key = "abcdef0123456789";

    /*****************************************************
     * AES加密（对称加密算法使用同一个密钥进行加密和解密）
     * @param content 加密内容
    此方法使用AES-128-ECB加密模式，key需要为16位
    加密解密key必须相同，如：abcd1234abcd1234
     * @return 加密密文
     ****************************************************/

    public static String enCode(String content) {

        if (key == null || "".equals(key)) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.ENCRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] byte_content = content.getBytes("utf-8"); //获取加密内容的字节数组(设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] encode_content = cipher.doFinal(byte_content); //密码器加密数据
            return Base64.encodeBase64String(encode_content); //将加密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //fms专用
    public static String deCode(String content, String key) {
        key = key.split("-")[0];
        if (key == null || "".equals(key)) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.DECRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] encode_content = Base64.decodeBase64(content); //把密文字符串转回密文字节数组
            byte[] byte_content = cipher.doFinal(encode_content); //密码器解密数据
            return new String(byte_content, "utf-8"); //将解密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*****************************************************
     * AES解密
     * @param content 加密密文
    此方法使用AES-128-ECB加密模式，key需要为16位
    加密解密key必须相同
     * @return 解密明文
     ****************************************************/

    public static String deCode(String content) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.DECRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] encode_content = Base64.decodeBase64(content); //把密文字符串转回密文字节数组
            byte[] byte_content = cipher.doFinal(encode_content); //密码器解密数据
            return new String(byte_content, "utf-8"); //将解密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PBE算法
     *
     * @param content
     * @param salt
     * @return
     */
    public static String enCodeByPBE(String content, byte[] salt) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            String password = key;// 加密口令
//            System.out.println(java.util.Base64.getEncoder().encodeToString(salt));
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            PBEParameterSpec pbeps = new PBEParameterSpec(salt, 1000);//1000为循环次数,安全性高,比较消耗性能
            Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeps);
            return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] getSalt() {
        try {
            return SecureRandom.getInstanceStrong().generateSeed(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PBE解密
     *
     * @param content
     * @param salt
     * @return
     */
    public static String deCodeByPBE(String content, String salt) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            String password = key;// 加密口令
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
            SecretKey skey = skeyFactory.generateSecret(keySpec);
            byte[] salt1 = java.util.Base64.getDecoder().decode(salt);
            PBEParameterSpec pbeps = new PBEParameterSpec(salt1, 1000);
            Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
            cipher.init(Cipher.DECRYPT_MODE, skey, pbeps);
            byte[] result = cipher.doFinal(java.util.Base64.getDecoder().decode(content));
            return new String(result, "utf-8");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        byte[] salt = getSalt();
        byte[] bytes = getSalt();
        String s = enCodeByPBE("drioe", salt);
        System.out.println(deCodeByPBE(s, java.util.Base64.getEncoder().encodeToString(salt)));
    }


}
