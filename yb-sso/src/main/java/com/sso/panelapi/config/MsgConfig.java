package com.sso.panelapi.config;


import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sso.utils.R;

import java.io.IOException;

public class MsgConfig {
    // 短信应用 SDK AppID
    final static int APP_ID = 1400338477; // SDK AppID 以1400开头
    // 短信应用 SDK AppKey
    final static  String APP_KEY = "f0cd8f9b4afb92c040d109ee63946e73";
    // 需要发送短信的手机号码
    //static   String phoneNumbers = "17708135874";
    // 短信模板 ID，需要在短信应用中申请
    final  static  Integer TEMPLATE_ID = 560760; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
    // 签名
    final static String SMS_SIGN = "成都君联智创科技有限公司"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请
    //设置短信失效时间
    final  static Integer TIME_OUT = 10;
    public static R sendMessages(String phoneNum,String code){
        try {
            String[] params = {code,TIME_OUT.toString()};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
                    TEMPLATE_ID, params, SMS_SIGN, "", "");

            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            return R.error(" HTTP 响应码错误");
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            return R.error(" JSON 解析错误");
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            return R.error(" 网络 IO 错误");
        }
        return R.ok("发送成功！");
    }

    public static String sendStringMessages(String phoneNum,String code){
        try {
            String[] params = {code,TIME_OUT.toString()};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
                    TEMPLATE_ID, params, SMS_SIGN, "", "");

            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            return " HTTP 响应码错误";
        } catch (org.json.JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            return " JSON 解析错误";
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            return " 网络 IO 错误";
        }
        return "发送成功！";
    }

}
