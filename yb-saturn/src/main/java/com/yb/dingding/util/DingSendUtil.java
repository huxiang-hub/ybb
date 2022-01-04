package com.yb.dingding.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import com.yb.dingding.config.URLConstant;
import org.springblade.core.tool.utils.StringUtil;

import java.util.UUID;

import static com.yb.dingding.config.Constant.AGENT_ID;

public class DingSendUtil {

    /**
     * 钉钉消息推送
     * @param msg
     * @param userId
     * @return
     */
    public static String dingSend(String msg, String userId){
        String apUnique = "InternalH5";
        String token = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.ASYNCSEND_V2);
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(AGENT_ID);
            req.setUseridList(userId);
            OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            obj1.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            obj2.setContent(msg);
            obj1.setText(obj2);
            req.setMsg(obj1);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
        } catch (ApiException e) {
            e.printStackTrace();
            return "err";
        }
        return "ok";
    }

    /**
     * 钉钉群推送
     * @param msg
     * @return
     */
    public static String send(String msg, String chatid){
        if(StringUtil.isEmpty(chatid)){
            chatid = "chatcd72b336adb1688845a6e4d19d42196b";
        }
        String apUnique = "InternalH5";
        String token = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.CHAT_SEND);
            OapiChatSendRequest req = new OapiChatSendRequest();
            req.setChatid(chatid);//群id
            OapiChatSendRequest.Text obj1 = new OapiChatSendRequest.Text();
            obj1.setContent(msg);
            req.setText(obj1);
            req.setMsgtype("text");
            OapiChatSendResponse rsp = client.execute(req, token);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
            return "err";
        }
        return "ok";
    }



}
