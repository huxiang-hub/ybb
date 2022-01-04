package com.yb.panelapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderAcceptRequest {

    @ApiModelProperty(value = "设备id(必传)", required = true)
    @NotBlank(message = "设备id不能为空")
    Integer maId;
    @ApiModelProperty(value = "设备id(必传)", required = true)
    @NotBlank(message = "开工的工单唯一标识wfId notnull")
    Integer wfId;
    @ApiModelProperty(value = "设备id(必传)", required = false)
    Integer usId;
}
