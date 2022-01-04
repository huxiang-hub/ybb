package com.yb.xunyue.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 工单开工请求
 * @Author my
 */
@Data
@ApiModel("工单开工请求")
public class XueYueOpenShiftRequest {

    @ApiModelProperty(value = "工单编号", required = true)
    @NotBlank(message = "工单编号不能为空")
    private String wbNo;

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull(message = "设备id不能为空")
    private Integer maId;

    @ApiModelProperty(value = "班次", required = false)
    //@NotBlank(message = "班次可以为空null ok")
    private Integer classes;

    @ApiModelProperty(value = "操作人", required = false)
    //@NotBlank(message = "操作人id不能为空")
    private String operator;
}
