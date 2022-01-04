package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author my
 */
@Data
@ApiModel("工单工序进度请求")
public class OrderProcessScheduleRequest {

    @ApiModelProperty(value = "工序id（非必传）", required = false)
    private Integer prId;

    @ApiModelProperty(value = "工单号（非必传）", required = false)
    private String wbNo;

    @ApiModelProperty(value = "工单唯一标识（必填）", required = true)
    @NotBlank(message = "工单唯一标识不能为空")
    private String sdId;
}
