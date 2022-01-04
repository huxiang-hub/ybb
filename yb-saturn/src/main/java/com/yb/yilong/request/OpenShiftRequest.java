package com.yb.yilong.request;

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
public class OpenShiftRequest {

    @ApiModelProperty(value = "工单编号", required = true)
    @NotBlank(message = "工单编号不能为空")
    private String wbNo;

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull(message = "设备id不能为空")
    private Integer maId;

    @ApiModelProperty(value = "状态", required = false)
    @NotBlank(message = "状态不能为空;默认为开始 0,开始 1结束")
    private String status = "0";

    @ApiModelProperty(value = "操作人", required = false)
    private String operator;
}
