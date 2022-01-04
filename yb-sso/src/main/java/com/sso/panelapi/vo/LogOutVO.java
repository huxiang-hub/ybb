package com.sso.panelapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "机台退出登录参数对象")
public class LogOutVO implements Serializable {
    @ApiModelProperty(value = "用户id")
    private Integer usId;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
}
