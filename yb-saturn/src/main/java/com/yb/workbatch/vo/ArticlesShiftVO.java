package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "在制品班次排产相关信息")
public class ArticlesShiftVO implements Serializable {
    @ApiModelProperty(value = "排产班次id")
    private Integer wfId;
    @ApiModelProperty(value = "排产id")
    private Integer sdId;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "排产班次计划开始时间")
    private Date proBeginTime;
    @ApiModelProperty(value = "计划数")
    private Integer planNum;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "作业数")
    private Integer productNum;
    @ApiModelProperty(value = "良品数")
    private Integer countNum;
    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;
}
