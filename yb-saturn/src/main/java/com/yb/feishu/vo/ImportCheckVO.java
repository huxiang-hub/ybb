package com.yb.feishu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "打卡流水记录")
public class ImportCheckVO implements Serializable {
    @ApiModelProperty(value = "打卡时间，精确到秒的时间戳", required = true)
    private long checkTime;
    @ApiModelProperty(value = "打卡备注")
    private String comment;
    @ApiModelProperty(value = "打卡地点")
    private String checkLocationName;
}
