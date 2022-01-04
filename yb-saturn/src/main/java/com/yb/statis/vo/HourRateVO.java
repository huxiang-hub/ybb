package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "获取小时达成率列表参数对象")
public class HourRateVO implements Serializable {

    @ApiModelProperty(value = "勾选的设备id")
    private List<Integer> maIdList;
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd)")
    private String startTime;
    @ApiModelProperty(value = "结束时间(yyyy-MM-dd)")
    private String endTime;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
}
