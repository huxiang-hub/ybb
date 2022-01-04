package com.sso.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书登录返回对象")
public class FeiShuLogin implements Serializable {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("avatar_thumb")
    private String avatarThumb;
    @JsonProperty("avatar_middle")
    private String avatarMiddle;
    @JsonProperty("avatar_big")
    private String avatarBig;
    @JsonProperty("expires_in")
    private int expiresIn;
    private String name;
    @JsonProperty("en_name")
    private String enName;
    @JsonProperty("open_id")
    private String openId;
    @JsonProperty("tenant_key")
    private String tenantKey;
    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
}
