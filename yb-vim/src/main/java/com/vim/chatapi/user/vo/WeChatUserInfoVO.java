package com.vim.chatapi.user.vo;

import lombok.Data;

@Data
public class WeChatUserInfoVO {

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    private String openid;
    /**
     * 普通用户昵称
     */
    private String nickname;
    /**
     * 普通用户性别，1 为男性，2 为女性
     */
    private int sex;
    /**
     * 普通用户个人资料填写的省份
     */
    private String province;
    /**
     * 普通用户个人资料填写的城市
     */
    private String city;
    /**
     * 国家，如中国为 CN
     */
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132
     * 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
     */
    private String headimgurl;
    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    private String privilege;
    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的
     */
    private String unionid;
}
