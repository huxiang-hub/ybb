package com.vim.chatapi.user.controller;

import com.alibaba.fastjson.JSON;
import com.vim.chatapi.friend.entity.IMResult;
import com.vim.chatapi.user.config.MD5Utils;
import com.vim.chatapi.user.config.MsgConfig;
import com.vim.chatapi.user.entity.BaseStaffinfo;
import com.vim.chatapi.user.service.IChatUserService;
import com.vim.chatapi.user.vo.BaseStaffextVO;
import com.vim.chatapi.user.vo.PWModel;
import com.vim.chatapi.user.vo.UserDto;
import com.vim.chatapi.user.vo.UserVO;
import com.vim.chatapi.utils.IMUpdateUserUtil;
import com.vim.chatapi.utils.ObjectMapperUtil;
import com.vim.chatapi.utils.QrCode;
import com.vim.message.service.IImMessageService;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImUserFriendService;
import com.vim.user.service.IImUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.system.feign.IChatSysTenantClient;
import org.springblade.system.user.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author qinbo
 */
@RestController
@AllArgsConstructor
@Api(value = "用户接口", tags = "用户接口")
@RequestMapping("api/user")
public class ChatUserController {
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
    public R loginByAccount(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        user = chatUserService.selectUser(user);
        if (user == null) {
            return R.fail(REQUEST_F, "账号密码不正确！");
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
        String status = MsgConfig.sendMessages(phoneNum, code.toString()); //下发验证码
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

            userVO.setChatNo(chatUserService.getMaxChatNo() + 1);
            userVO.setPhone(redisUtil.get("PHONENUM" + userVO.getPhone()).toString());
            //userVO.setAccount(userVO.getPhone());
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
            user.setPassword(MD5Utils.encodeByMD5(userVO.getPassword()));
            chatUserService.updateById(user);
            redisUtil.del("CODE" + userVO.getPhone());
            redisUtil.del("PHONENUM" + userVO.getPhone());
            return R.success("找回密码成功！");
        }


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
        } else if (!MD5Utils.encodeByMD5(pwModel.getOldPassword()).equals(
                chatUserService.getById(pwModel.getUser().getId()).getPassword())) {

            return R.fail("原密码输入有误！");
        } else if (pwModel.getOldPassword().equals(pwModel.getNewPassword())) {
            return R.fail("新密码和旧密码必须不同！");
        } else {
            /**
             * 加密处理
             */
            pwModel.setNewPassword(MD5Utils.encodeByMD5(pwModel.getNewPassword()));
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
        String updateIM;
        try {
            updateIM = IMUpdateUserUtil.updateIM(userVO);//修改IM上的用户信息
        } catch (IOException e) {
            return R.fail("修改失败");
        }
        IMResult iMResult = ObjectMapperUtil.toObject(updateIM, IMResult.class);
        if(iMResult.getErrorCode() != 0){
            return R.fail("修改失败");
        }
        User user =
                chatUserService.getById(userVO.getId());
        //更新user  修改名字 性别等
        user.setSex(userVO.getSex());
        user.setCurraddr(userVO.getCurraddr());
        user.setRealName(userVO.getRealName());
        user.setName(userVO.getName());
        chatUserService.updateById(user);
        BaseStaffinfo staffinfo = null;
        if (userVO.getJobNum() != null) {
            staffinfo =
                    chatUserService.getbaseStaffInfoByJobnNum(userVO.getJobNum());
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
