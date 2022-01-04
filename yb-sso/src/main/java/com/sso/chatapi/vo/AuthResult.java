package com.sso.chatapi.vo;

import lombok.Data;

@Data
public class AuthResult {

    private String access_token;
    private Integer expires_in;
    private String openid;
    private String scope;
    private String unionid;
}

