package com.vim.api.controller;

import com.vim.api.entity.RegisterResult;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//import com.vim.user.service.IImUserService;

/**
 * 注册
 *
 * @author 乐天
 * @since 2018-10-07
 */
@Controller
public class RegisterController {

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;

    /**
     * 用户注册
     *
     * @param name     用户名
     * @param password 密码
     * @param phone    手机
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public RegisterResult register(String name, String password, String phone) {
        RegisterResult registerResult = new RegisterResult();
        if (imUserService.getByLoginName(phone) != null) {
            registerResult.setResultCode(RegisterResult.ERROR);
            registerResult.setMessage("手机号码重复");
        } else {
            try {
                ImUser imUser = new ImUser();
                String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode(password.trim());
                imUser.setPassword(finalSecret);
                imUser.setLoginName(phone);
                imUser.setMobile(phone);
                imUser.setName(name);
                imUser.setAvatar("/img/default-user.png");
                imUserService.registerUser(imUser);
                registerResult.setResultCode(RegisterResult.SUCCESS);
                registerResult.setMessage("SUCCESS");
            } catch (Exception e) {
                registerResult.setResultCode(RegisterResult.ERROR);
                registerResult.setMessage("保存用户失败");
            }
        }
        return registerResult;
    }
}
