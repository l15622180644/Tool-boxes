package com.lzk.toolboxes.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;


public class HashUtil {

    //测试
    public static void main(String[] args) {
        System.out.println(RipeMD160("你好"));
        SecretKey key = getKey();
        String md5 = HmacMD5("123", key);
        String s = new BigInteger(1, key.getEncoded()).toString(16);
        System.out.println(compareHmacMD5("123", s, md5));
        System.out.println(SHA512("你好"));
        System.out.println(SHA256("你好"));
        System.out.println(SHA1("你好"));
    }


    /**
     * 输出长度（字节）:20 bytes
     * 输出长度（位）:160 bits
     *
     * @param str
     * @return
     */
    public static String RipeMD160(String str) {
        try {
//            使用RipeMD160算法需加载第三方库
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest md = MessageDigest.getInstance("RipeMD160");
            md.update(str.getBytes("UTF-8"));
            byte[] result = md.digest();
            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取特定字符串的MD5码
     * eg:md5("YEN") 输出：0d9962cb9239354e6da0ca55ebd0b93c
     * 输出长度（字节）：16字节
     * 输出长度（位）:128 bits
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] b = md.digest();

            int temp;
            StringBuffer sb = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                temp = b[offset];
                if (temp < 0) temp += 256;
                if (temp < 16) sb.append("0");
                sb.append(Integer.toHexString(temp));
            }
            str = sb.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 输出长度（字节）：20字节
     * 输出长度（位）:160 bits
     *
     * @param str
     * @return
     */
    public static String SHA1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes("UTF-8"));
            byte[] result = md.digest();
            return new BigInteger(1, result).toString(16);//转换为十六进制的字符串
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 输出长度（字节）：32 bytes
     * 输出长度（位）:256 bits
     *
     * @param str
     * @return
     */
    public static String SHA256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            byte[] result = md.digest();
            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 输出长度（字节）:64 bytes
     * 输出长度（位）:512 bits
     *
     * @param str
     * @return
     */
    public static String SHA512(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes("UTF-8"));
            byte[] result = md.digest();
            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加盐MD5
     * Hmac算法就是一种基于密钥的消息认证码算法，它的全称是Hash-based Message Authentication Code，是一种更安全的消息摘要算法
     *
     * @param str
     * @return
     */
    public static String HmacMD5(String str, SecretKey key) {
        try {
//            打印随机生成的key:
//            byte[] skey = key.getEncoded();
//            System.out.println(Arrays.toString(skey));
//            System.out.println(new BigInteger(1, skey).toString(16));
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);
            mac.update(str.getBytes("UTF-8"));
            byte[] result = mac.doFinal();
            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 随机生成秘钥
     * 长度是64字节
     *
     * @return
     */
    public static SecretKey getKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
            SecretKey key = keyGen.generateKey();
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HmacMD5校验
     *
     * @param unencrypted 未加密信息
     * @param key         16进制秘钥
     * @param encrypted   已加密信息
     * @return
     */
    public static boolean compareHmacMD5(String unencrypted, String key, String encrypted) {
        try {
            byte[] keyArray = new BigInteger(key, 16).toByteArray();
            if (keyArray[0] == 0) {
                byte[] temArray = new byte[keyArray.length - 1];
                System.arraycopy(keyArray, 1, temArray, 0, temArray.length);
                keyArray = temArray;
            }
            SecretKey skey = new SecretKeySpec(keyArray, "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(skey);
            mac.update(unencrypted.getBytes("UTF-8"));
            byte[] result = mac.doFinal();
            String s = new BigInteger(1, result).toString(16);
            return encrypted.equals(s);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }
}
