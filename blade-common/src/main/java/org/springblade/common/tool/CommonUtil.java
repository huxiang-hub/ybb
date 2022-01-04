/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.common.tool;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import org.springblade.common.constant.DingURLConstant;

import java.util.UUID;

import static org.springblade.common.constant.DingURLConstant.URL_GET_TOKKEN;


/**
 * 通用工具类
 *
 * @author Chill
 */
public class CommonUtil {

    /*内部应用*/
    /**
     * 开发者后台->应用开发-企业内部应用->选择您创建的小程序->应用首页-查看详情->查看AppKey
     */
    public static final Long AGENT_ID = 877379767L;
    /**
     * 开发者后台->应用开发-企业内部应用->选择您创建的小程序->应用首页-查看详情->查看AppKey
     */
    public static final String APP_KEY = "dinglkaw4r4fclpynfd9";
    /**
     * 开发者后台->应用开发-企业内部应用->选择您创建的小程序->应用首页-查看详情->查看AppSecret
     */
    public static final String APP_SECRET = "QomL_-KEOSpdRnHnP3ccEfcWZ4RPXqAECp-OUJrpfTVA-Op1cabHOJgF596xSHwS";



    /**
     * 内部应用获取access_token
     * @return
     * @throws RuntimeException
     */
    public static String getToken() throws RuntimeException {
        try{
            DefaultDingTalkClient client;
            client = new DefaultDingTalkClient(URL_GET_TOKKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(APP_KEY);
//            request.setAppkey(Constant.APP_KEY);
//            request.setAppsecret(Constant.APP_SECRET);
            request.setAppsecret(APP_SECRET);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            return accessToken;
        } catch (ApiException e) {
            throw new RuntimeException();
        }
    }




    /**
     * 钉钉消息推送
     * @param msg 消息内容
     * @param userId 发送到哪个用户id
     * @return
     */
    public static String dingSend(String msg, String userId){

        UUID uuid = UUID.randomUUID();
        String token = CommonUtil.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingURLConstant.ASYNCSEND_V2);
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(AGENT_ID);
            req.setUseridList(userId);
            OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            obj1.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            obj2.setContent(msg + " \nUUID:" + uuid);
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
     * @param msg 消息内容
     * @return
     */
    public static String send(String msg){

        String token = CommonUtil.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingURLConstant.CHAT_SEND);
            OapiChatSendRequest req = new OapiChatSendRequest();
            req.setChatid("chatcd72b336adb1688845a6e4d19d42196b");//群id
            OapiChatSendRequest.Text obj1 = new OapiChatSendRequest.Text();
            obj1.setContent("异常消息:\n" + msg);
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
