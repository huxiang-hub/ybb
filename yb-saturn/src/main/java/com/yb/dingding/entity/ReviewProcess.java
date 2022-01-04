package com.yb.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "审核流程返回对象")
public class ReviewProcess implements Serializable {
    @ApiModelProperty(value = "发起人姓名")
    private String originatorUserName;
    @ApiModelProperty(value = "审批状态：NEW：新创建, RUNNING：审批中, TERMINATED：被终止, COMPLETED：完成, CANCELED：取消")
    private String status;
    @ApiModelProperty(value = "任务列表")
    private List<Tasks> tasksList;
    @ApiModelProperty(value = "审核类型1、盘点2、报废、3机长")
    private Integer spMold;

}
