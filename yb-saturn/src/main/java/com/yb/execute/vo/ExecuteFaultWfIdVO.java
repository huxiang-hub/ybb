package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "设备停机排产单分类对象", description = "设备停机排产单分类对象")
public class ExecuteFaultWfIdVO implements Serializable {
    @ApiModelProperty(value = "需要主动上报的id")
    private Integer id;
    @ApiModelProperty(value = "排产班次id")
    private Integer wfId;
    @ApiModelProperty(value = "总停机时长")
    private Integer totalDowntime;
    @ApiModelProperty(value = "总停机次数")
    private Integer totalDownNum;
    @ApiModelProperty(value = "客户")
    private String cmName;
//    @ApiModelProperty(value = "用户名")
//    private String userName;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工单")
    private String wbNo;
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    @ApiModelProperty(value = "已完成数量")
    private Integer finishNum;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "机台停机列表对象")
    private List<ExecuteFaultMachineVO> executeFaultMachineVOList;
}
