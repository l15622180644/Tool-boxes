package com.lzk.toolboxes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author
 * @module 正则校验
 * @date 2021/6/5 10:13
 */
public class RegexUtil {
    //身份证每位加权因子
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//17位
    //身份证第18位校检码
    private static String[] refNumber = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};//11位
    //省(直辖市)代码表
    /*
    11北京、12天津、13河北、14山西、15内蒙古、21辽宁、22吉林、23黑龙江、31上海、32江苏
    33浙江、34安徽、35福建、36江西、37山东、41河南、42湖北、43湖南、44广东、45广西、
    46海南、50重庆、51四川、52贵州、53云南、54西藏、61陕西、62甘肃、63青海、64宁夏、
    65新疆、71台湾、81香港、82澳门
     */
    private static String provinceCode[] = {
            "11", "12", "13", "14", "15", "21", "22", "23", "31", "32",
            "33", "34", "35", "36", "37", "41", "42", "43", "44", "45",
            "46", "50", "51", "52", "53", "54", "61", "62", "63", "64",
            "65", "71", "81", "82"};

    public static void main(String[] args) {
//        System.out.println(RegexUtil.isChineseName("你好"));
        System.out.println(isValidEmail("2257638971@qq.com"));

    }

    /**
     * 校验email
     *
     * @param s
     * @return
     */
    public static boolean isValidEmail(String s) {
        return Pattern.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", s);
    }

    /**
     * 校验身份证
     *
     * @param cardID
     * @return
     */
    public static boolean isValidIdentityCard(String cardID) {
        boolean matches = Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", cardID);
        if (matches && checkProvinceId(cardID) && isValidDate(cardID) && checkCardIdLastNum(cardID)) {
            return true;
        }
        return false;
    }

    /**
     * 校验中文姓名
     *
     * @param realname
     * @return
     */
    public static boolean isChineseName(String realname) {
        return Pattern.matches("[\u4e00-\u9fa5|·]{2,15}", realname);
    }


    /*public static String[] randomArray(int length) {
        int index = 0;
        String temp = ",";
        for (int ii = 0; ii < 1000; ii++) {
            for (int i = 0; i < length; i++) {
                index = (int) (Math.random() * 10);
                temp += (index + "");
            }
            temp += ",";
        }
        return temp.split(",");
    }*/


    /**
     * 检查身份证的省份信息是否正确（使用与18/15位身份证）
     *
     * @param cardId
     * @return
     */
    public static boolean checkProvinceId(String cardId) {
        for (String id : provinceCode) {
            if (id.equals(cardId.substring(0, 2))) {
                return true;
            }
        }
//        System.out.println("省份码不合法");
        return false;
    }

    /**
     * 校验身份证第18位是否正确(只适合18位身份证)
     *
     * @param cardID
     * @return
     */
    public static boolean checkCardIdLastNum(String cardID) {
        if (cardID.length() != 18) {
            return false;
        }
        char[] tmp = cardID.toCharArray();
        int[] cardidArray = new int[tmp.length - 1];
        int i = 0;
        for (i = 0; i < tmp.length - 1; i++) {
            cardidArray[i] = Integer.parseInt(tmp[i] + "");
        }
        String checkCode = sumPower(cardidArray);
        String lastNum = tmp[tmp.length - 1] + "";
        if (lastNum.equals("x")) {
            lastNum = lastNum.toUpperCase();
        }
        if (!checkCode.equals(lastNum)) {
//            System.out.println("第18位错误");
            return false;
        }
        return true;
    }

    /**
     * 计算身份证的第十八位校验码
     * 计算公式：身份证前17位分别乘以不同的系数（power），相乘的结果相加后除以11并取余，余数对应（refNumber）
     *
     * @param cardIDArray
     * @return
     */
    public static String sumPower(int[] cardIDArray) {
        int result = 0;
        for (int i = 0; i < power.length; i++) {
            result += power[i] * cardIDArray[i];
        }
        return refNumber[(result % 11)];
    }

    /**
     * 判断日期是否有效
     *
     * @param cardID
     * @return
     */
    public static boolean isValidDate(String cardID) {
        if (cardID == null) {
            return false;
        }
        String date = cardID.substring(6, 14);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (date.length() != dateFormat.toPattern().length()) {
//            System.out.println("日期不合法");
            return false;
        }
        //SimpleDateFormat的setLenient(true)
        //这种情况下java会把你输入的日期进行计算，比如55个月那么就是4年以后，这时候年份就会变成03年了
        //SimpleDateFormat的setLenient(false)
        //这种情况下java不会把你输入的日期进行计算，比如55个月那么就是不合法的日期了，直接异常
        dateFormat.setLenient(false);//严格的日期匹配,关闭日期自动计算,
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
//            System.out.println("日期不合法");
            return false;
        }
        return true;
    }

    /**
     * 校验手机号
     *
     * @param s
     * @return
     */
    public static boolean isValidMobileNumber(String s) {
        return Pattern.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", s);
    }

    /**
     * 是否存在空白行
     *
     * @param s
     * @return \n\r 返回true
     */
    public static boolean isBlankLine(String s) {
        return Pattern.matches("\\n\\s*\\r", s);
    }

    /**
     * 校验URL
     *
     * @param s
     * @return
     */
    public static boolean isValidURL(String s) {
        return Pattern.matches("[a-zA-z]+://[^\\s]*", s);
    }

    /**
     * 校验邮政编码
     *
     * @param s
     * @return
     */
    public static boolean isValidZipCode(String s) {
        return Pattern.matches("[1-9]\\d{5}(?!\\d)", s);
    }

}
