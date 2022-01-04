package com.yb.panelapi.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/16 14:32
 */
@ApiModel("周小时达成率总体损耗请求")
@Data
public class WeekHourRateLossRequest {

    @ApiModelProperty(value = "设备id")
    private Integer maId;

    @ApiModelProperty(value = "设备类型")
    private Integer maType;

    @ApiModelProperty(value = "部门id")
    private Integer dpId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
