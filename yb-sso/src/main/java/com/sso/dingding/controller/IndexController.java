package com.sso.dingding.controller;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.sso.chatapi.entity.AuthCode;
import com.sso.chatapi.entity.ImChatGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.User;
import com.sso.chatapi.service.IChatUserService;
import com.sso.chatapi.service.IImUserFriendService;
import com.sso.chatapi.service.IImUserService;
import com.sso.chatapi.vo.BaseStaffextVO;
import com.sso.dingding.config.URLConstant;
import com.sso.dingding.util.AccessTokenUtil;
import com.sso.dingding.util.ServiceResult;
import com.sso.supervise.service.IBaseStaffinfoService;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业内部内部-小程序Quick-Start示例代码 实现了最简单的免密登录（免登）功能
 */
@RestController
public class IndexController {
    private static final Logger bizLogger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IImUserService imUserService;
    @Autowired
    private IChatUserService chatUserService;
    @Autowired
    private IChatUserService userService;
    @Autowired
    private IImUserFriendService imUserFriendService;

    /*分页查询所需*/
    private final Long offset = 0L;
    private final Long size = 100L;
    /**
     * 欢迎页面,通过url访问，判断后端服务是否启动
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome";
    }

    /**
     * 钉钉用户登录，显示当前登录用户的userId和名称
     *
     * @param authCode 免登临时code
     *, method = RequestMethod.POST
     */
    @PostMapping(value = "/ddlogin")
    public R login(@RequestBody AuthCode authCode) {
        String code = authCode.getAuthCode();
        String apUnique = authCode.getApUnique();
        Map<String, Object> result = new HashMap<>();
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken(apUnique);

        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
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
        String ddId = response.getUserid();
       /* String userName = getUserName(accessToken, userId);
        //返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("userName", userName);
        ServiceResult serviceResult = ServiceResult.success(resultMap);*/
        User user = getUserByDdId(ddId);
        if(user == null){
            return R.fail(400, "该用户未绑定印聊用户！");
        }else {
            user.setDdId(ddId);
            user.setPassword(null);
            BaseStaffextVO staffextVO =
                    chatUserService.getBaseStaffextVO(user.getId());
            staffextVO.setPassword(null);

            result.put("staffInfo",
                    userService.getUserInfo(user.getId(), user.getTenantId()));
            result.put("code", 200);
            String userId = String.valueOf(user.getId());
            /**
             * 初始化好友未读消息列表
             */
            List<ImUser> friends = imUserFriendService.getFriendsList(userId);
            /**
             * 初始化用户群组未读消息
             */
            List<ImChatGroup> chatGroups = imUserService.getChatGroups(userId);
            //初始化用户信息列表
            result.put("friends", friends);
            result.put("user", user);
            result.put("groups", chatGroups);
            return R.data(result, "登录成功！");
        }
    }

    /**
     * 根据钉钉id查询用户信息
     * @param ddId
     * @return
     */
    public User getUserByDdId(String ddId){
        User user = chatUserService.getUserByDdId(ddId);
        return user;
    }

    /**
     * 获取用户姓名
     * @param accessToken
     * @param userId
     * @return
     */
    private String getUserName(String accessToken, String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            return response.getName();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        String apUnique = "";
        String  accessToken = AccessTokenUtil.getToken(apUnique);

    }

}


