package com.yb.dingding.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.yb.auth.filter.UrlFilter;
import com.yb.dingding.entity.DingAppinfo;
import com.yb.dingding.mapper.DingAppinfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.yb.dingding.config.URLConstant.*;

/**
 * 获取access_token工具类
 */
@Component
@Slf4j
public class DingAccessTokenUtil {

    private static HttpServletRequest req;
    private static DingAppinfoMapper dingAppinfoMapper;

    @Autowired
    public DingAccessTokenUtil(HttpServletRequest request, DingAppinfoMapper dingAppinfoMapper){
        DingAccessTokenUtil.req = request;
        DingAccessTokenUtil.dingAppinfoMapper = dingAppinfoMapper;
    }

    /**
     * 获取钉钉应用信息
     * @return
     */
    public static DingAppinfo getDingAppinfo(){
        StringBuffer requestURL = req.getRequestURL();
        String tenant = UrlFilter.getTenant(requestURL);
        String url = requestURL.toString();
        int index = url.indexOf("?");//先获取url路径上?的下标
        int lastIndexOf;
        if(index == -1){//如果不存在,直接获取最后一个/的下标
            lastIndexOf = url.lastIndexOf("/");
        }else {//否则获取从?反向查询的第一个/
            lastIndexOf = url.lastIndexOf("/", index);
        }
        String apUnique = url.substring(url.lastIndexOf("/", lastIndexOf - 1) + 1, lastIndexOf);
        /*获取钉钉的应用信息*/
        DingAppinfo dingAppinfo = null;
        try {
            dingAppinfo =
                    dingAppinfoMapper.selectOne(new QueryWrapper<DingAppinfo>()
                            .eq("ap_unique", apUnique)
                            .eq("ap_domain", tenant)
                            .eq("status", 1));
        }catch (Exception e){
            log.error("同一个公司的同一类型应用存在多条启用信息");
        }
        return dingAppinfo;
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
            DingAppinfo dingAppinfo = DingAccessTokenUtil.getDingAppinfo(apUnique);
            if(dingAppinfo == null){
                log.error("钉钉应用信息未查到或查询出现异常");
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
            log.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
    }

//    /**
//     * 获取企业授权凭证
//     * @return
//     */
//
//    public static String getCorpToken(HttpServletRequest request){
//        StringBuffer requestURL = request.getRequestURL();
//        String tenant = UrlFilter.getTenant(requestURL);//访问的客户信息
//        CompanyCorpID companyCorpID = new CompanyCorpID();
//        String corpID = companyCorpID.getCompanyCorpID(tenant);
//        try {
//            String suiteToken = getSuiteToken();
//            DingTalkClient client = new DefaultDingTalkClient(URL_GET_CORP_TOKEN + suiteToken);
//            OapiServiceGetCorpTokenRequest req = new OapiServiceGetCorpTokenRequest();
//            req.setAuthCorpid(corpID);
//            OapiServiceGetCorpTokenResponse rsp = client.execute(req);
//            AccessToken accessToken = ObjectMapperUtil.toObject(rsp.getBody(), AccessToken.class);
//            return accessToken.getAccess_token();
//        } catch (ApiException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }
//
//    /**
//     *获取第三方应用凭证
//     * @return
//     */
//    public static String getSuiteToken(){
//        try {
//            DingTalkClient client = new DefaultDingTalkClient(URL_GET_SUITE_TOKEN);
//            OapiServiceGetSuiteTokenRequest req = new OapiServiceGetSuiteTokenRequest();
//            req.setSuiteKey(Constant.SUITE_KEY);
//            req.setSuiteSecret(Constant.SUITE_SECRET);
//            req.setSuiteTicket(Constant.SUITE_TICKET);
//            OapiServiceGetSuiteTokenResponse rsp = client.execute(req);
//            SuiteAccessToken suiteAccessToken = ObjectMapperUtil.toObject(rsp.getBody(), SuiteAccessToken.class);
////            System.out.println(suiteAccessToken);
//            return suiteAccessToken.getSuite_access_token();
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public static void main(String[] args)throws ApiException{
//        String corpToken = AccessTokenUtil.getCorpToken();
//        System.out.println(corpToken);

        String path = "http://localhost:2020/satapi/InternalH5/welcome";
        int indexOf = path.indexOf("?");
        int index;
        System.out.println(indexOf);
        if(indexOf == -1){
            index = path.lastIndexOf("/");
        }else {
            index = path.lastIndexOf("/", indexOf);
        }

        String oo=path.substring(path.lastIndexOf("/",   index- 1) + 1, index);
        System.err.println(oo);
    }
}
