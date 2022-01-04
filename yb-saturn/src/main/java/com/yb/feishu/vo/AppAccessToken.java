package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppAccessToken implements Serializable {
    private int code;
    private String msg;
    @JsonProperty("app_access_token")
    private String appAccessToken;
    private int expire;
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;
}
