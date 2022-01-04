package com.sso.chatapi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 免密登录
 */
@Data
public class AuthCode implements Serializable {
    @ApiModelProperty(value = "免登码code")
    private String authCode;
    @ApiModelProperty(value = "应用标识符")
    private String apUnique;
    @ApiModelProperty(value = "应用标识符")
    private String apDomain;
}
