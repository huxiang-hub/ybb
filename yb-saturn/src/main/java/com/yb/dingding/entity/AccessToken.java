package com.yb.dingding.entity;

import lombok.Data;

/**
 * 获取外部应用返回对象
 */
@Data
public class AccessToken {

    private Integer errcode;
    private String errmsg;
    private String access_token;
    private Integer expires_in;
}
