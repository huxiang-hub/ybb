package com.yb.machine.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 设备状态变化vo
 * @Author my
 */
@ApiModel("设备状态变化vo")
@Data
public class MachineStatusChangePortVO {

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("总产量")
    private Integer numberOfDay;

    @JsonIgnore
    private Integer diffTime;

    @ApiModelProperty("状态(1.运行，2.停机，4离线)")
    private String status;
}
