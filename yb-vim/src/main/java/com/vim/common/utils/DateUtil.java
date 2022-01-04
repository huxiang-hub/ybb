package com.vim.common.utils;

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

    /***
     * 仅仅返回日期内容信息
     * @return
     */
    public static String refNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(System.currentTimeMillis()));
    }



    /***
     * 仅仅返回日期内容信息
     * @return
     */
    public static String refNowDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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
    public static double calLastedTime(long currtime, Date startDate) {
//        long a = System.currentTimeMillis();
        long b = (startDate != null) ? startDate.getTime() : System.currentTimeMillis();
        double c = (double) ((currtime - b) / 1000);
        return c;
    }

    /****
     * 開始時間和當前時間差 以秒计算
     * @param startDate
     * @return
     */
    public static double durationTime(Date startDate) {
        long a = System.currentTimeMillis();
        long b = (startDate != null) ? startDate.getTime() : System.currentTimeMillis();
        double c = (double) ((a - b) / (1000 * 60));
        return c;
    }

    /***
     * 转化时间格式
     * @param datestr
     * @param format
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

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验e799bee5baa6e997aee7ad94e78988e69d8331333361313836证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  获得前多少分钟的时间和后多少分钟的时间
     * @param before
     * @return
     */
    private static Date  getCurrentTimeBeforeAndEnd(Integer before) {
        Date dNow = new Date(); // 当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(dNow);// 把当前时间赋给日历
        calendar.add(Calendar.MINUTE,before); // 设置当前时间多少分钟前的时间
        dBefore = calendar.getTime(); // 得到前一天的时间
        calendar.setTime(dNow);// 把当前时间赋给日历
        return dBefore;
    }


    /******
     * 按照格式转换为date时间格式信息
     * @param daystr
     * @return
     */
    public static Date changeDay(String daystr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(daystr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
}
