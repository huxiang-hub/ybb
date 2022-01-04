package org.springblade.common.tool;

import lombok.extern.log4j.Log4j;
import org.springblade.common.constant.FeiShuURLConstant;
import org.springblade.common.pojo.Content;
import org.springblade.common.pojo.FeiShuSendVO;
import org.springblade.common.pojo.TenantAccessToken;

import java.util.HashMap;
import java.util.Map;

@Log4j
public class FeiShuUtil {

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
     * 飞书消息发送研发群
     * @param msg 消息内容
     * @return
     */
    public static String feiShuChatSend(String msg){

        try {
            FeiShuSendVO feiShuSendVO = new FeiShuSendVO();
            feiShuSendVO.setChatId("oc_bba231df0daaa0d7fa662733f086c451");
            feiShuSendVO.setMsgType("text");
            Content content = new Content();
            content.setText(msg);
            feiShuSendVO.setContent(content);
            String toJSON = ObjectMapperUtil.toJSON(feiShuSendVO);
            String tenantAccessToken = getTenantAccessToken();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + tenantAccessToken);
            HttpClientUtil.sendHttpPost("https://open.feishu.cn/open-apis/message/v4/send/", toJSON, header);
        }catch (Exception e){
            return "飞书消息发送出现异常";
        }
        return "消息发送成功";
    }

}
