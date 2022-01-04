package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("计划达成率详情信息的请求参数")
public class ShiftReachExeinfoRequest {

    @ApiModelProperty("设备id")
    @NotNull(message = "设备Id不能为空")
    private Integer maId;

    @ApiModelProperty("班次id")
    @NotNull(message = "班次信息不能为空")
    private Integer wsId;

    @ApiModelProperty("日期(必传)")
    @NotBlank(message = "日期不能为空")
    private String targetDay;
}
