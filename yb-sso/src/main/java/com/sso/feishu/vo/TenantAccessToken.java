package com.sso.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书AccessToken返回实体类")
public class TenantAccessToken implements Serializable {
    @ApiModelProperty(value = "状态值, 0为成功")
    private int code;
    @ApiModelProperty(value = "剩余剩余周期")
    private int expire;
    @ApiModelProperty(value = "错误信息")
    private String msg;
    @JsonProperty("tenant_access_token")
    @ApiModelProperty(value = "AccessToken")
    private String tenantAccessToken;
}
