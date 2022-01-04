package com.yb.common.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  获取节假日或者周末
 */

public class HolidaysAndFestivals {

    /**
     * 根据传入年,月份,返回该年的下一个月所有节假日
     * @param year
     * @param month
     * @return
     */
    public static Set<String> JJR(int year,int month) {
        //获取所有的周末
        Set<String> monthWekDay = getMonthWekDay(year, month);
        //http://timor.tech/api/holiday api文档地址
        Map jjr = getJjr(year, month+1);//月份加一
        Integer code = (Integer) jjr.get("code");
        if(code  != 0){
            return monthWekDay;
        }
        Map<String,Map<String,Object>> holiday = (Map<String, Map<String,Object>>) jjr.get("holiday");
        Set<String> strings = holiday.keySet();
        for (String str: strings) {
            Map<String, Object> stringObjectMap = holiday.get(str);
            Integer wage = (Integer) stringObjectMap.get("wage");
            String date = (String) stringObjectMap.get("date");
            //筛选掉补班
            if(wage .equals( 1)){
                monthWekDay.remove(date);
            }else{
                monthWekDay.add(date);
            }
        }
        return monthWekDay;
    }

    //获取节假日不含周末
    private static Map getJjr(int year, int month) {
        String url = "http://timor.tech/api/holiday/year/"+year+"-"+month;
        OkHttpClient client = new OkHttpClient();
        Response response;
        //解密数据
        String rsa = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            response = client.newCall(request).execute();
            rsa = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(rsa, Map.class);
    }

    //获取周末  月从0开始
    public static Set<String> getMonthWekDay(int year,int mouth){
        Set<String> dateList = new HashSet<>();
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(year, mouth , 1);
        int i = 1;
        while (calendar.get(Calendar.MONTH) < mouth+1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.MONTH) == mouth) {
                //System.out.println("周日：" + simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.MONTH) == mouth) {
                //System.out.println("周六：" + simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
        }
        return dateList;
    }


    public static void main(String[] args) {
        Set<String> jjr = JJR(2020, 5);
        for(String date : jjr){
            System.out.println(date);
        }
    }

}
