package com.yb.dingding.controller;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.taobao.api.ApiException;
import com.yb.dingding.entity.DingChatCreateParam;
import com.yb.dingding.service.DdSendService;
import com.yb.dingding.util.DingAccessTokenUtil;
import com.yb.dingding.util.DingSendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.tool.ObjectMapperUtil;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.yb.dingding.config.URLConstant.CHAT_CREATE;

@RestController
@RequestMapping(value = "/DdSendController/InternalH5")
@Api(tags = "钉钉消息")
public class DdSendController {

    @Autowired
    private DdSendService ddSendService;

    @GetMapping(value = "/asyncsend_v2")
    @ApiOperation(value = "钉钉消息推送", notes = "传入参数msg:消息内容, userId用户钉钉id可多个用','隔开, apUnique应用标识符固定传InternalH5")
    public void asyncsend_v2(String msg, String userId, String apUnique){
        ddSendService.asyncsend_v2(msg, apUnique, userId);
    }
    @GetMapping(value = "/send")
    @ApiOperation(value = "钉钉群消息推送")
    public void send(String msg, String apUnique){
        ddSendService.send(msg, apUnique, null);
    }

    @PostMapping("/chatCreate")
    @ApiOperation(value = "创建群")
    public R<OapiChatCreateResponse> chatCreate(@RequestBody DingChatCreateParam dingChatCreateParam){

        try {
            String token = DingAccessTokenUtil.getToken("InternalH5");
            DingTalkClient client = new DefaultDingTalkClient(CHAT_CREATE);
            OapiChatCreateRequest req = new OapiChatCreateRequest();
            req.setName(dingChatCreateParam.getName());
            req.setOwner(dingChatCreateParam.getOwner());
            req.setUseridlist(dingChatCreateParam.getUseridlist());
            OapiChatCreateResponse rsp = client.execute(req, token);
            System.out.println(rsp.getBody());
            return R.data(rsp);
        } catch (ApiException e) {
            e.printStackTrace();
            return R.fail("出现异常");
        }
    }

}
