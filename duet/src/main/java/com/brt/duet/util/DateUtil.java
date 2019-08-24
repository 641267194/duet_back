package com.brt.duet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 方杰
 * @date 2019年1月9日
 * @description 日期工具类
 */
public abstract class DateUtil {

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return Date
	 * @description 获取日期
	 */
	public static Date getDate() {
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		return new Date();
	}
	
	/**
	 * @author 方杰
	 * @date 2019年4月3日
	 * @param str yyyy-MM-dd HH:mm:ss格式的字符串
	 * @return Date
	 * @description yyyy-MM-dd HH:mm:ss字符串转换成Date类型
	 */
	public static Date dateTimeStrToDate(String str) {
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return long 当前毫秒
	 * @description 获取当前毫秒
	 */
	public static long getCurMilli() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(getDate());
		long millisecond = 0L;
		try {
			millisecond = sdf.parse(newDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return String 年月日时分秒毫秒
	 * @description 获取年月日时分秒毫秒
	 */
	public static String getYmdhmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		return sdf.format(getDate());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return String 年月日时分秒
	 * @description 获取年月日时分秒
	 */
	public static String getYmdhm() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(getDate());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return long 当前秒
	 * @description 获取当前秒
	 */
	public static long getCurSecond() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(getDate());
		long millisecond = 0L;
		try {
			millisecond = sdf.parse(newDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond / 1000;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param millisecond
	 * @return String yyyy-MM-dd HH:mm:ss
	 * @description 毫秒转日期
	 */
	public static String getDateTime(long millisecond) {
		System.setProperty("user.timezone","Asia/Shanghai");
		if (millisecond == 0L) {
			millisecond = System.currentTimeMillis();
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisecond);
		return dateformat.format(calendar.getTime());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return String yyyy-MM-dd
	 * @description 获取当前的日期
	 */
	public static String getNowDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(getDate());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return int
	 * @description 获取当前年份
	 */
	public static int getYear() {
		System.setProperty("user.timezone","Asia/Shanghai");
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return int
	 * @description 获取当前季度
	 */
	public static int getQuarter() {
		System.setProperty("user.timezone","Asia/Shanghai");
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int quarter = 0;
		if (month < 4) {
			quarter = 1;
		} else if (month < 7 && month > 3) {
			quarter = 2;
		} else if (month < 10 && month > 6) {
			quarter = 3;
		} else if (month > 9) {
			quarter = 4;
		}
		return quarter;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return String
	 * @description 获取年/月/日
	 */
	public static String getYmd() {
		System.setProperty("user.timezone","Asia/Shanghai");
		Calendar now = Calendar.getInstance();
		String ymd = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH) + "/";
		return ymd;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @return String
	 * @description 获取yyyy年MM月dd日
	 */
	public static String getYmd2() {
		System.setProperty("user.timezone","Asia/Shanghai");
		Calendar now = Calendar.getInstance();
		String ymd = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
		return ymd;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param date 日期字符串yyyy-MM-dd
	 * @return String yyyy-MM-dd
	 * @description 延后1天
	 */
	public static String getEndDate(String date) {
		System.setProperty("user.timezone","Asia/Shanghai");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTimeInMillis(sdf.parse(date).getTime() + 86400000L);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param date yyyy-MM-dd HH:mm:ss
	 * @param day 天数
	 * @return String yyyy-MM-dd HH:mm:ss
	 * @description 延后n天
	 */
	public static String getDelayed(String date, int day) {
		System.setProperty("user.timezone","Asia/Shanghai");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTimeInMillis(sdf.parse(date).getTime() + 86400000L * day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param date
	 * @return long 
	 * @description 日期(yyyy-MM-dd HH:mm:ss)转换成毫秒
	 */
	public static long getMillisecond(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long millisecond = 0L;
		try {
			millisecond = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond;
	}

	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param date yyyy-MM-dd
	 * @return long
	 * @description 日期(yyyy-MM-dd)转换成毫秒
	 */
	public static long getDateMillisecond(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long millisecond = 0L;
		try {
			millisecond = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月9日
	 * @param mss 毫秒
	 * @return String *days*hours*minutes*seconds
	 * @description 毫秒转换为 *days*hours*minutes*seconds
	 */
	public static String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
	    long seconds = (mss % (1000 * 60)) / 1000;
	    String str = "";
	    if (days > 0) {
	    	str += days+"天";
		}
	    if (hours > 0) {
	    	str += hours+"小时";
		}
	    if (minutes > 0) {
	    	str += minutes+"分钟";
		}
	    if (seconds > 0) {
	    	str += seconds+"秒";
		}
	    return str;  
	}  

	public static void main(String[] args) {
		
	}
}
