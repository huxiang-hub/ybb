package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "机台停机列表返回对象", description = "机台停机列表返回对象")
public class ExecuteFaultMachineVO implements Serializable {

    @ApiModelProperty(value = "区分不同时间段")
    private String dtValue;
    @ApiModelProperty(value = "分段停机时间")
    private Integer allStopTime;
    @ApiModelProperty(value = "分段停机次数")
    private Integer allStopNum;
    @ApiModelProperty(value = "分段未确认数")
    private Integer unconfirmedNum;
    @ApiModelProperty(value = "停机列表")
    private List<FaultMachineVO> executeFaultList;
}
