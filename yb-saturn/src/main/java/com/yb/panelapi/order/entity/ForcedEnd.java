package com.yb.panelapi.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "强制结束参数对象")
public class ForcedEnd implements Serializable {

    @ApiModelProperty(value = "上报id(bfId)", required = true)
    private Integer bid;
    @ApiModelProperty(value = "良品数", required = true)
    private Integer countNum;
    @ApiModelProperty(value = "作业数", required = true)
    private Integer productNum;
    @ApiModelProperty(value = "上上工序废品数")
    private Integer uppwasteNum;
    @ApiModelProperty(value = "上工序废品数")
    private Integer upwasteNum;
    @ApiModelProperty(value = "废品数", required = true)
    private Integer wasteNum;
    @ApiModelProperty(value = "上报类型")
    private Integer reportType;
    @ApiModelProperty(value = "上报人id")
    private Integer reportUserid;
    @ApiModelProperty(value = "执行state的id", required = true)
    private Integer esId;
    @ApiModelProperty(value = "id", required = true)
    private Integer id;
    @ApiModelProperty(value = "设备id", required = true)
    private Integer maId;
    @ApiModelProperty(value = "用户id")
    private String usIds;
    @ApiModelProperty(value = "排产班次id,", required = true)
    private Integer wfId;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    @ApiModelProperty(value = "执行id", required = true)
    private Integer exId;
}
