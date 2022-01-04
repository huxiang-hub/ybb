package com.yb.dingding.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import com.yb.dingding.config.URLConstant;
import com.yb.dingding.service.DdSendService;
import com.yb.dingding.util.DingAccessTokenUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.yb.dingding.config.Constant.AGENT_ID;

@Service
public class DdSendServiceImpl implements DdSendService {

    @Override
    public void asyncsend_v2(String msg, String apUnique, String userId) {
        UUID uuid = UUID.randomUUID();
        String token = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.ASYNCSEND_V2);
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(AGENT_ID);
            if(StringUtil.isEmpty(userId)){
                req.setUseridList("203530266240140226");//接收者的用户userid列表(龙正彬)
            }else {
                req.setUseridList(userId);
            }
//            req.setUseridList("31120559171053860");
            OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            obj1.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            obj2.setContent(msg + " \nUUID:" + uuid);
            obj1.setText(obj2);
            req.setMsg(obj1);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, token);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String msg, String apUnique, String chatid) {
        if(StringUtil.isEmpty(chatid)){
            chatid =  "chatcd72b336adb1688845a6e4d19d42196b";
        }
        String token = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.CHAT_SEND);
            OapiChatSendRequest req = new OapiChatSendRequest();
            req.setChatid("");//群id
            OapiChatSendRequest.Text obj1 = new OapiChatSendRequest.Text();
            obj1.setContent("异常消息:\n" + msg);
            req.setText(obj1);
            req.setMsgtype("text");
            OapiChatSendResponse rsp = client.execute(req, token);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


}
