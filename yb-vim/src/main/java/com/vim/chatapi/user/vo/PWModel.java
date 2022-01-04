package com.vim.chatapi.user.vo;

import lombok.Data;
import org.springblade.system.user.entity.User;

@Data
public class PWModel {
    /**
     * 当前登录的用户对象
     */
    private User user;
    /**
     * 用户的密码
     */
    private String oldPassword;
    /**
     * 用户修改的新密码
     */
    private String newPassword;
    /**
     * 新密码确认密码
     */
    private String newPassword1;
}
