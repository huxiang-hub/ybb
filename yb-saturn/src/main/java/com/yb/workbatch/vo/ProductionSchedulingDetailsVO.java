package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "排产详情图返回对象")
public class ProductionSchedulingDetailsVO implements Serializable {

    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "吃饭时间")
    private List<MealOneTimeVO> mealOneTimeVOList;
    @ApiModelProperty(value = "设备名称")
    private List<WorkbatchShiftDetailVO> workbatchShiftDetailVOList;
}
