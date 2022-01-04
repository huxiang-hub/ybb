package com.vim.chatapi.user.controller;
import com.alibaba.fastjson.JSON;
import com.vim.chatapi.user.constant.Constants;
import com.vim.chatapi.user.service.IChatUserService;
import com.vim.chatapi.user.vo.AuthResult;
import com.vim.chatapi.user.vo.UserVO;
import com.vim.chatapi.user.vo.WeChatUserInfoVO;
import com.vim.chatapi.utils.HttpClientUtils;
import com.vim.chatapi.utils.URLEncodeUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器
 *
 * @author qinbo
 */
@RestController
@AllArgsConstructor
@Api(value = "微信登录接口", tags = "微信登录微信登录接口")
@RequestMapping("api/weChat/")
public class WeChatLoginController {
    private IChatUserService chatUserService;
    private Constants constants;
    private ChatUserController chatUser;
    Map<String,Object> qqProperties = new HashMap<>();
    /**
     * 根据openId获取用户信息
     */
    @RequestMapping("getUserInfo")
    @ResponseBody
    public R getUserInfo(AuthResult authResult) throws Exception {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?");
        url.append("access_token="+authResult.getAccess_token());
        url.append("&openid="+authResult.getOpenid());
        String result = HttpClientUtils.get(url.toString(),"UTF-8");
        Object json = JSON.parseObject(result,WeChatUserInfoVO.class);
        //获取了微信得个人信息
        WeChatUserInfoVO weChatUserInfo = (WeChatUserInfoVO)json;
        Map<String, Object> map = new HashMap<>();
        map.put("unionid",weChatUserInfo.getUnionid());
        User user = new User();
        user.setUnionid(authResult.getUnionid());
        R loginCode = chatUser.loginByAccount(user); //调用登录接口
        // 通过Unionid 没有找到此人，
        // 说明第一次微信登录
        if (loginCode.getCode()==ChatUserController.REQUEST_F) {
            //注册此人信息，保存unionid以便下次登录依据
            user = new User();
            user.setRealName(weChatUserInfo.getNickname());//姓名
            user.setSex(weChatUserInfo.getSex());//姓别
            user.setAvatar(weChatUserInfo.getHeadimgurl());//头像
            user.setCurraddr(weChatUserInfo.getCity()+"-"+weChatUserInfo.getProvince());//加入地址
            user.setUnionid(weChatUserInfo.getUnionid());
            chatUserService.save(user);
        }else {
            return loginCode;
        }
        return R.data(user,"登录成功！");
    }
}
