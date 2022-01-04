package com.yb.statis.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "获取设备班次达成率参数对象")
public class GetShiftreachVO implements Serializable {
    @ApiModelProperty(value = "勾选的设备id")
    private List<Integer> maIdList;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
