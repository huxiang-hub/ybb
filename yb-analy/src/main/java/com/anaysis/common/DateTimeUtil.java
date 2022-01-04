package com.anaysis.common;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
     * */
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

    // 获取指定日期是哪一月
    public static int getLastDayByDesignationMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }


    /*
    判读时间差距，两个时间相差多少天，时，分，秒
     */
    public static Long getDay(Date nowDate, Date oldDate) {
        Long days = null;
        long diff = nowDate.getTime() - oldDate.getTime();
        days = diff / (1000 * 60 * 60);
        return days;
    }

//    public static void main(String[] args) throws ParseException {
//        Date date = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date pastTime = dateFormat.parse("2020-09-13 17:34:46");//过去时间
//        System.out.println(getDay(date, pastTime));
//    }
}
