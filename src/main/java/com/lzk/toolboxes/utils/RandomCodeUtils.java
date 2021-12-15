package com.lzk.toolboxes.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author
 * @module
 * @date 2021/12/14 15:35
 */
public class RandomCodeUtils {

    private static final char[] charSequence = "AaBbC0cDdEe1FfGgH2hIiJj3KkLlM4mNnOo5PpQqR6rSsTt7UuVvW8wXxYy9Zz".toCharArray();

    private static final char[] number = "0123456789".toCharArray();

    private static final char[] upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final char[] lowerCase = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static Random random = new Random();

    //种子值
    private static long seed;

    private RandomCodeUtils() {}

    /**
     * 设置种子值
     * @param s 随机数生成器的种子值
     */
    public static void setSeed(long s){
        seed = s;
        random = new Random(seed);
    }

    /**
     * 获取种子值
     * @return long 随机数生成器的种子值
     */
    public static long getSeed(){
        return seed;
    }

    /**
     * 随机返回0到1之间的实数 [0,1)
     * @return double 随机数
     */
    public static double uniform(){
        return random.nextDouble();
    }

    /**
     * 随机返回0到N-1之间的整数 [0,N)
     * @param N 上限
     * @return int 随机数
     */
    public static int uniform(int N){
        return random.nextInt(N);
    }

    /**
     * 随机返回0到1之间的实数 [0,1)
     * @return double 随机数
     */
    public static double random(){
        return uniform();
    }

    /**
     * 随机返回a到b-1之间的整数 [a,b)
     * @param a 下限
     * @param b 上限
     * @return int 随机数
     */
    public static int uniform(int a,int b){
        return a + uniform(b - a);
    }

    /**
     * 随机返回a到b之间的实数
     * @param a 下限
     * @param b 上限
     * @return double 随机数
     */
    public static double uniform(double a,double b){
        return a + uniform() * (b - a);
    }

    public static String getRandomCode(int len){
        if(len==0) return "";
        char[] temp = new char[len];
        for(int i=0;i<len;i++){
            int nextInt = random.nextInt(charSequence.length);
            temp[i] = charSequence[nextInt];
        }
        return String.valueOf(temp);
    }

    public static String getNumberRandomCode(int len){
        if(len==0) return "";
        char[] temp = new char[len];
        for(int i=0;i<len;i++){
            int nextInt = random.nextInt(number.length);
            temp[i] = number[nextInt];
        }
        return String.valueOf(temp);
    }

    public static String getUpperCaseRandomCode(int len){
        if(len==0) return "";
        char[] temp = new char[len];
        for(int i=0;i<len;i++){
            int nextInt = random.nextInt(upperCase.length);
            temp[i] = upperCase[nextInt];
        }
        return String.valueOf(temp);
    }

    public static String getLowerCaseRandomCode(int len){
        if(len==0) return "";
        char[] temp = new char[len];
        for(int i=0;i<len;i++){
            int nextInt = random.nextInt(lowerCase.length);
            temp[i] = lowerCase[nextInt];
        }
        return String.valueOf(temp);
    }

    /**
     * 返回随机字符串，同时包含数字、大小写字母
     * @param len 字符串长度，不能小于3
     * @return String 随机字符串
     */
    public static String randomStr(int len){
        if(len < 3){
            throw new IllegalArgumentException("字符串长度不能小于3");
        }
        //数组，用于存放随机字符
        char[] chArr = new char[len];
        //为了保证必须包含数字、大小写字母
        chArr[0] = (char)('0' + uniform(0,10));
        chArr[1] = (char)('A' + uniform(0,26));
        chArr[2] = (char)('a' + uniform(0,26));


        char[] codes = { '0','1','2','3','4','5','6','7','8','9',
                         'A','B','C','D','E','F','G','H','I','J',
                         'K','L','M','N','O','P','Q','R','S','T',
                         'U','V','W','X','Y','Z','a','b','c','d',
                         'e','f','g','h','i','j','k','l','m','n',
                         'o','p','q','r','s','t','u','v','w','x',
                         'y','z'};
        //charArr[3..len-1]随机生成codes中的字符
        for(int i = 3; i < len; i++){
            chArr[i] = codes[uniform(0,codes.length)];
        }

        //将数组chArr随机排序
        for(int i = 0; i < len; i++){
            int r = i + uniform(len - i);
            char temp = chArr[i];
            chArr[i] = chArr[r];
            chArr[r] = temp;
        }

        return new String(chArr);
    }

    public static boolean verify(String checked,String trueCode){
        if(checked==null) return false;
        if(trueCode==null) return false;
        return checked.trim().equalsIgnoreCase(trueCode);
    }


}



