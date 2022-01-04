package com.sso.chatapi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.chatapi.constant.Constants;
import com.sso.chatapi.entity.ImChatGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.User;
import com.sso.chatapi.service.IChatUserService;
import com.sso.chatapi.service.IImUserFriendService;
import com.sso.chatapi.service.IImUserService;
import com.sso.chatapi.vo.AuthResult;
import com.sso.chatapi.vo.UserVO;
import com.sso.mapper.SaUserMapper;
import com.sso.panelapi.vo.WeChatUserInfoVO;
import com.sso.system.entity.SaUser;
import com.sso.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sso.chatapi.controller.ChatUserController.REQUEST_OK;

/**
 * 控制器
 *
 * @author qinbo
 */
@RestController
@AllArgsConstructor
@Api(value = "微信登录接口", tags = "微信登录微信登录接口")
@RequestMapping("weChat")
public class WeChatLoginController {
    private IChatUserService chatUserService;
    private Constants constants;
    private ChatUserController chatUser;
    Map<String, Object> qqProperties = new HashMap<>();
    private SaUserMapper saUserMapper;
    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;
    private IImUserFriendService imUserFriendService;
    private IChatUserService userService;
    /**
     * 根据openId获取用户信息
     */
    @RequestMapping("getUserInfo")
    @ResponseBody
    public R getUserInfo(AuthResult authResult) throws Exception {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?");
        url.append("access_token=" + authResult.getAccess_token());
        url.append("&openid=" + authResult.getOpenid());
        String result = HttpClientUtils.get(url.toString(), "UTF-8");
        Object json = JSON.parseObject(result, WeChatUserInfoVO.class);
        //获取了微信得个人信息
        WeChatUserInfoVO weChatUserInfo = (WeChatUserInfoVO) json;
        Map<String, Object> map = new HashMap<>();
        map.put("unionid", weChatUserInfo.getUnionid());
        UserVO user = new UserVO();
        user.setUnionid(authResult.getUnionid());

        Map<String, Object> resultMap = new HashMap<>();

        R loginCode = chatUser.loginByAccount(user); //调用登录接口
        // 通过Unionid 没有找到此人，
        // 说明第一次微信登录
        if (loginCode.getCode() == ChatUserController.REQUEST_F) {
            User user2 = chatUserService.getBaseMapper().selectOne(new QueryWrapper<User>().eq("unionid", weChatUserInfo.getUnionid()));
            if (user2 == null) {
                //注册此人信息，保存unionid以便下次登录依据
                user = new UserVO();
                user.setRealName(weChatUserInfo.getNickname());//姓名
                user.setSex(weChatUserInfo.getSex());//姓别
                user.setAvatar(weChatUserInfo.getHeadimgurl());//头像
                user.setCurraddr(weChatUserInfo.getCity() + "-" + weChatUserInfo.getProvince());//加入地址
                user.setUnionid(weChatUserInfo.getUnionid());
                chatUserService.save(user);
            }
        } else {
            return loginCode;
        }
        resultMap.put("user", user);
        List<ImUser> friends = imUserFriendService.getFriendsList(String.valueOf(user.getId()));
        resultMap.put("friends", friends);
         /**
         * 初始化用户群组未读消息
         */
        List<ImChatGroup> chatGroups = imUserService.getChatGroups(String.valueOf(user.getId()));
        resultMap.put("groups", chatGroups);
        resultMap.put("staffInfo",
                userService.getUserInfo(user.getId(), user.getTenantId()));
        resultMap.put("code", REQUEST_OK);
        return R.data(resultMap, "登录成功！");
    }
}
