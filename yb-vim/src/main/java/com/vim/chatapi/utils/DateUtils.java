package com.vim.chatapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /*public static void main(String[] args) throws ParseException {

        System.out.println(operDate("2018-05-24", -1));

    }*/

    public static String operDate(String str, int day) throws ParseException {
        Date date;
        date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        //当前日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DAY_OF_MONTH, day);//加、减
        return format.format(calendar.getTime());
    }
}
