package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "机台上报列表详情对象")
public class ExecuteBrieferDetailVO implements Serializable {

    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "上报总数")
    private String brieferNum;
    @ApiModelProperty(value = "废品数")
    private Integer countNum;
    @ApiModelProperty(value = "作业数")
    private Integer productNum;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "机长")
    private String usName;
    @ApiModelProperty(value = "班次名称")
    private String wsName;
    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;
    @ApiModelProperty(value = "日期")
    private String exDate;
    @ApiModelProperty(value = "执行开始时间")
    private String startTime;
    @ApiModelProperty(value = "执行结束时间")
    private String endTime;
    @ApiModelProperty(value = "总托数")
    private Integer totalNum;
    @ApiModelProperty(value = "审核进度id")
    private String processInstanceId;
    @ApiModelProperty(value = "托盘清单")
    private List<TraycardDetailedVO> traycardDetailedVOList;
}
