package com.sso.dingding.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.sso.dingding.entity.DingAppinfo;
import com.sso.filter.UrlFilter;
import com.sso.mapper.DingAppinfoMapper;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.sso.dingding.config.URLConstant.URL_GET_TOKKEN;

/**
 * 获取access_token工具类
 */
@Component
public class AccessTokenUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(AccessTokenUtil.class);

    private static HttpServletRequest req;
    private static DingAppinfoMapper dingAppinfoMapper;

    @Autowired
    public  AccessTokenUtil(HttpServletRequest request, DingAppinfoMapper dingAppinfoMapper){
        AccessTokenUtil.req = request;
        AccessTokenUtil.dingAppinfoMapper = dingAppinfoMapper;
    }

    /**
     * 根据特殊标识符和域名查询钉钉基础信息
     * @param apUnique
     * @return
     */
    public static DingAppinfo getDingAppinfo(String apUnique){
        StringBuffer requestURL = req.getRequestURL();
        String tenant = UrlFilter.getTenant(requestURL);
        List<DingAppinfo> dingAppinfoList =
                dingAppinfoMapper.selectList(new QueryWrapper<DingAppinfo>()
                        .eq("ap_unique", apUnique)
                        .eq("ap_domain", tenant)
                        .eq("status", 1));
        if(dingAppinfoList.isEmpty()){
            return null;
        }
        return dingAppinfoList.get(0);
    }

    /**
     * 内部应用获取access_token
     * @return
     * @throws RuntimeException
     */
    public static String getToken(String apUnique) throws RuntimeException {
        try {
            DingAppinfo dingAppinfo = AccessTokenUtil.getDingAppinfo(apUnique);
            if(dingAppinfo == null){
                bizLogger.error("钉钉应用信息未查到或查询出现异常");
                return null;
            }
            String appKey = dingAppinfo.getAppKey();
            String secret = dingAppinfo.getAppSecret();
            DefaultDingTalkClient client;
            client = new DefaultDingTalkClient(URL_GET_TOKKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(appKey);
//            request.setAppkey(Constant.APP_KEY);
//            request.setAppsecret(Constant.APP_SECRET);
            request.setAppsecret(secret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            return accessToken;
        } catch (ApiException e) {
            bizLogger.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
    }


    public static void main(String[] args)throws ApiException{
        String apUnique = "";
        String accessToken = AccessTokenUtil.getToken(apUnique);
        System.out.println(accessToken);
    }
}
