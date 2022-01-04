package com.anaysis.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    /***
     * 返回时间带毫秒的时间格式的信息 yyyy-MM-dd HH:mm:ss SSS
     * @return
     */
    public static String refNowMilli() {
        SimpleDateFormat sdfmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return sdfmm.format(new Date(System.currentTimeMillis()));
    }

    /***
     * 返回时间带秒的信息内容 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String refFormatNowTimes() {
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfm.format(new Date(System.currentTimeMillis()));
    }

    public static String refNowTimes(Date date) {
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfm.format(date);
    }

    public static String refNowMilli(Date date) {
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return sdfm.format(date);
    }

    /***
     * 仅仅返回日期内容信息
     * @return
     */
    public static String refNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /****
     * 開始時間和當前時間差
     * @param startDate  单位分钟
     * @return
     */
    public static double durationTime(Date startDate) {
        long a = System.currentTimeMillis();
        long b = (startDate != null) ? startDate.getTime() : System.currentTimeMillis();
        double c = (double) ((a - b) / (1000 * 60));
        return c;
    }

    /****
     * 開始時間和當前時間差
     * @param startDate  单位秒
     * @return
     */
    public static int duration(Date startDate) {
        long a = System.currentTimeMillis();
        long b = (startDate != null) ? startDate.getTime() : System.currentTimeMillis();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    /***
     * 计算最后时间和当前时间的差值是否超过timeout的时间 返回真true假false
     * @param lasttime
     * @param timeout
     * @return
     * @throws Exception
     */
    public static boolean diffNowTime(Long lasttime, Long timeout) {
        if (lasttime == null) {
            return false;
            //判断当前时间和最后的时间差大于等于超时时间，
        }
        if (System.currentTimeMillis() - lasttime >= timeout) {
            return true;
        }
        return false;
    }

    /****
     * 开始时间跟结束时间的差值是否超过间隔时间
     * @param lasttime
     * @param starttime
     * @param timeout
     * @return
     */
    public static boolean diffIntervalTime(Long starttime, Long lasttime, Long timeout) {
        //判断如果结束时间为空，
        if (lasttime == null) {
            lasttime = System.currentTimeMillis();
        }
        if (lasttime - starttime >= timeout) {
            return true;
        }
        return false;
    }


    /****
     * 開始時間和當前時間相差秒數
     * @param startDate
     * @return
     */
    public static int calLastedTime(long endtime, Date startDate) {
//        long a = System.currentTimeMillis();
        long b = (startDate != null) ? startDate.getTime() : System.currentTimeMillis();
        int c = (int) ((endtime - b) / 1000);
        return c;
    }

    /***
     *
     * @param datestr
     * @param format  转化时间格式
     * @return
     */
    public static Date toDate(String datestr, String format) {
        //如果类型格式为空，就用默认的格式
        format = format != null ? format : "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdfm = new SimpleDateFormat(format);
        Date dateTime = new Date();
        try {
            dateTime = sdfm.parse(datestr);
        } catch (ParseException e) {
            //e.printStackTrace();
            return dateTime;
        }
        return dateTime;
    }

    /****
     * 时间转化字符串
     * @param date
     * @param format
     * @return
     */
    public static String toDatestr(Date date, String format) {
        //如果类型格式为空，就用默认的格式
        format = format != null ? format : "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdfm = new SimpleDateFormat(format);
        return sdfm.format(date);
    }


    /****
     * 传入需要
     * @param date
     * @param adday
     * @return
     */
    public static Date addDayForDate(Date date, int adday) {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = null;
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, adday); //把日期往后增加一天(日期往后增加adday),整数  往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            System.out.println("前天：" + sj.format(date));
        } catch (Exception e) {
            return date;
        }
        return date;
    }
}
