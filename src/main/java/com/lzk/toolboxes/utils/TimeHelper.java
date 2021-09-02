package com.lzk.toolboxes.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TimeHelper {

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String DD = "dd";
	public static final int ThisWeek = 0;
	public static final int NestWeek = 1;
	public static final int LastWeek = -1;

	/**
	 * 时间戳转换成日期格式字符串
	 *
	 * @param seconds 精确到秒的字符串
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	public static String timeStamp2Date(String seconds) {
		return timeStamp2Date(seconds, "yyyyMMdd");
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param date_str 字符串日期
	 * @param format   如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Long date2TimeStamp(String date_str, String format) {
		try {
			if (!StringUtils.isNotBlank(date_str)) {
				return 0L;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date_str).getTime() / 1000;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param date_str 字符串日期
	 * @return
	 */
	public static String date2TimeStamp(String date_str) {
		return String.valueOf(TimeHelper.date2TimeStamp(date_str, "yyyy-MM-dd"));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param date_str 字符串日期
	 * @param format   如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Long date2TimeStampMills(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date_str).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	/**
	 * 获取当前的年,月,日
	 */
	public static String getDateYYYYMMdd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		//String text = date.replace("-", "");
		return date;
	}

	public static String getDateYYYYMMddHHMMss(Long seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy年MM月dd日 HH时mm分ss秒");
		Date date = new Date(seconds);
		String formatTime = "(" + sdf.format(date) + ")";
		return formatTime;
	}

	/**
	 * 获取当前的年
	 */
	public static String getDateYYYY() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		return date;
	}


	/**
	 * 获取当前的年,月
	 */
	public static String getDateYYYYMM() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		String text = date.replace("-", "");
		return text;
	}

	/**
	 * 获取当前的月
	 */
	public static Integer getDateMM() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		String text = date.replace("-", "");
		return Integer.valueOf(text);
	}


	/**
	 * 获取当前的日
	 */
	public static String getDateDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		String text = date.replace("-", "");
		return text;
	}

	/**
	 * return hour,minute,second
	 *
	 * @return
	 */
	public static String[] GetCurentTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
		Date date = new Date(System.currentTimeMillis());
		String string = df.format(date);
		String[] datesss = string.split(":");
		// System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return datesss;
	}

	/**
	 * all current times
	 * <p>
	 * year-month-day hour-minute-second
	 *
	 * @return
	 */
	public static String[] GetAllCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sdf.format(curDate);
		String[] text = date.split(":");
		return text;
	}

	/**
	 * 获取系统的时间戳（10位） java中Date类中的getTime()是获取时间戳的，java中生成的时间戳精确到毫秒级别，
	 * 而unix中精确到秒级别，所以通过java生成的时间戳需要除以1000
	 *
	 * @return
	 */
	public static Long getCurrentTime10() {
		return Long.valueOf(System.currentTimeMillis() / 1000);
	}

	public static String getCurrentTime10Str() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}


	/**
	 * 获取周日-日期(20160424):
	 * <p>
	 * whichWeek=-1 上上周末 ;
	 * <p>
	 * whichWeek=0 上周末 ;
	 * <p>
	 * whichWeek=1 本周末 ;
	 *
	 * @return
	 */
	public static int getWeekSunday(String formatDate, Integer whichWeek) {
		if (whichWeek == null)
			whichWeek = 0;
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + whichWeek * 7);
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取系统的时间戳（13位）
	 *
	 * @return 13位的时间戳
	 */
	public static Long getCurrentTime13() {
		return System.currentTimeMillis();
	}

	/**
	 * 通过时间戳的差获取天数
	 *
	 * @param deltime
	 * @return
	 */
	public static int getDaysByTimeStamp(int deltime) {
		if (deltime > 24 * 60 * 60)
			return (int) (deltime / (24 * 60 * 60));
		return 0;
	}

	/**
	 * 获取当前时间20160316
	 *
	 * @param formatDate
	 * @return
	 */
	public static long getDateNow(String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		String first = format.format(calendar.getTime());
		return Long.valueOf(first);
	}

	/**
	 * 获取当前月份的日期
	 *
	 * @param formatDate
	 * @return
	 */
	public static int getDataOfTheMonth(String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取本月月的第一天days=1
	 *
	 * @param formatDate
	 * @param days       1 第一天，20 第20天
	 * @return
	 */
	public static int getDayOfTheMonth(String formatDate, int days) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, days);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取本月的最后一天
	 *
	 * @return
	 */
	public static int getLastDayOfTheMonth(String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取上个月的第一天days=1
	 *
	 * @param formatDate
	 * @param days       1 第一天 20 第20天
	 * @return
	 */
	public static int getDayOfTheLastMonth(String formatDate, int days) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);// 前一个月
		c.set(Calendar.DAY_OF_MONTH, days);// 月份的第一天1
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取上个月的最后一天
	 *
	 * @return
	 */
	public static int getLastDayOfTheLastMonth(String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);// 前一个月
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 获取上上个月的最后一天
	 *
	 * @return
	 */
	public static int getLastDayOfTheLastLastMonth(String formatDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -2);// 前2个月
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		String first = format.format(c.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 今天是本周的第几天: 7,1,2,3,4,5,6("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
	 *
	 * @return
	 */
	public static int getWeekDayOfWeek() {
		int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获取当周的星期一
	 *
	 * @return
	 */
	public static int getMondayOfWeekDay() {
		int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 求延迟后的时间=当前时间-延迟时间
	 *
	 * @param formatDate 时间格式
	 * @param delayDate  延迟的天数
	 * @return 延迟后的时间
	 */
	public static int getDelayDateDay(String formatDate, int delayDate) {
		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, delayDate);
		String currentTime = format.format(c.getTime());
		return Integer.valueOf(currentTime);
	}

	/**
	 * 返回今天日期的整形数字例：20160317
	 *
	 * @return
	 */
	public static int getIntToday() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return (Integer.valueOf(dateFormat.format(new Date())));
	}

	/**
	 * yyyy-MM-dd HH:mm:ss 转为 yyyyMMdd
	 *
	 * @param date
	 * @return
	 */
	public static int date2Int(String date) {
		if (StringUtils.isNotBlank(date)) {
			String a = date.split(" ")[0];
			String b = a.replaceAll("-", "");
			return Integer.valueOf(b);
		}
		return 0;
	}

	/**
	 * 返回本月日期的整形数字例：201603
	 *
	 * @return
	 */
	public static int getIntTodayMonth() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return (Integer.valueOf(dateFormat.format(new Date())));
	}

	/**
	 * 获得当天的下一天Timespan
	 *
	 * @return
	 */
	public static int nextDayTimeSpan() {
		// 获取下一天的timespan
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 获得当天加N天--时间戳
	 *
	 * @return
	 */
	public static int getAddDayTimeSpan(int addDays) {
		// 获取下一天的timespan
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, addDays);
		Date date = calendar.getTime();
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 获得当天加N天--年月日
	 *
	 * @return
	 */
	public static int getAddDayToday(int addDays) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, addDays);
		String first = format.format(calendar.getTime());
		return Integer.valueOf(first);
	}

	/**
	 * 通过时间戳获取时间20160316
	 *
	 * @param formatDate
	 * @return
	 */
	public static int getDataByTimeStamp(String formatDate, String TimeStamp) {
		Long timestamp = Long.parseLong(TimeStamp) * 1000;
		String date = new SimpleDateFormat(formatDate).format(new Date(timestamp));
		return Integer.valueOf(date);
	}

	/**
	 * 获取星期一的时间戳
	 *
	 * @param whichWeek 0代表本周 ; -1代表上一周
	 * @return
	 */
	public static int getMondayTimeSpan(int whichWeek) {
		// 获取下一天的timespan
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.add(Calendar.DAY_OF_WEEK, whichWeek * 7);
		Date date = calendar.getTime();
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 获取xxx天+多少天的时间戳
	 *
	 * @param whichDay 指定时间戳
	 * @param days     距离几天
	 * @return
	 */
	public static int getDateByTimeStamp(long whichDay, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(whichDay));
		calendar.add(Calendar.DAY_OF_YEAR, days);
		Date date = calendar.getTime();
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 天转秒
	 *
	 * @param day
	 * @return
	 */
	public static int day2Sec(int day) {
		return day * 24 * 60 * 60;
	}

	/**
	 * 时转秒
	 *
	 * @param hour
	 * @return
	 */
	public static int hour2Sec(int hour) {
		return hour * 60 * 60;
	}

	/**
	 * 分转秒
	 *
	 * @param minute
	 * @return
	 */
	public static int min2Sec(int minute) {
		return minute * 60;
	}

	/**
	 * 获取本分钟剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getThisMinuteLeaveMillSec(LocalDateTime now) {
		return getAddMinutesLeaveMillSec(now, 1L);
	}

	/**
	 * 获取本小时剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getThisHourLeaveMillSec(LocalDateTime now) {
		return getAddHoursLeaveMillSec(now, 1L);
	}

	/**
	 * 获取今天剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getTodayLeaveMillSec(LocalDateTime now) {
		return getAddDaysLeaveMillSec(now, 1L);
	}

	/**
	 * 获取本周剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getThisWeekLeaveMillSec(LocalDateTime now) {
		long addDays = 7 - now.getDayOfWeek().getValue() + 1L;
		return getAddDaysLeaveMillSec(now, addDays);
	}

	/**
	 * 获取本月剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getThisMonthLeaveMillSec(LocalDateTime now) {
		return getAddMonthsLeaveMillSec(now, 1L);
	}

	/**
	 * 获取本年剩余毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getThisYearLeaveMillSec(LocalDateTime now) {
		return getAddYearsLeaveMillSec(now, 1L);
	}

	/**
	 * 获取增加M分钟后剩余的毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getAddMinutesLeaveMillSec(LocalDateTime now, long minutes) {
		String strFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
		String d1 = now.format(dateTimeFormatter);
		String d2 = now.withSecond(0).withNano(0).plusMinutes(minutes).format(dateTimeFormatter);
		long timeSpan = TimeHelper.date2TimeStampMills(d2, strFormat) - TimeHelper.date2TimeStampMills(d1, strFormat);
		return timeSpan;
	}

	/**
	 * 获取增加H小时后剩余的毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getAddHoursLeaveMillSec(LocalDateTime now, long hours) {
		String strFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
		String d1 = now.format(dateTimeFormatter);
		String d2 = now.withMinute(0).withSecond(0).withNano(0).plusHours(hours).format(dateTimeFormatter);
		long timeSpan = TimeHelper.date2TimeStampMills(d2, strFormat) - TimeHelper.date2TimeStampMills(d1, strFormat);
		return timeSpan;
	}

	/**
	 * 获取增加D天后剩余的毫秒数
	 *
	 * @param days
	 * @return
	 */
	public static long getAddDaysLeaveMillSec(LocalDateTime now, long days) {
		String strFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
		String d1 = now.format(dateTimeFormatter);
		String d2 = now.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(days).format(dateTimeFormatter);
		long timeSpan = TimeHelper.date2TimeStampMills(d2, strFormat) - TimeHelper.date2TimeStampMills(d1, strFormat);
		return timeSpan;
	}

	/**
	 * 获取增加M月后剩余的毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getAddMonthsLeaveMillSec(LocalDateTime now, long months) {
		String strFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
		String d1 = now.format(dateTimeFormatter);
		String d2 = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).plusMonths(months).format(dateTimeFormatter);
		long timeSpan = TimeHelper.date2TimeStampMills(d2, strFormat) - TimeHelper.date2TimeStampMills(d1, strFormat);
		return timeSpan;
	}

	/**
	 * 获取增加Y年后剩余的毫秒数
	 *
	 * @param now
	 * @return
	 */
	public static long getAddYearsLeaveMillSec(LocalDateTime now, long years) {
		String strFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
		String d1 = now.format(dateTimeFormatter);
		String d2 = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).plusYears(years).format(dateTimeFormatter);
		long timeSpan = TimeHelper.date2TimeStampMills(d2, strFormat) - TimeHelper.date2TimeStampMills(d1, strFormat);
		return timeSpan;
	}

	/**
	 * 时间戳转字符串
	 *
	 * @param timeStamp 时间戳
	 * @param format    格式
	 * @return 转换后的字符串
	 */
	public static String getTimeFormatStr(Long timeStamp, String format) {
		if (timeStamp == null) {
			return "";
		}
		Instant instant = Instant.ofEpochMilli(timeStamp);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * 获取随机整数
	 *
	 * @param max 最大数
	 * @param min 最小数
	 * @return
	 */
	public static Integer getRandomNum(int max, int min) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}

	/**
	 * 获取随机整数
	 *
	 * @param max 最大数
	 * @return
	 */
	public static Integer getRandomNum(int max) {
		return getRandomNum(max, 0);
	}

	//获得当天0点时间
	public static Long getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	//获得当天24点时间
	public static Long getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTimeInMillis() / 1000;
	}

	//获得本周一0点时间
	public static Long getTimesWeekStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 1);
		return cal.getTimeInMillis() / 1000;
	}

	//获得本周日24点时间
	public static Long getTimesWeekEnd() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 7);
		return (cal.getTimeInMillis() + 24 * 60 * 60 * 1000) / 1000;
	}


	//获得本月第一天0点时间
	public static Long getTimesMonthStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTimeInMillis() / 1000;
	}

	//获得本月最后一天24点时间
	public static Long getTimesMonthEnd() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return (cal.getTimeInMillis() / 1000);
	}

	// 获得某天最大时间 2017-10-15 23:59:59
	public static Long getEndOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()).getTime();
	}

	// 获得某天最小时间 2017-10-15 00:00:00
	public static Long getStartOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()).getTime();
	}

	/**
	 * 计算时间差
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Map<String, Long> timeDifference(Date startTime, Date endTime) {
		long l = endTime.getTime() - startTime.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		Map<String, Long> result = new HashMap();
		result.put("d", day);
		result.put("h", hour);
		result.put("m", min);
		result.put("s", s);
		return result;
	}


	/**
	 * 计算时间差
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Map<String, Long> timeDifference(String startTime, String endTime) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = df.parse(startTime);
		Date end = df.parse(endTime);
		long l = end.getTime() - start.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		Map<String, Long> result = new HashMap();
		result.put("d", day);
		result.put("h", hour);
		result.put("m", min);
		result.put("s", s);
		return result;
	}

	/**
	 * 获取指定时间的一周内的所有日期
	 * @param date
	 * @return
	 */
	public static List<String> getWeekDays(Date date){
		List<String> weekDayList = new ArrayList<String>();
		//s1.设置时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//s2.设置要计算的时间
		cal.setTime(date);

		//s3.判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		//s4.获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		//s5.判断是不是一个星期的第一天
		if (1 == dayWeek) {
			//s6.如果是一个星期的第一天，则减去一天
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		//s7.设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		//s8.获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		//s9.根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		//s10.获取星期一
		String monday = sdf.format(cal.getTime());

		//s11.获取星期二
		cal.add(Calendar.DATE, 1);
		String tuesday = sdf.format(cal.getTime());

		//s12.获取星期三
		cal.add(Calendar.DATE, 1);
		String wednesday = sdf.format(cal.getTime());

		//s13.获取星期四
		cal.add(Calendar.DATE, 1);
		String thursday = sdf.format(cal.getTime());

		//s14.获取星期五
		cal.add(Calendar.DATE, 1);
		String friday = sdf.format(cal.getTime());

		//s15.获取星期六
		cal.add(Calendar.DATE, 1);
		String saturday = sdf.format(cal.getTime());

		//s16.获取星期日
		cal.add(Calendar.DATE, 1);
		String sunday = sdf.format(cal.getTime());
		weekDayList.add(monday);
		weekDayList.add(tuesday);
		weekDayList.add(wednesday);
		weekDayList.add(thursday);
		weekDayList.add(friday);
		weekDayList.add(saturday);
		weekDayList.add(sunday);
		return weekDayList;
	}


	public static void main(String[] args) {
		System.out.println(getDateYYYYMMdd());
	}

}
