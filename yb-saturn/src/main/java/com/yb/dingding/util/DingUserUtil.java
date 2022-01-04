package com.yb.dingding.util;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import com.yb.dingding.config.URLConstant;

public class DingUserUtil {
    /**
     * 根据钉钉id获取钉钉用户详情
     * @param accessToken 登录token
     * @param userId 钉钉用户id
     * @return
     */
    public static OapiUserGetResponse getDingUserDetail(String accessToken, String userId){

        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            /*if(response != null){
                saveDDuser(response);
            }*/

            return response;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据code获取当前的钉钉用户id
     * @param authCode 免登码
     * @return
     */
    public static String getDingUserId(String authCode){
        String accessToken = DingAccessTokenUtil.getToken("InternalH5");
        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(authCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
        //3.查询得到当前用户的userId
        // 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
        String userId = response.getUserid();
        return userId;
    }
}
