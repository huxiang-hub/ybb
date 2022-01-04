package com.yb.dingding.entity;

import lombok.Data;

@Data
public class SuiteAccessToken {

    private Integer errcode;
    private String errmsg;
    private String suite_access_token;
    private Integer expires_in;
}
