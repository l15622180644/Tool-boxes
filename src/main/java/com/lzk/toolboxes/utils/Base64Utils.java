package com.lzk.toolboxes.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * DateTime: 2016/9/19 14:07
 * 功能：生成64位的图片
 * 思路：
 */
public class Base64Utils {


    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param srcPath    生成64编码的图片的路径
     * @return
     */
    public static String encode(String srcPath){
        InputStream in=null;
        byte[] data=null;

        //读取图片字节数组
        try {
            in=new FileInputStream(srcPath);
            data=new byte[in.available()];
            in.read(data);
            in.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return encode(data);

    }

    public static String encode(byte[] bytes){
        //对字节数组Base64编码
        BASE64Encoder encoder=new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(bytes);
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param str    转换为图片的字符串
     * @param createPath 将64编码生成图片的路径
     * @return
     */
    public static boolean decode(String str,String createPath){
        if(null==str){   //图像数据为空
            return false;
        }
        BASE64Decoder decoder=new BASE64Decoder();
        try {
            //Base64解码
            byte[] b=decoder.decodeBuffer(str);
            for ( int i = 0; i < b.length; i++ ) {
                if(b[i]<0){ //调整异常数据
                    b[i]+=256;
                }
            }

            OutputStream out=new FileOutputStream(createPath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch ( IOException e ) {
            e.printStackTrace();
            return false;
        }
    }

}
