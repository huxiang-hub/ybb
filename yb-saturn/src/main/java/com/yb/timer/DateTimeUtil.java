package com.yb.timer;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author my
 * 日期工具类
 */
public class DateTimeUtil {

    public static final String DEFAULT_DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static final long MILLISECOND = 24 * 60 * 60 * 1000;

    public static final String DATE_FORMATTER = "yyyyMMdd";

    public static final String DEFAULT_DATE_FORMATTER = "yyyy-MM-dd";

    public static final String YEAR_FORMATTER = "yyyy";

    public static final String MONTH_FORMATTER = "yyyy-MM";

    public static final String ORDER_TIME = "yyyyMMddHHmmssSSS";

    public static final String IOT_SEND_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";


    /**
     * 格式化结束时间
     *
     * @param endTime 结束时间
     * @return 时间
     */
    public static Date formatEndTime(Date endTime) {
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        return endTime;
    }

    /**
     * 格式化结束时间年
     *
     * @param endTime 结束时间
     * @return 时间
     */
    public static Date formatYearEndTime(Date endTime) {
        endTime.setMonth(11);
        endTime.setDate(31);
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        return endTime;
    }

    /**
     * 格式化开始时间年
     *
     * @param StartTime 结束时间
     * @return 时间
     */
    public static Date formatYearStartTime(Date StartTime) {
        StartTime.setMonth(01);
        StartTime.setDate(01);
        return StartTime;
    }

    /**
     * 格式化开始时间月
     *
     * @param StartTime 结束时间
     * @return 时间
     */
    public static Date formatMonthStartTime(Date StartTime) {
        StartTime.setDate(01);
        return StartTime;
    }


    /**
     * 格式化结束时间月
     *
     * @param endTime 结束时间
     * @return 时间
     */
    public static Date formatMonthEndTime(Date endTime) {
        endTime.setDate(31);
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        return endTime;
    }


    /**
     * 将字符串转化为时间
     *
     * @param date         string 类型时间
     * @param formatString 格式
     * @return
     */
    public static Date format(String date, String formatString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.CHINA);
        return format.parse(date);
    }

    /**
     * 将时间格式化成指定格式
     *
     * @param date         时间
     * @param formatString 格式
     * @return 格式化后时间
     */
    public static String format(Date date, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    /**
     * now时间字符串
     *
     * @return now: "yyyy-MM-dd HH:mm:ss"
     */
    public static String now() {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMATTER);
        return format.format(Date.from(Instant.now()));
    }

    /**
     * now时间字符串
     *
     * @param formatString format
     * @return now:format
     */
    public static String now(String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(Date.from(Instant.now()));
    }


    /**
     * 获取当前日期时间字符串
     *
     * @param formatter 格式化
     * @return String
     */
    public static String getCurrentDateTimeAsString(DateTimeFormatter formatter) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (null == formatter) {
            formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER);
        }
        return currentDateTime.format(formatter);
    }

    /**
     * 获取当前日期字符串
     *
     * @param formatter 格式化
     * @return String
     */
    public static String getCurrentDateAsString(DateTimeFormatter formatter) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (null == formatter) {
            formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        }
        return currentDateTime.format(formatter);
    }

    /**
     * 转换Date为String
     *
     * @param formatter 格式化
     * @return String
     */
    public static String convertDateToString(DateTimeFormatter formatter, Date date) {
        if (null == formatter) {
            formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.format(formatter);
    }

    /**
     * 获取当前日期时间
     *
     * @param formatter 格式化
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime(DateTimeFormatter formatter) {
        String text = getCurrentDateTimeAsString(null);
        if (null == formatter) {
            formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMATTER);
        }
        return LocalDateTime.parse(text, formatter);
    }

    /**
     * 获取当前日期时间
     *
     * @param formatter 格式化
     * @return Date
     */
    public static Date getCurrentDate(DateTimeFormatter formatter) {
        LocalDateTime parse = getCurrentLocalDateTime(formatter);
        return convertLocalDateTimeToDate(parse);
    }

    /**
     * 转换java8的LocalDateTime为Date对象
     *
     * @param localDateTime java8
     * @return Date
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 转换Date对象为java8的LocalDateTime
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     *
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * 在当前时间加秒数
     *
     * @param second 秒(可为负数)
     * @return 日期
     */
    public static Date plusSecondBaseOnCurrentDate(int second) {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime plusSeconds = currentLocalDateTime.plusSeconds(second);
        return convertLocalDateTimeToDate(plusSeconds);
    }

    /**
     * 在当前时间加分钟
     *
     * @param min 分钟(可为负数)
     * @return 日期
     */
    public static Date plusMinBaseOnCurrentDate(int min) {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime plusMin = currentLocalDateTime.plusMinutes(min);
        return convertLocalDateTimeToDate(plusMin);
    }

    /**
     * 在当前时间加小时
     *
     * @param hour 小时(可为负数)
     * @return 日期
     */
    public static Date plusHourBaseOnCurrentDate(int hour) {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime plusHours = currentLocalDateTime.plusHours(hour);
        return convertLocalDateTimeToDate(plusHours);
    }

    /**
     * 在当前时间加小时
     *
     * @param day 天(可为负数)
     * @return 日期
     */
    public static Date plusDayBaseOnCurrentDate(int day) {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime plusHours = currentLocalDateTime.plusDays(day);
        return convertLocalDateTimeToDate(plusHours);
    }

    /**
     * 获取第二天的凌晨三点 - 主要用于JWT过期时间
     *
     * @return 日期
     */
    public static Date getThreeOclockAMOfTheNextDay() {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime nextDay = currentLocalDateTime.plusDays(1);
        LocalDateTime threeOclockAMOfTheNextDay = LocalDateTime.of(nextDay.getYear(), nextDay.getMonth(), nextDay.getDayOfMonth(), 3, 0, 0);
        return convertLocalDateTimeToDate(threeOclockAMOfTheNextDay);
    }

    /**
     * 获取第七天的凌晨三点 - 主要用于JWT过期时间
     *
     * @return 日期
     */
    public static Date getThreeOclockAMOfSeventhDay() {
        LocalDateTime currentLocalDateTime = getCurrentLocalDateTime(null);
        LocalDateTime nextDay = currentLocalDateTime.plusDays(7);
        LocalDateTime threeOclockAMOfSeventhDay = LocalDateTime.of(nextDay.getYear(), nextDay.getMonth(), nextDay.getDayOfMonth(), 3, 0, 0);
        return convertLocalDateTimeToDate(threeOclockAMOfSeventhDay);
    }

    /**
     * 将时间加上天数获取新的时间
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime();
        day = day * MILLISECOND;
        time += day;
        return new Date(time);
    }

    /**
     * 获取当前月的最后一天
     */
    public static String getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }


    //获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //获取昨天的结束时间
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //获取当天的开始时间
    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //获取当天的结束时间
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取本周结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    //获取上周开始时间
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }


    // 获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    // 获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));

        return format(cal.getTime(), DEFAULT_DATE_FORMATTER);
    }

    // 获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取上月的开始时间
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取上月的结束时间
    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    // 返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    // 返回某个日期前几天的日期
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    // 获取本年的开始时间
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    // 获取本年的结束时间
    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    // 获取指定月份的结束时间
    public static Date getEndDayByMonth(int month, Date date) {
        int nowMonth = getNowMonth();
        if (nowMonth == month) {
            return getEndDayOfMonth();
        }
        if (nowMonth <= month) {
            return formatMonthEndTime(date);
        }
        Calendar calendar = Calendar.getInstance();
        if (nowMonth > month) {
            int m = nowMonth - month;
            calendar.set(getNowYear(), getNowMonth() - 1 - m, 1);
            int day = calendar.getActualMaximum(5);
            calendar.set(getNowYear(), getNowMonth() - 1 - m, day);
        }
        return getDayEndTime(calendar.getTime());
    }

    // 获取指定月份的开始时间
    public static Date getStartDayByMonth(int month, Date date) {
        int nowMonth = getNowMonth();
        if (nowMonth == month) {
            return getEndDayOfMonth();
        }
        if (nowMonth <= month) {
            return formatMonthEndTime(date);
        }
        Calendar calendar = Calendar.getInstance();
        if (nowMonth > month) {
            int m = nowMonth - month;
            calendar.set(getNowYear(), getNowMonth() - 1 - m, 1);
            int day = calendar.getActualMaximum(5);
            calendar.set(getNowYear(), getNowMonth() - 1 - m, day);
        }
        return getDayStartTime(calendar.getTime());
    }

    // 获取指定日期是哪一月
    public static int getLastDayByDesignationMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    public static Date getBeforeByHourTime(int ihour) {
        String returnstr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        return calendar.getTime();
    }


    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

    public static Date getNextHoutTime(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date); //设置时间
        c.add(Calendar.HOUR_OF_DAY, hour);
        Date result = c.getTime(); //结果
        return result;
    }

    public static Date conversionSdDateTime(String sdDate, Date time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(time);
        return simpleDate.parse(sdDate + (" ") + date);
    }


    /**
     * 根据所选年和周获取该周开始键
     *
     * @param year 所在年分
     * @param week 所在周
     * @return
     */
    public static LocalDate parseWeekBegin(int year, int week) {

        return LocalDate.parse(year + " " + week,
                new DateTimeFormatterBuilder().appendPattern("YYYY w").parseDefaulting(WeekFields.ISO.dayOfWeek(), 1).toFormatter());
    }

    /**
     * 根据周开始时间获取周结束时间
     *
     * @param begin
     * @return
     */
    public static LocalDate parseWeekEnd(LocalDate begin) {
        return begin.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    }

    /**
     * 根据日期获取所在年的第几周
     *
     * @param date
     * @return
     */
    public static int getYearWeek(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.applyPattern("w");
        return Integer.valueOf(dateFormatter.format(date));
    }

    //根据时间获取所在周的开始时间
    public static String getWeekStartTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return sdf.format(cal.getTime());
    }

    //根据时间获取所在周的结束时间
    public static String getWeekEndime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.add(Calendar.DATE, 6);
        return sdf.format(cal.getTime());
    }

    /*
    判读时间差距，两个时间相差多少天，时，分，秒
     */
    public static Long getDay(String nowDate, Date oldDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long days = null;
        try {
            Date currentTime = dateFormat.parse(dateFormat.format(nowDate));//现在系统当前时间
            long diff = currentTime.getTime() - oldDate.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static void main(String[] args) {
//        LocalDate begin = parseWeekBegin(2020, 37);
//        System.out.println(begin.toString());
//        System.out.println(parseWeekEnd(begin));
//        getYearWeek(new Date());
//        String a="2020-07-08";
//        System.out.println(getLastDayByDesignationMonth(new Date()));
        Date date = new Date();
        System.out.println(format(date, YEAR_FORMATTER));
        System.out.println(getWeekEndime(date));
        System.out.println(getLastDayByDesignationMonth(date));

        System.out.println(getFirstDayOfMonth(2020, 9));
    }
}
