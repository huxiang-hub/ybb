package com.sso.chatapi.controller;


import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.sso.base.entity.BaseStaffinfo;
import com.sso.chatapi.entity.IMImprot;
import com.sso.chatapi.entity.ImChatGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.User;
import com.sso.chatapi.message.service.IImMessageService;
import com.sso.chatapi.service.IChatUserService;
import com.sso.chatapi.service.IImUserFriendService;
import com.sso.chatapi.service.IImUserService;
import com.sso.chatapi.utils.HttpClientUtils;
import com.sso.chatapi.utils.IMResult;
import com.sso.chatapi.utils.ImMap;
import com.sso.chatapi.utils.ObjectMapperUtil;
import com.sso.chatapi.vo.BaseStaffextVO;
import com.sso.chatapi.vo.PWModel;
import com.sso.chatapi.vo.UserDto;
import com.sso.chatapi.vo.UserVO;
import com.sso.dingding.config.URLConstant;
import com.sso.dingding.util.AccessTokenUtil;
import com.sso.panelapi.config.MsgConfig;
import com.sso.utils.QrCode;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.system.feign.IChatSysTenantClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Api(value = "用户接口", tags = "用户接口")
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class ChatUserController {
    private IChatSysTenantClient sysTenantClient;
    private IChatUserService chatUserService;
    private RedisUtil redisUtil;
    static final Integer REQUEST_F = 400; // 定义请求的的code
    static final Integer REQUEST_OK = 200; // 定义请求的的code
    static final Integer SESSION_TIME = 600;//定义验证码失效时间
    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;
    private IImUserFriendService imUserFriendService;
    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService messageService;

    private IChatUserService userService;

    /**
     * 账号密码登录
     */
    @ApiOperation(value = "账号密码登录", tags = "手机，密码登录")
    @PostMapping("/loginByAccount")
    @ResponseBody
    public R loginByAccount(@RequestBody UserVO userVO) {
        Map<String, Object> result = new HashMap<>();
        log.info("印聊登录信息:account:{}, password:{}", userVO.getPhone(), userVO.getPassword());
        String authCode = userVO.getAuthCode();
        String apUnique = userVO.getApUnique();
        User user = null;
        if(!StringUtil.isEmpty(authCode)){//先查询是否是通过钉钉直接登录
            String ddId = getDdId(authCode, apUnique);
            if(!StringUtil.isEmpty(ddId)){
                user = getUserByDdId(ddId);
                if (user == null) {
                    return R.fail(REQUEST_F, "该用户不存在！");
                }
            }
        }else {
            user = chatUserService.selectUser(userVO);
            if (user == null) {
                return R.fail(REQUEST_F, "账号密码不正确！");
            }
        }
        user.setPassword(null);
        BaseStaffextVO staffextVO =
                chatUserService.getBaseStaffextVO(user.getId());
        staffextVO.setPassword(null);

        result.put("staffInfo",
                userService.getUserInfo(user.getId(), user.getTenantId()));
        result.put("code", REQUEST_OK);
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
     * 获取钉钉当前登录用户的钉钉id
     * @param authCode
     * @return
     */
    public String getDdId(String authCode, String apUnique){
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken(apUnique);

        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(authCode);
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
        return response.getUserid();
    }

    /***
     * 手机注册
     * @param phoneNum
     * @return
     */
    @ApiOperation(value = "手机验证码注册")
    @PostMapping("/getPhoneCode")
    @ResponseBody
    public R getPhoneCode(String phoneNum, Integer flag) {
        if (flag != null) {
            if (chatUserService.getUserByPhone(phoneNum) != null) {

                return R.fail(REQUEST_F, "此号码已经有人注册过了");
            }
        }
        if (flag == null) {
            if (chatUserService.getUserByPhone(phoneNum) == null) {

                return R.fail(REQUEST_F, "此号码未注册");
            }
        }
        //获取四位数验证码
        StringBuffer code = getSsmCode();
        com.sso.utils.R status = MsgConfig.sendMessages(phoneNum, code.toString()); //下发验证码
        Map<String, Object> result = new HashMap<>();
        redisUtil.set("CODE" + phoneNum, code, SESSION_TIME); /*设置验证码10分钟无效*/
        redisUtil.set("PHONENUM" + phoneNum, phoneNum, SESSION_TIME); /*设置验证码10分钟无效*/
        result.put("code", code);
        result.put("status", status);
        result.put("phoneNum", phoneNum);
        return R.data(result, "验证码已下发！");
    }

    /**
     * 生成一个四位的随机数
     *
     * @return
     */
    private StringBuffer getSsmCode() {
        StringBuffer code = new StringBuffer("");

        for (int i = 0; i < 5; i++) {
            int num = (int) (Math.random() * 10);
            if (num == 0) {
                num += 1;
            }
            code.append(num);
        }
        return code;
    }

    /**
     * 提交短信验证码，在数据库保存User
     * author qinbo
     * date 2020/3/23
     *
     * @return
     */
    @PostMapping("/changeInfo")
    @ApiOperation(value = "验证验证码", notes = "传入验证密码")
    @ResponseBody
    public R changeInfo(@RequestBody UserVO userVO) {//user里面至少包含一个phone,password
        //验证是否正确，是否超时间
        if (redisUtil.get("CODE" + userVO.getPhone()) == null ||
                !userVO.getCode().equals(redisUtil.get("CODE" + userVO.getPhone()).toString()) ||
                redisUtil.get("PHONENUM" + userVO.getPhone().toString()) == null ||
                !userVO.getPhone().equals(redisUtil.get("PHONENUM" + userVO.getPhone()))) {
            return R.fail("验证失败！");
        }

        if (userVO.getFlag() == 0) { //   手机注册

//            userVO.setChatNo(chatUserService.getMaxChatNo() + 1);
            userVO.setPhone(redisUtil.get("PHONENUM" + userVO.getPhone()).toString());
            //userVO.setAccount(userVO.getPhone());
//            userVO.setChatNo(Integer.valueOf(userVO.getPhone()));//印聊号
            String avatar = "/static/img/default_avatar.jpg";
            userVO.setAvatar(avatar);//默认头像
            IMImprot iMImprot = new IMImprot();
            userVO.setName("印聊用户"+userVO.getPhone());
            iMImprot.setIdentifier(userVO.getPhone());//IM用户名
            iMImprot.setNick(userVO.getName());//IM用户呢称
            iMImprot.setFaceUrl(avatar);//IM头像
            String importIM;
            try {
                importIM = importIM(iMImprot);
            } catch (Exception e) {
                return R.fail("注册失败！");
            }
            IMResult imResult = ObjectMapperUtil.toObject(importIM, IMResult.class);
            Integer errorCode = imResult.getErrorCode();
            if(errorCode != 0){
                return R.fail("注册失败！");
            }
            chatUserService.saveBladeUser(userVO);

            redisUtil.del("CODE" + userVO.getPhone());
            redisUtil.del("PHONENUM" + userVO.getPhone());
            return R.success("手机注册成功！");

        } else if (userVO.getFlag() == 1) { //修改手机号

            chatUserService.updateById(userVO);
            redisUtil.del("CODE" + userVO.getPhone());
            redisUtil.del("PHONENUM" + userVO.getPhone());
            return R.success("修改手机号码成功！");
        } else { // 找回密码

            /*当前登录人的Id 通过用户接收验证码的手机号*/
            User user =
                    chatUserService.getUserByPhone(
                            redisUtil.get("PHONENUM" + userVO.getPhone()).toString());
            //修改密码之前先加密
            user.setPassword(DigestUtil.encrypt(userVO.getPassword()));
            chatUserService.updateById(user);
            redisUtil.del("CODE" + userVO.getPhone());
            redisUtil.del("PHONENUM" + userVO.getPhone());
            return R.success("找回密码成功！");
        }


    }

    /**
     * IM导入用户
     *
     * @return
     */
    public String importIM(IMImprot iMImprot) throws Exception {
        Map<String, String> mapIM = ImMap.getImMap();
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?";
        return HttpClientUtils.doPost(url, iMImprot, mapIM);
    }

    /**
     * 修改密码
     *
     * @param pwModel
     * @return
     */
    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改密码", notes = "传入密码")
    @ResponseBody
    public R updatePassword(@RequestBody PWModel pwModel) {
        if (!pwModel.getNewPassword().equals(pwModel.getNewPassword1())) {
            return R.fail("两次密码不一致！");
        } else if (!DigestUtil.encrypt(pwModel.getOldPassword()).equals(
                chatUserService.getById(pwModel.getUser().getId()).getPassword())) {

            return R.fail("原密码输入有误！");
        } else if (pwModel.getOldPassword().equals(pwModel.getNewPassword())) {
            return R.fail("新密码和旧密码必须不同！");
        } else {
            /**
             * 加密处理
             */
            pwModel.setNewPassword(DigestUtil.encrypt(pwModel.getNewPassword()));
            pwModel.getUser().setPassword(pwModel.getNewPassword());
            chatUserService.updateById(pwModel.getUser());
            return R.success("修改密码成功！");
        }

    }

    /**
     * 获取用户扩展信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(Integer userId, String tenantId) {
        UserDto user = userService.getUserInfo(userId, tenantId);
        return R.data(user);
    }

    /**
     * 获取用户信息二维码
     */
    @PostMapping("/getUserQrCode")
    public R getUserQrCode(@RequestBody ImUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", user);
        map.put("flag", "yb-user");
        String qrCode = null;
        try {
            qrCode = QrCode.createQrCode(JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.data(qrCode);
    }

    /**
     * 修改个人中心得信息
     */
    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "修改个人信息", notes = "")
    @ResponseBody
    public R updateUserInfo(@RequestBody UserVO userVO) {
        User user =
                chatUserService.getById(userVO.getId());
        //更新user  修改名字 性别等
        user.setSex(userVO.getSex());
        user.setCurraddr(userVO.getCurraddr());
        chatUserService.updateById(user);
        user.setRealName(userVO.getRealName());
        user.setName(userVO.getName());
        BaseStaffinfo staffinfo = null;
        if (userVO.getJobNum() != null) {
            staffinfo =  chatUserService.getbaseStaffInfoByJobnNum(userVO.getJobNum());
        }
        // 判断是否有这个工号,姓名，有才能把userId和 staffinfo 关联
        if (staffinfo != null) {
            if (userVO.getTenantId() != null) {
                if (staffinfo.getJobnum().equals(userVO.getJobNum()) &&
                        staffinfo.getName().equals(userVO.getRealName())) {
                    chatUserService.updateBaseSatffinfo(userVO.getJobNum(),
                            userVO.getId());
                } else {
                    return R.fail("名字或者工号不匹配！");
                }
            } else {
                return R.fail("公司信息为空！");
            }
        }
        return R.success("修改成功！");
    }


    /* 根据用户id  查找用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/selectUserInfo")
    public R getUserInfo(Integer userId) {
        User user = userService.selectUserInfo(userId);
        return R.data(user);
    }


}
