package com.yb.statis.utils;

import org.springblade.core.tool.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeekOfDate {
    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 根据传入日期返回传入日期所在周的所有日期, 如果传入为null则默认当周
     * @param targeDay
     * @return
     */
    public static List<String> getWeekDate(String targeDay)  {
        List<String> weekDateList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Date date = new Date();
        if(!StringUtil.isEmpty(targeDay)){
            try {
                date = df.parse(targeDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        cld.setTimeInMillis(date.getTime());//当前时间
        cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);//周二
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);//周三
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);//周四
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);//周五
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//周六
        weekDateList.add(df.format(cld.getTime()));
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//周日
        weekDateList.add(df.format(cld.getTime()));
        return weekDateList;
    }

    public static void main(String[] args) {
        String targeDay = "2020-08-03";
        List<String> weekDateList = WeekOfDate.getWeekDate(targeDay);
        for(String week : weekDateList){
            System.out.println(week);
        }
    }
}
