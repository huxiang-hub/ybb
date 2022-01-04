package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "查询停机参数对象")
public class ExecuteFaultRequest implements Serializable {

    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    @ApiModelProperty(value = "设备id集合")
    private List<Integer> maIdList;
    @ApiModelProperty(value = "停机类型")
    private String classify;
}
