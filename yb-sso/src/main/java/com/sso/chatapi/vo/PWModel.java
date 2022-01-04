package com.sso.chatapi.vo;


import com.sso.chatapi.entity.User;
import lombok.Data;

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
