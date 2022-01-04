package com.yb.workbatch.utils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取后一天和前三天时间(毫秒)
 */
public class GetTimeUtils {

    public static long[] getToDayTime(){
        long [] time = new long[2];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowDate);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        String format1 = simpleDateFormat.format(calendar.getTime());
        Date date1 = null;//后一天时间
        try {
            date1 = simpleDateFormat.parse(format1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date1);
        time[0] = date1.getTime();
        Calendar calendar1 = new GregorianCalendar();
        nowDate = new Date();
        calendar1.setTime(nowDate);
        calendar1.add(calendar1.DATE,-3);//把日期往后增加一天.整数往后推,负数往前移动
        String format2 = simpleDateFormat.format(calendar1.getTime());
        Date date2 = null;//前三天时间
        try {
            date2 = simpleDateFormat.parse(format2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date2);
        time[1] = date2.getTime();
        return time;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        list.add("张三");
        list.add("妞儿");
        list.add("张三");
        list.add("妞儿");
        list.add("妞儿");
        list.add("李四");
        list.add("张三");
        list.add("王五");
        list.add("张三");
        list.add("王五");
        list.add("张三");
        list.add("李四");

        Map<String, Integer> map = new HashMap();
        for(String s : list){
            Integer value = map.get(s);
            if(map.containsKey(s)){
                map.put(s, value + 1);
            }else {
                map.put(s, 1);
            }

        }
        System.out.println(map);
    }
}
