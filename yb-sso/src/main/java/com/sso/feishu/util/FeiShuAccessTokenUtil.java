package com.sso.feishu.util;

import com.sso.chatapi.utils.ObjectMapperUtil;
import com.sso.feishu.config.FeiShuURLConstant;
import com.sso.feishu.vo.AppAccessToken;
import com.sso.feishu.vo.TenantAccessToken;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j
public class FeiShuAccessTokenUtil {
    private static String appId = "cli_9f53ddfa9d72500d";//先写死
    private static String appSecret = "oEefTkrAF7BwXizmFzPVgfWAHPjQo6Wn";
    /**
     * 飞书获取TenantAccessToken
     * @return
     */
    public static String getTenantAccessToken(){
        Map<String, String> map = new HashMap<>();
        map.put("app_id", appId);
        map.put("app_secret", appSecret);
        try {
            TenantAccessToken tenantAccessToken =
                    ObjectMapperUtil.toObject(HttpClientUtil.sendHttpPost(FeiShuURLConstant.TENANT_ACCESS_TOKEN, map), TenantAccessToken.class);
            if (tenantAccessToken.getCode() != 0) {
                log.error("------------------获取TenantAccessToken出错----------------------");
                return null;
            }
            return tenantAccessToken.getTenantAccessToken();
        }catch (Exception e){
            log.error("------------------获取TenantAccessToken出错----------------------");
            e.printStackTrace();
        }
      return null;
    }

    /**
     * 获取AppAccessToken
     * @return
     */
    public static String getAppAccessToken(){
        Map<String, String> map = new HashMap<>();
        map.put("app_id", appId);
        map.put("app_secret", appSecret);
        try {
            AppAccessToken appAccessToken = ObjectMapperUtil.toObject(
                    HttpClientUtil.sendHttpPost(FeiShuURLConstant.APP_ACCESS_TOKEN, map), AppAccessToken.class);
            return appAccessToken.getAppAccessToken();
        } catch (RuntimeException e) {
            log.error("------------------获取AppAccessToken时出错---------------------");
            e.printStackTrace();
        }
       return null;
    }

}
