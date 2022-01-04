package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "排产详情图参数对象")
public class ProductionSchedulingDetailsParam implements Serializable {

    @ApiModelProperty(value = "开始时间")
    private String starTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "班次集合")
    private List<Integer> wsIdList;
    @ApiModelProperty(value = "设备集合")
    private List<Integer> maIdList;
}
