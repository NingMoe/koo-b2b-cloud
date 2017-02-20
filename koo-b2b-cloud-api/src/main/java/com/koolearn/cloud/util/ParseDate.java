package com.koolearn.cloud.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.*;
import java.util.*;

public class ParseDate {
	public static final int DATE_FORMAT_YYYYMMDD_HHMMSS = 1;
	public static final int DATE_FORMAT_YYYYMMDD = 2;
	public static final int DATE_FORMAT_HHMMSS = 3;
	public static final int DATE_FORMAT_MMDD = 4;
	public static final int DATE_FORMAT_YYYYMMDD_HHMM = 6;
	public static final int DATE_FORMAT_YYYYYEARMMMONTH = 7;
	public static final int DATE_FORMAT_MMDDCN = 8;
	public static final int DATE_FORMAT_YYYYMM = 9;
	public static final int DATE_FORMAT_YYYYMMDDHHMM_SLASHE = 10;// 斜杠/分割
	public static final int DATE_FORMAT_YYYYMMDD_SLASHE = 11;// 斜杠/分割
	public static final int DATE_FORMAT_YYYYMMDDHHMMSS_SLASHE=12;
	public static final int DATE_FORMAT_YYYYMMDDHHMMSS=13;
	public static final int DATE_FORMAT_YYYYMMDD_POINT = 21;
	public static final int DATE_FORMAT_YYYYMMDD_NO = 22;
	public static final int DATE_FORMAT_YYYYMMDDHH = 23;
	public static final int DATE_FORMAT_HHMM = 31;
	/** 空格 */
	public static final String DELIMITER_ONE_SPACE = " ";
	/** 短杠- */
	public static final String DELIMITER_ONE_LINE = "-";
	/** 下划线_ */
	public static final String DELIMITER_ONE_UNDERLINE = "_";
	public static final String DELIMITER_ONE_SLASH = "/";
	/** 逗号, */
	public static final String DELIMITER_ONE_COMMA = ",";
	/**句号.*/
	public static final String DELIMITER_ONE_PERIOD = ".";

	/** 用HS替换掉时间中的冒号 **/
	public static final String REPLACE_COLON_OF_TIME = "HS";

	static Logger logger = Logger.getLogger(ParseDate.class);

	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 根据显示格式，返回相应日期格式
	 * 
	 * @param date
	 * @param type
	 * @return
	 */
	public static String formatByString(String date, int type) {
		Date d = parse(date);
		String newdate = formatByDate(d, type);
		return newdate;
	}

	public static String formatByDate(Date d, int type) {
		if (d == null) {
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		String newdate = null;
		switch (type) {
		case DATE_FORMAT_YYYYMMDD_HHMMSS:
			newdate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(d);
			break;
		case DATE_FORMAT_YYYYMMDD:// 年月日
			newdate = new SimpleDateFormat("yyyy-MM-dd").format(d);
			break;
		case DATE_FORMAT_YYYYMMDD_POINT:// 年月日
			newdate = new SimpleDateFormat("yyyy.MM.dd").format(d);
			break;
        case DATE_FORMAT_YYYYMMDD_NO:// 年月日
            newdate = new SimpleDateFormat("yyyyMMdd").format(d);
            break;
        case DATE_FORMAT_YYYYMMDDHH:// 年月日时
            newdate = new SimpleDateFormat("yyyyMMddHH").format(d);
            break;
		case DATE_FORMAT_YYYYMM:// 年月日
			newdate = new SimpleDateFormat("yyyy-MM").format(d);
			break;
		case DATE_FORMAT_HHMMSS:// 小时数
			newdate = new SimpleDateFormat("HH:mm:ss").format(d);
			break;
		case DATE_FORMAT_HHMM:// 小时数
			newdate = new SimpleDateFormat("HH:mm").format(d);
			break;
		case 5:// 当天取小时，非当天取日期
			Calendar today = Calendar.getInstance();
			if ((c.get(Calendar.YEAR) == today.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == today.get(Calendar.MONTH) && c
					.get(Calendar.DAY_OF_MONTH) == today
					.get(Calendar.DAY_OF_MONTH))
					|| c.get(Calendar.YEAR) == 1970) {
				newdate = new SimpleDateFormat("HH:mm").format(d);
				if (newdate.equals("00:00")) {
					newdate = new SimpleDateFormat("MM-dd").format(d);
					// newdate = "";
				}
			} else {
				newdate = new SimpleDateFormat("MM-dd").format(d);
			}
			break;
		case DATE_FORMAT_YYYYMMDD_HHMM:
			newdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
			break;
		case DATE_FORMAT_YYYYYEARMMMONTH:
			newdate = new SimpleDateFormat("yyyy年MM月").format(d);
			break;
		case DATE_FORMAT_MMDD:// 仅取日期
			newdate = new SimpleDateFormat("MM-dd").format(d);
			break;
		case DATE_FORMAT_MMDDCN:
			newdate = new SimpleDateFormat("MM月dd日").format(d);
			break;
		case DATE_FORMAT_YYYYMMDDHHMM_SLASHE:
			newdate = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(d);
			break;
		case DATE_FORMAT_YYYYMMDD_SLASHE:
			newdate = new SimpleDateFormat("yyyy/MM/dd").format(d);
			break;
		case DATE_FORMAT_YYYYMMDDHHMMSS_SLASHE:
			newdate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d);
			break;
        case DATE_FORMAT_YYYYMMDDHHMMSS:
            newdate = new SimpleDateFormat("yyyyMMddHHmmss").format(d);
            break;
		default:
			newdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
			break;
		}
		return newdate;
	}

	public static Integer getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(c.YEAR);
	}

	public static Integer getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (c.get(c.MONTH) + 1); // 0 表示1月
	}

	public static Integer getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(c.DATE);
	}

	/**
	 * 根据模板，解析传入的字符为日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date parse(String date) {
		Date d = null;
		if (StringUtils.isBlank(date)) {
			return null;
		}

		ParsePosition pos = new ParsePosition(0);
		SimpleDateFormat s = new SimpleDateFormat("dd MM yyyy HH:mm:ss",
				new DateFormatSymbols(Locale.US));
		for (int i = 0; i < temps.length; i++) {
			s.applyPattern(temps[i]);
			d = s.parse(date, pos);
			if (pos.getIndex() == 0) { // 2012年11月16日d人fas ：能匹配29， index值为11
										// 2012-11-1rt6ss :能匹配28
										// ，index值是9，d=2012-11-1 ！！
				continue;
			}
			// System.out.println(i);
			break;
		}
		return d;
	}

	public static boolean isNumber(String number) {

		if (StringUtils.isBlank(number)) {
			return true;
		}
		String regStr = "^[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)([eE][+-]?[0-9]+)?$";
		boolean isNum = number.matches(regStr);
		if (isNum) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean dateFormatError(String date) {
		Date d = null;
		if (StringUtils.isBlank(date)) {
			return false;
		}
		ParsePosition pos = new ParsePosition(0);
		SimpleDateFormat s = new SimpleDateFormat("dd MM yyyy HH:mm:ss",
				new DateFormatSymbols(Locale.US));
		for (int i = 0; i < temps.length; i++) {
			s.applyPattern(temps[i]);
			d = s.parse(date, pos);
			if (pos.getIndex() == 0) {
				continue;
			}
			break;
		}
		if (d == null) {
			return true;
		} else {
			// System.out.println(s.format(d));
			return false;
		}

	}

	public static Date addYear(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.YEAR, num);
		return c.getTime();
	}

	/**
	 * 日期模板
	 */
	private static String[] temps = new String[39];
	static {
		/**
		 * 日期字符串前面匹配模版，就能解析 2012年11月16日d人fas ：能匹配29， index值为11 2012-11-1rt6ss
		 * :能匹配28 ，index值是9，d=2012-11-1 ！！ pos.getIndex()：最后一个被解析的字符的索引
		 */
		temps[0] = "yyyy-MM-dd HH:mm:ss";// 新华网rss:27 Sep 2006 14:38:00
											// GMT+08:00
		temps[1] = "yy/MM/dd HH:mm";// 新华网rss:27 Sep 2006 14:38:00 +0800
		temps[2] = "yyyy年MM月dd日 HH:mm:ss";// Y年M月D日BH:F:S
		temps[3] = "yyyy年MM月dd日 HH:mm:ss";// Y年M月D日H:F:S
		temps[4] = "yyyy-MM-dd HH:mm:ss";// Y-M-DBH:F:S
		temps[5] = "yyyy/MM/dd HH:mm:ss";// Y/M/DBH:F:S
		temps[6] = "yyyy.MM.dd HH:mm:ss";// Y.M.DBH:F:S
		temps[7] = "yyyy年MM月dd日 HH时mm分ss秒";// Y年M月D日BH时F分S秒
		temps[8] = "yyyy年MM月dd日 HH时mm分ss秒";// Y年M月D日H时F分S秒
		temps[9] = "yyyy年MM月dd日 HH点mm分ss秒";// Y年M月D日BH点F分S秒

		temps[10] = "yyyy年MM月dd日 HH点mm分ss秒";// Y年M月D日H点F分S秒
		temps[11] = "dd MMM yyyy HH:mm:ss zzz";// Y-M-DBABH:F:S
		temps[12] = "MM/dd/yyyy HH:mm:ss";// M/D/YBH:F:S
		temps[13] = "dd MMM yyyy HH:mm:ss";// baidu的rss时间 27 Sep 2006 14:38:00
		temps[14] = "yyyy年MM月dd日 HH:mm";// Y年M月D日BH:F
		temps[15] = "yyyy年MM月dd日 HH：mm";// Y年M月D日BH：F
		temps[16] = "yyyy年MM月dd日 HH:mm";// Y年M月D日H:F", //新浪，搜狐，人民网，QQ
		temps[17] = "yyyy年MM月dd日 HH时mm分ss秒";// Y年M月D日BH时F分S秒
		temps[18] = "yyyy年MM月dd日 HH时mm分";// Y年M月D日BH时F分", //TOM
		temps[19] = "yyyy年MM月dd日HH时mm分";// Y年M月D日H时F分",

		temps[20] = "yyyy年MM月dd日 HH点mm分";// Y年M月D日BH点F分
		temps[21] = "yyyy年MM月dd日HH点mm分";// Y年M月D日H点F分
		temps[22] = "yyyy.MM.dd HH:mm";// Y.M.DBH:F"
		temps[23] = "yyyy/MM/dd HH:mm";// "Y/M/DBH:F//北青网Thu, 03 Apr 2008
										// 03:02:05 +0000
		temps[24] = "yyyy-MM-dd HH:mm";// Y-M-DBH:F"
		temps[25] = "MM/dd HH:mm:ss";// M/DBH:F:S
		temps[26] = "MM-dd HH:mm";// yyyymmddBH:F", //西部在线
		temps[27] = "MM/dd/yyyy/HH:mm";// "M/D/Y/H:F", //年份在中间，必须放到五字段模板的最后
		temps[28] = "yyyy-MM-dd";// Y-M-D",
		temps[29] = "yyyy年MM月dd日";// Y年M月D日",

		temps[30] = "yyyy/MM/dd";// Y/M/D
		temps[31] = "yyyy.MM.dd";// Y.M.D
		temps[32] = "dd-MM-yyyy";// D-M-Y
		temps[33] = "EEE, dd MMM yyyy HH:mm:ss Z";// Thu, 03 Apr 2008 03:02:05
													// +0000
		temps[34] = "yyyyMMdd HH:mm";// yyyymmddBH:F", //西部在线
		temps[36] = "EEE, dd MMM yyyy HH:mm:ss";// 奇虎 rss:Thu, 29 May 2008
												// 09:08:35 +8000
		temps[35] = "HH:mm";// 贴吧微件的日期
		temps[37] = "dd MMM yyyy HH:mm:ss Z";// 新华网rss:27 Sep 2006 14:38:00
												// +0800
		temps[38] = "EEE,dd MMM yyyy HH:mm:ss z";// 新华网rss:27 Sep 2006 14:38:00
													// +0800
	}

	public static double string2Double(String data) {
		if (StringUtils.isBlank(data)) {
			data = "0";
		}

		return Double.parseDouble(data);
	}

	/**
	 * 去掉字符串前导 0
	 * 
	 * @return
	 */
	public static String remove0leadString(String s) {
		return s.replaceAll("^0+(?!$)", "");
	}

	public static int getRandom() {
		double a = Math.random() * 2123123;
		a = Math.ceil(a);
		return new Double(a).intValue();
	}

	/**
	 * 根据日期获取星期
	 * 
	 * @param s
	 * @return
	 */
	public static String getWeek(String s) {
		final String dayNames[] = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Calendar calendar = Calendar.getInstance();
		Date date = new Date();

		try {
			date = sdfInput.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	public static String getWeek(Date date) {
		return getWeek(formatByDate(date, DATE_FORMAT_YYYYMMDD_HHMM));
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public static Date getSysDate() {
		return new Date();
	}

	/**
	 * 日期 按格式 输出成字符串
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String getFormatString(Date date, String fmt) {
		if (date == null) {
			date = new Date();
		}
		if (fmt == null || fmt.trim().equals("")) {
			fmt = "yyyy-MM-dd HH:mm:ss";
		}

		DateFormat dateFormat = new SimpleDateFormat(fmt);
		return dateFormat.format(date);
	}

	/**
	 * * @Description: TODO(根据当前日期得到周几)
	 * 
	 * @param dt
	 *            日期
	 * @return
	 * @return String 星期日
	 * @author: 葛海松
	 * @time: 2014年12月23日 下午3:26:58
	 * @throws
	 */
	public static String getWeekOfDateCN(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String getWeekOfDateCN(String dt) {
		return getWeekOfDateCN(parse(dt));
	}

	public static String getTime(Date dt, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(dt);
	}

	/**
	 * * @Description: TODO(获取当月最大天数)
	 * 
	 * @return
	 * @return int
	 * @author: 葛海松
	 * @time: 2014年12月15日 下午8:11:48
	 * @throws
	 */
	public static int getActualMaximum(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(calendar.DAY_OF_MONTH);
	}

	public static int getActualMaximum(String date) {
		return getActualMaximum(parse(date));
	}

	/**
	 * * @Description: TODO(获取每月的第一天)
	 * 
	 * @param date
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月15日 下午8:21:27
	 * @throws
	 */
	public static String getFistDateOfMonth(String date) {
		Date d = parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return formatByDate(calendar.getTime(), DATE_FORMAT_YYYYMMDD_HHMMSS);

	}

	/**
	 * * @Description: TODO(获取每月的最后一天)
	 * 
	 * @param date
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月15日 下午8:21:27
	 * @throws
	 */
	public static String getLastDateOfMonth(String date) {
		Date d = parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(calendar.DAY_OF_MONTH));
		return formatByDate(calendar.getTime(), DATE_FORMAT_YYYYMMDD_HHMMSS);

	}

	/**
	 * 获取某年某周的起始时间和结束时间
	 * 
	 * @return
	 */
	public static String[] getBeginEndDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		Date dateBegin = addDate(calendar.getTime(), 1 - dayOfWeek);
		calendar.setTime(dateBegin); // 得到本周的第一天
		String begin = formatByDate(calendar.getTime(), DATE_FORMAT_YYYYMMDD);

		Date dateEnd = addDate(calendar.getTime(), 6);
		calendar.setTime(dateEnd); // 得到本周的最后一天
		String end = formatByDate(calendar.getTime(), DATE_FORMAT_YYYYMMDD);

		String[] range = new String[2];
		range[0] = begin;
		range[1] = end;
		return range;
	}

	public static String[] getBeginEndDayOfWeek(String date) {
		return getBeginEndDayOfWeek(parse(date));
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @return
	 */
	public static long getQuot(Date date) {
		long quot = 0;
		quot = date.getTime() - ParseDate.getSysDate().getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return quot;
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 * @return
	 */
	public static long getQuot(Date date, Date date2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		long quot = 0;
		quot = date2.getTime() - date.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		if (quot == 0
				&& calendar.get(Calendar.DAY_OF_MONTH) != calendar2
						.get(Calendar.DAY_OF_MONTH)) {
			quot = 1;
		}
		return quot;
	}

	public static long getQuot(String date, String date2) {
		return getQuot(parse(date), parse(date2));
	}

	/**
	 * * @Description: TODO(获取两个时间的时间查 如1天2小时30分钟)
	 * 
	 * @param endDate
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2015年3月19日 下午4:58:25
	 * @throws
	 */
	public static String getDatePoor(Date beginDate, Date endDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	public static String getDatePoor(String beginDate, String endDate) {
		return getDatePoor(parse(beginDate), parse(endDate));
	}

	/**
	 * * @Description: TODO(获取分钟时间差)
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return long
	 * @author: 葛海松
	 * @time: 2015年3月19日 下午5:08:19
	 * @throws
	 */
	public static long getDatePoorMinute(Date beginDate, Date endDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		// 计算差多少分钟
		long min = diff / nm;
		return min;
	}

	public static long getDatePoorMinute(String beginDate, String endDate) {
		return getDatePoorMinute(parse(beginDate), parse(endDate));
	}

	/** 日期加减天数 **/
	public static Date addDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ days);
		return calendar.getTime();
	}

	/** 日期加减天数 **/
	public static Date addDate(String date, int days) {
		return addDate(parse(date), days);
	}

	/**
	 * * @Description: TODO(对分钟的加减)
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 * @return Date
	 * @author: 葛海松
	 * @time: 2014年12月21日 下午3:34:10
	 * @throws
	 */
	public static Date addMinute(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutes);
		return calendar.getTime();
	}

	public static Date addMinute(String date, int minutes) {
		return addMinute(parse(date), minutes);
	}
	 /**
	  * * @Description: TODO(秒加减) 
	    *  @param date
	    *  @return
	    * @return Date    
	    * @author: 葛海松
	    * @time:    2015年5月4日 下午3:26:06 
	    * @throws
	  */
	public static Date addSecond(Date date, int sec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + sec);
		return calendar.getTime();
	}
	public static Date addSecond(String date, int sec) {
		return addSecond(parse(date), sec);
	}
	/**
	 * * @Description: TODO(返回指定日期的日)
	 * 
	 * @param date
	 * @return
	 * @return int
	 * @author: 葛海松
	 * @time: 2014年12月15日 下午9:03:58
	 * @throws
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfMonth(String date) {
		return getDayOfMonth(parse(date));
	}

	/**
	 * * @Description: TODO(根据日期字符串获取周几)
	 * 
	 *            2014-12-15
	 * @return
	 * @return String {"7", "1", "2", "3", "4", "5", "6"}
	 * @author: 葛海松
	 * @time: 2014年12月14日 下午4:20:00
	 * @throws
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static String getWeekOfDate(String date) {
		return getWeekOfDate(parse(date));
	}

	/**
	 * * @Description: TODO(根据日期字符串获取周几)
	 * 
	 *            2014-12-15
	 * @return
	 * @return Integer 周一返回 1 周日返回0
	 * @author: 葛海松
	 * @time: 2014年12月14日 下午4:20:00
	 * @throws
	 */
	public static Integer getWeekIndexOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return w;
	}

	public static Integer getWeekIndexOfDate(String date) {
		return getWeekIndexOfDate(parse(date));
	}

	public static int dayOfWeek(Date date) {
		int dayOfWeek = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		switch (weekDay) {
		case 1:
			dayOfWeek = 7;
			break;
		case 2:
			dayOfWeek = 1;
			break;
		case 3:
			dayOfWeek = 2;
			break;
		case 4:
			dayOfWeek = 3;
			break;
		case 5:
			dayOfWeek = 4;
			break;
		case 6:
			dayOfWeek = 5;
			break;
		case 7:
			dayOfWeek = 6;
			break;
		}
		return dayOfWeek;
	}

	/**
	 * * @Description: TODO(月份增减)
	 * 
	 * @param date
	 * @return
	 * @return Date
	 * @author: 葛海松
	 * @time: 2014年12月16日 下午4:27:50
	 * @throws　此方法会把把日期设置为15，防止月份加减失败 如：2014-3-29　　月份减一：2014-03-01
	 */
	public static Date aodMonth(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 15);//
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
		return calendar.getTime();
	}

	public static Date aodMonth(String date, int months) {

		return aodMonth(parse(date), months);
	}

	/**
	 * * @Description: TODO(周份增减)
	 * 
	 * @param date
	 * @return
	 * @return Date
	 * @author: 葛海松
	 * @time: 2014年12月16日 下午4:27:50
	 * @throws　
	 */
	public static Date aodWeek(Date date, int weeks) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.WEEK_OF_MONTH,
				calendar.get(Calendar.WEEK_OF_MONTH) + weeks);
		return calendar.getTime();
	}

	public static Date aodWeek(String date, int weeks) {
		return aodWeek(parse(date), weeks);
	}

	/**
	 * * @Description: TODO(获得日期)
	 * 
	 * @return yyyy-MM-dd 00:00:00
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月17日 下午9:16:10
	 * @throws
	 */
	public static String getDate(Date date) {
		String d = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return d + " 00:00:00";
	}

	public static String getDate(String date) {
		return getDate(parse(date));
	}

	/**
	 * * @Description: TODO(获取当前日期所在周的开始和结束日期)
	 * 
	 * @param date
	 * @return
	 * @return String 2014-2-29_2014-2-29
	 * @author: 葛海松
	 * @time: 2014年12月19日 下午2:59:16
	 * @throws
	 */
	public static String getStartEndTimeOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 获取当月最大天数
		int maxDays = ParseDate.getActualMaximum(date);
		// 获得日
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// 获得周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		weekDay = weekDay - 1;// 处理周日为0
		// 获得本月第几周
		int weekDayInMonth = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		int startDay = day - weekDay;// 本周开始日
		int endDay = day + 6 - weekDay;// 本周结束日
		if (startDay <= 0) {
			startDay = 1;
		}
		if (endDay >= maxDays) {
			endDay = maxDays;
		}
		calendar.set(Calendar.DAY_OF_MONTH, startDay);
		Date startTime = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, endDay);
		Date endTime = calendar.getTime();
		return formatByDate(startTime, DATE_FORMAT_YYYYMMDD) + "_"
				+ formatByDate(endTime, DATE_FORMAT_YYYYMMDD);
	}

	public static String getStartEndTimeOfWeek(String date) {
		return getStartEndTimeOfWeek(parse(date));
	}

	/**
	 * * @Description: TODO(获取时间范围内的所以日期集合(包含开始结束日期))
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @return List<String>
	 * @author: 葛海松
	 * @time: 2014年12月21日 下午3:06:58
	 * @throws
	 */
	public static List<String> getdateListOfWeek(String beginTime,
			String endTime) {
		List<String> dateList = new ArrayList<String>();
		Calendar calendarBegin = Calendar.getInstance();
		calendarBegin.setTime(parse(beginTime));
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(parse(endTime));
		long end = getQuot(beginTime, endTime);
		for (int i = 0; i <= end; i++) {
			Date date = addDate(calendarBegin.getTime(), i);
			dateList.add(formatByDate(date, DATE_FORMAT_YYYYMMDD));
		}
		// System.out.println(ArrayUtils.toString(dateList.toArray()));
		return dateList;
	}

	/**
	 * * @Description: TODO(获取辅导服务一天的时间端)
	 * 
	 * @param date
	 *            计算的日期 空当前日期
	 * @param startTime
	 *            开始时间点 默认8:00
	 * @param endTime
	 *            结束时间点 默认22：00
	 * @param useMinute
	 *            服务时长（分钟）
	 * @param restMinute
	 *            休息间隔（只有 0 或 15 分钟）
	 * @return
	 * @return List<String> 09:30-10:00 10:15-10:45
	 * @author: 葛海松
	 * @time: 2014年12月21日 下午3:46:35
	 * @throws
	 */
	public static List<String> getFreeTimeRangeOfDay(String date,
			String startTime, String endTime, int useMinute, int restMinute) {
		List<String> freeTimeRangeList = new ArrayList<String>();
		if (StringUtils.isNotBlank(date)) {
			date = formatByString(date, DATE_FORMAT_YYYYMMDD);
		} else {
			date = formatByDate(new Date(), DATE_FORMAT_YYYYMMDD);
		}
		if (StringUtils.isBlank(startTime)) {
			startTime = "8:00";
		}
		if (StringUtils.isBlank(endTime)) {
			endTime = "22:00";
		}
		if (restMinute != 15) {
			restMinute = 0;
		}

		String startT = date + DELIMITER_ONE_SPACE + startTime;
		long endT = parse(date + DELIMITER_ONE_SPACE + endTime).getTime();

		boolean falg = true;
		String sb1Date = startT;
		String sb2Date = "";
		while (falg) {

			String sb1 = formatByString(sb1Date, DATE_FORMAT_HHMM);
			Date tempDate = addMinute(sb1Date, useMinute);
			String sb2 = formatByDate(tempDate, DATE_FORMAT_HHMM);
			tempDate = addMinute(tempDate, restMinute);
			freeTimeRangeList.add(DELIMITER_ONE_SPACE + sb1
					+ DELIMITER_ONE_LINE + sb2);
			// System.out.println(" "+sb1+"-"+sb2);
			// 设置下次服务开始的时间段
			sb1Date = formatByDate(tempDate, DATE_FORMAT_YYYYMMDD_HHMM);
			// 判断是否继续循环
			Date flagDate = addMinute(tempDate, useMinute);
			long flagDateTime = flagDate.getTime();
			if (flagDateTime > endT) {
				// 服务时间超过最后时间 则终止
				falg = false;
			}
		}
		return freeTimeRangeList;
	}

	public static List<String> getFreeTimeRangeOfDay(int useMinute,
			int restMinute) {
		return getFreeTimeRangeOfDay(null, null, null, useMinute, restMinute);
	}

	/**
	 * * @Description: TODO(生成日期范围)
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2015年3月20日 上午11:33:09
	 * @throws
	 */
	public static String getValidityDate(Date startTime, Date endTime) {
		return formatByDate(startTime, DATE_FORMAT_YYYYMMDD_SLASHE)
				+ DELIMITER_ONE_LINE
				+ formatByDate(endTime, DATE_FORMAT_YYYYMMDD_SLASHE);
	}

	/************************ 计算时间段相关 begin ********************************/
	/**
	 * * @Description: TODO(根据粗略数字时间点计算详细时间点 数据库里保存的是该方法处理后的时间点)
	 * 
	 * @param rangeTimeNum
	 *            41,44 (连续多个时间段范围)
	 * @return String 41,42,43,44 （连续时间段保存结果） 特例：数组范围是同一个数： 21:45,22:00 88,88
	 *         返回88 保存在库里
	 * @author: 葛海松
	 * @time: 2014年12月24日 上午11:45:48
	 * @throws
	 */
	public static String getContinueTimeNumByRangeTimeNum(String rangeTimeNum) {
		if (null == rangeTimeNum || "".equals(rangeTimeNum)) {
			return "";
		}
		String b[] = null;
		if (rangeTimeNum.indexOf(DELIMITER_ONE_COMMA) != -1) {
			b = rangeTimeNum.split(DELIMITER_ONE_COMMA);
			int k = Integer.parseInt(b[0]);
			int f = Integer.parseInt(b[1]);
			String strTimes = "";
			for (int i = k; i <= f; i++) {
				int value = k++;
				strTimes += value + DELIMITER_ONE_COMMA;
			}
			rangeTimeNum = strTimes.substring(0, strTimes.length() - 1);
		}
		return rangeTimeNum;
	}

	/**
	 * * @Description: TODO(根据时间得到数字,时间限定在 8:00到 22:00之间)
	 * 
	 * @param startdate
	 *            9:15
	 * @param enddate
	 *            10:00
	 * @return 38,40 or 88,88
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月14日 下午4:33:36
	 * @throws
	 */
	public static String getNumTime(String startdate, String enddate) {
		if ("".equals(startdate) || "".equals(enddate)) {
			return "0";
		}
		if (startdate.indexOf(":") == -1 || enddate.indexOf(":") == -1) {
			return "0";
		}
		int startd = Integer.parseInt(startdate.replace(":", "").toString());
		int endd = Integer.parseInt(enddate.replace(":", "").toString());
		if (startd < 800 || endd > 2200) {
			return "请输入正确的时间点";
		}
		int start = (Integer.parseInt(startdate.split(":")[0])) * 4;
		String a5 = startdate.split(":")[1];
		if (a5.equals("15")) {
			start = start + 2;
		} else if (a5.equals("30")) {
			start = start + 3;
		} else if (a5.equals("45")) {
			start = start + 4;
		} else if (a5.equals("00")) {
			start = start + 1;
		}
		int a4 = Integer.parseInt(enddate.split(":")[0]);
		String a2 = enddate.split(":")[1];
		// 结束数字
		int end = 0;
		if (a2.equals("15")) {
			end = a4 * 4 + 1;
		} else if (a2.equals("30")) {
			end = a4 * 4 + 2;
		} else if (a2.equals("45")) {
			end = a4 * 4 + 3;
		} else if (a2.equals("00")) {
			end = a4 * 4;
		}
		return start + DELIMITER_ONE_COMMA + end;
	}

	/**
	 * * @Description: TODO(根据数字得到时间点)
	 * 
	 * @param a
	 *            38
	 * @param b
	 *            40
	 * @return String 9:15,10:00
	 * @author: 葛海松
	 * @time: 2014年12月14日 下午4:33:15
	 * @throws
	 */
	public static String getTimeByNum(int a, int b) {
		return getTimeByNum(a, b, DELIMITER_ONE_LINE);
	}

	public static String getTimeByNum(int a, int b, String connStr) {
		if (a == 0 || b == 0) {
			return "0";
		}
		int c = a / 4;// 得到小时
		int d = a % 4;// 得到余数
		if (d == 0) {
			c--;
		}
		StringBuffer startTime = new StringBuffer();
		startTime.append(c + ":");
		if (d == 1) {
			startTime.append("00");
		} else if (d == 2) {
			startTime.append("15");
		} else if (d == 3) {
			startTime.append("30");
		} else if (d == 0) {
			startTime.append("45");
		}
		int c1 = b / 4;// 得到小时
		int d1 = b % 4;// 得到余数
		StringBuffer endTime = new StringBuffer();
		endTime.append(c1 + ":");
		if (d1 == 1) {
			endTime.append("15");
		} else if (d1 == 2) {
			endTime.append("30");
		} else if (d1 == 3) {
			endTime.append("45");
		} else if (d1 == 0) {
			endTime.append("00");
		}
		return startTime + connStr + endTime;

	}

	/**
	 * * @Description: TODO(根据数字得到时间点)
	 * 
	 * @param timeNumStr
	 *            38,40
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月16日 下午12:28:09
	 * @throws
	 */
	public static String getTimeByNum(String timeNumStr) {
		if (StringUtils.isNotBlank(timeNumStr)) {
			String[] strarr = timeNumStr.split(",");
			if (strarr.length < 2) {
				String[] str = new String[2];
				str[0] = strarr[0];
				str[1] = strarr[0];
				strarr = str;
			}
			return getTimeByNum(Integer.parseInt(strarr[0]),
					Integer.parseInt(strarr[strarr.length - 1]));
		}
		return "";
	}

	/**
	 * * @Description: TODO(过滤集合重复数据)
	 * 
	 * @param oneString
	 *            37,38,38,38,39,39,40,41,42,43,43,44,
	 * @param towString
	 *            38,39,39,40,40,41,42,43,
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月14日 下午4:25:59
	 * @throws
	 */
	public static String listToString(String oneString, String towString) {
		if ("".equals(towString)  || towString.length() == 0 || null == towString) {
			towString = ",";
		}
		// 处理过滤数据
		String[] strArray = towString.split(",");
		for (String str : strArray) {
			if (oneString.indexOf(str + ",") != -1) {
				oneString = oneString.substring(0,
						(oneString.indexOf(str + ",")))
						+ oneString.substring(
								(oneString.indexOf(str + ",") + (str + ",")
										.length()), oneString.length());
			}
		}

		// 去掉重复的数据并排序
		Set<String> set = new TreeSet<String>();// 可预约
		String[] strs = oneString.split(",");
		for (String str : strs) {
			set.add(str);
		}
		StringBuffer sb = new StringBuffer();
		for (String s : set) {
			sb.append(s).append(",");
		}
		String strTime = sb.toString();
		strTime = strTime.substring(0, strTime.lastIndexOf(","));

		return strTime;
	}

	/**
	 * * @Description: TODO(去掉时间包含的： 已作为可用的页面id)
	 * 
	 * @param time
	 *            10:15-10:45
	 * @return
	 * @return String
	 * @author: 葛海松
	 * @time: 2014年12月29日 下午8:45:15
	 * @throws
	 */
	public static String parseTime(String time) {
		if (StringUtils.isNotBlank(time)) {
			return time.replace(":", REPLACE_COLON_OF_TIME);
		}
		return "";
	}

	/*** 把时间中的HS还原成冒号 */
	public static String reduParseTime(String time) {
		if (StringUtils.isNotBlank(time)) {
			return time.replace(REPLACE_COLON_OF_TIME, ":");
		}
		return "";
	}

	/**
	 * * @Description: TODO(判断日期是否是今天以前)
	 * 
	 * @param date
	 *            2015-02-15 00:00:00
	 * @param day
	 *            12
	 * @return
	 * @return boolean
	 * @author: 葛海松
	 * @time: 2015年2月28日 下午6:01:28
	 * @throws
	 */
	public static boolean isBeforeToday(String date, Integer day) {
		if (day != null) {
			String[] dd = date.split(DELIMITER_ONE_LINE);
			date = dd[0] + DELIMITER_ONE_LINE + dd[1] + DELIMITER_ONE_LINE
					+ day;
		}
		String today = formatByDate(new Date(), DATE_FORMAT_YYYYMMDD);
		long num = getQuot(parse(date), parse(today));
		if (num > 0)
			return true;
		return false;
	}

	/**
	 * * @Description: TODO(判断当前时间是否在指定试卷范围内)
	 * 
	 * @param startTime
	 * @param endTime
	 * @return true 过期
	 * @return boolean
	 * @author: 葛海松
	 * @time: 2015年3月28日 下午3:56:25
	 * @throws
	 */
	public static boolean isOverdue(Date startTime, Date endTime) {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		Calendar start = Calendar.getInstance();
		start.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		return !(now.after(start) && now.before(end));
	}

	public static boolean isOverdue(String startTime, String endTime) {
		return isOverdue(parse(startTime), parse(endTime));
	}

	/************************ 计算时间段相关 end *******************************/
	public static void main(String[] args) {
		// System.out.println( parseTime("10:15-10:45"));
		// System.out.println( reduParseTime("10HS15-10HS45"));
		// System.out.println(getTimeByNum(88,88));
		// System.out.println(getNumTime("21:45","22:00"));
		// System.out.println("111:"+getNumTime("8:00","8:15"));
		// System.out.println( getContinueTimeNumByRangeTimeNum("88,88"));;
		// getBeginEndDayOfWeek("2014-12-17");
		// aodWeek("2014-12-31" ,-1);
		// getFreeTimeRangeOfDay(30,15);
		// getdateListOfWeek("2014-12-7","2014-12-13");
		// System.out.println(formatByDate(addMinute("2014-12-7 8:20",15),
		// DATE_FORMAT_YYYYMMDD_HHMM));
		// String s="0000000002714000";
		// System.out.println(remove0leadString(s));
		// System.out.println(Integer.parseInt(s));
		// System.out.println(dateFormatError("2012年11月16日d人fas"));
		// System.out.println(dateFormatError("12-23 12:3f0"));
		// System.out.println(ParseDate.getYear(addYear(new Date(),8)));
		// parse("2012e-02-05");/
		// System.out.println(isNumber("20"));
		// System.out.println(isNumber("-20"));
		// System.out.println(isNumber("+200.0"));
		// System.out.println(isNumber("20.568"));
		// System.out.println(isNumber("2d0"));
		// System.out.println(isNumber("-0.25"));
		// System.out.println(isNumber("--20"));
		//
		// System.out.println(getYear(new Date()));
		// System.out.println(getMonth(new Date()));
		// System.out.println(getDay(new Date()));
		// System.out.println(0.0==0);
		// System.out.println(getstr("Tuesday,29 March 2011 9:33:00 GMT",3));
		// String s=null;
		// System.out.println(getstr(s,33));
		// s=null;
		// System.out.println(string2Double(s)+34);
		// SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
		// Date d=parse("");
		// System.out.println("日期："+sdf.format(d));
		// Date d=new Date();
		// System.out.println(ParseDate.formatByDate(d, 1));
		// d=addDate(d,-3);
		// System.out.println(ParseDate.formatByDate(d, 1));
		//
		// System.out.println(parse("2014-2-21"));
		// System.out.println(getFistDateOfMonth("2014-12-21"));
		// System.out.println(getLastDateOfMonth("2014-12-21"));
		// System.out.println(getDayOfMonth(parse("2014-2-29")));
		// System.out.println(formatByString("2015-1-11",DATE_FORMAT_YYYYYEARMMMONTH));
		// System.out.println(formatByDate(aodMonth("2015-1-11",1),DATE_FORMAT_YYYYMMDD_HHMMSS));
		// System.out.println(getDate("2014-2-29"));
		// System.out.println(getStartEndTimeOfWeek("2014-12-22"));
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdf.format(aodWeek(new Date(), 1)));
		// System.out.println(isBeforeToday("2015-2-28 00",28));
		// System.out.println(ParseDate.getTimeByNum("33,34,35"));
//		System.out.println(ParseDate.getContinueTimeNumByRangeTimeNum(ParseDate
//				.getNumTime("08:00", "08:45")));
//		;
//		System.out
//				.println(getDatePoor("2015-1-3 15:2:30", "2015-1-3 16:30:30"));
//		System.out
//				.println(getDatePoorMinute("2015-3-20 11:45:30", ParseDate
//						.formatByDate(new Date(), DATE_FORMAT_YYYYMMDD_HHMMSS)));
//		Double d = new Double(2.93);
//		System.out.println(d.intValue());
//		isOverdue("2015-3-30", "2015-3-30");
//
//		System.out.println(ParseDate
//						.formatByDate(addSecond("2015-1-3 15:2:30", 10), DATE_FORMAT_YYYYMMDD_HHMMSS));
        System.out.println(ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDDHH));
    }

}