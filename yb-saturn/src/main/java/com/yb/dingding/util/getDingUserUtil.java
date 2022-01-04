package com.yb.dingding.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import com.yb.dingding.config.URLConstant;
import com.yb.dingding.controller.InternalH5Controller;
import com.yb.dingding.entity.DDUser;

public class getDingUserUtil {

    /**
     * 获取访问人的钉钉id和姓名
     * @param requestAuthCode
     * @param apUnique
     * @return
     */
    public static DDUser  getDingUserUtil(String requestAuthCode, String apUnique){
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
            String userid = response.getUserid();
            String userName = InternalH5Controller.getUserName(accessToken, userid);
            DDUser ddUser = new DDUser();
            ddUser.setUserid(userid);
            ddUser.setName(userName);
            return ddUser;
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
