package com.lzk.toolboxes.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 自增編號生成器
 */
public class NumberUtil {

    /**
     * lastNumber为null时从当前年度开始自增，下一年度重新从初始值开始自增
     *
     * @param head          自定义头部        如：BZ
     * @param format        number尾部格式    如：000
     * @param startFromZero 是否从0开始
     * @param lastNumber    需要递增的number  如：BZ2021000
     * @return 如：BZ2021001
     */
    public static String addSelfByYear(String head, String format, boolean startFromZero, String lastNumber) {
        //定义number格式
        DecimalFormat decimalFormat = new DecimalFormat(format);
        //获取当前年度
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        Integer year = calendar.get(Calendar.YEAR);
        if (StringUtils.isBlank(lastNumber)) {
            if (startFromZero) {
                return head + year + decimalFormat.format(0);
            } else {
                return head + year + decimalFormat.format(1);
            }
        }
        if (!lastNumber.startsWith(head)) {
            System.out.println("number格式不匹配");
            return "";
        }
        //获取尾部数字
        String rear = lastNumber.replace(head, "");
        //获取年份
        String thisYear = rear.substring(0, 4);
        //获取年份后的number
        String number = rear.replace(thisYear, "");
        //判断年份是否相等
        if (thisYear.equals(year.toString())) {
            return head + thisYear + decimalFormat.format(Integer.parseInt(number) + 1);
        } else {
            thisYear = year.toString();
            if(startFromZero){
                return head + thisYear + decimalFormat.format(0);
            }else {
                return head + thisYear + decimalFormat.format(1);
            }
        }
    }

    /**
     * 无限自增
     *
     * @param head          自定义头部        如：BZ
     * @param format        number尾部格式    如：000
     * @param startFromZero 是否从0开始
     * @param lastNumber    需要递增的number  如：BZ000
     * @return 如：BZ001
     */
    public static String addSelf(String head, String format, boolean startFromZero, String lastNumber) {
        //定义number格式
        DecimalFormat decimalFormat = new DecimalFormat(format);
        if (StringUtils.isBlank(lastNumber)) {
            if (startFromZero) {
                return head + decimalFormat.format(0);
            } else {
                return head + decimalFormat.format(1);
            }
        }
        if (!lastNumber.startsWith(head)) {
            System.out.println("number格式不匹配");
            return "";
        }
        //获取尾部数字
        String rear = lastNumber.replace(head, "");
        return head + decimalFormat.format(Integer.parseInt(rear) + 1);
    }


    public static void main(String[] args) {
        String cv = NumberUtil.addSelf("cvvv", "0000", false, "cvvv99");
        String s = NumberUtil.addSelfByYear("GH", "0000", true, null);
        System.out.println(s);
    }


}
