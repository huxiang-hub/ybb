package com.yb.feishu.utils;

import org.apache.commons.codec.digest.HmacUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClockInHeadersutil {

    /*考勤打卡*/
    private static String AppKey = "7b030b395080d531872135d5bf032aa4";//先写死
    /*考勤打卡的appSecret*/
    private static String AppSecret = "d8c8524ad1e12d7752931d3e82383112";

    /**
     * 获取飞书考勤打卡请求头所需参数
     * @return
     */
    public static Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        // 返回当前系统的UTC时间，具体实现可参看JDK源码
        Long expiredTime = cal.getTimeInMillis() + 1000 * 60 * 10;//十分钟过期
        //32位随机字符串
        String signatureNonce = UUID.randomUUID().toString().trim().replaceAll("-", "");
        //签名
        String authorization = HmacUtils.hmacSha1Hex(AppSecret, signatureNonce + expiredTime);
        headers.put("AppKey", AppKey);
        headers.put("Authorization", authorization);
        headers.put("ExpiredTimes", expiredTime.toString());
        headers.put("SignatureNonce", signatureNonce);
        return headers;
    }

}
