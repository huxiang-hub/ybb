package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "获取已上报数据对象")
public class ReportedVO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "日期")
    private String targetDay;
    @ApiModelProperty(value = "客户名称")
    private String cmName;
    @ApiModelProperty(value = "班次名称")
    private String wsName;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    @ApiModelProperty(value = "准备-数量（系统记录）")
    private Integer readyNum;
    @ApiModelProperty(value = "盒子计数")
    private Integer boxNum;
    @ApiModelProperty(value = "上报作业数")
    private Integer productNum;
    @ApiModelProperty(value = "良品数")
    private Integer countNum;
    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;
    @ApiModelProperty(value = "上报人")
    private String usName;
    @ApiModelProperty(value = "审核状态 0 未审核 1 通过 2未通过")
    private Integer exStatus;
    @ApiModelProperty(value = "是否处理上报事件0未上报1已上报")
    private Integer handle;
    @ApiModelProperty(value = "托盘总数")
    private Integer totalNum;
    @ApiModelProperty(value = "上报时间")
    private Date handleTime;
    @ApiModelProperty(value = "结束时间（结束时间）")
    private Date endTime;
    @ApiModelProperty(value = "开始时间（设备工单开始时间接单B1）")
    private Date startTime;
//    @ApiModelProperty(value = "审核进度id")
//    private String processInstanceId;
//    @ApiModelProperty(value = "完成率")
//    private Double finishingRate;
    @ApiModelProperty(value = "执行单id")
    private Integer exId;
    @ApiModelProperty(value = "审核进度id集合")
    private List<ProcessInstanceVO> processInstanceIdList;
}
