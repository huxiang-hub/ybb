package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 工单清单请求信息
 * @Author my
 */
@Data
@ApiModel("工单清单请求信息")
public class OrderCheckListRequest {

    @ApiModelProperty(value = "排产单号（必传）", required = true)
    @NotNull(message = "排产单号")
    private Integer wfId;
}
