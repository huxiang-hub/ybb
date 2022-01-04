package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "排产饱和度返回对象")
public class SaturabilityVO implements Serializable {

    @ApiModelProperty(value = "日期:天")
    private Integer day;
    @ApiModelProperty(value = "计划耗时(分)")
    private Integer planTotalTime;
    @ApiModelProperty(value = "可用时长(分)")
    private Integer TotalTime;
    @ApiModelProperty(value = "排产日期")
    private String sdDate;
    @ApiModelProperty(value = "饱和度")
    private Double Saturability;
}
