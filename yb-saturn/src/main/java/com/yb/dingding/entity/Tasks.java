package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "任务列表")
public class Tasks implements Serializable {
    @JsonProperty("task_status")
    @ApiModelProperty(value = "任务状态：NEW:未启动, RUNNING：处理中, PAUSED：暂停, CANCELED：取消, COMPLETED：完成, TERMINATED：终止")
    private String taskStatus;
    @JsonProperty("create_time")
    @ApiModelProperty(value = "开始时间")
    private String createTime;
    @JsonProperty("activity_id")
    @ApiModelProperty(value = "")
    private String activityId;
    @JsonProperty("task_result")
    @ApiModelProperty(value = "结果：AGREE：同意, REFUSE：拒绝, REDIRECTED：转交")
    private String taskResult;
    @ApiModelProperty(value = "任务处理人")
    private String userid;
    @ApiModelProperty(value = "任务节点ID")
    private String taskid;
    @ApiModelProperty(value = "任务URL")
    private String url;
    @JsonProperty("finish_time")
    @ApiModelProperty(value = "结束时间")
    private String finishTime;
    @ApiModelProperty(value = "审核人姓名")
    private String reviewUserName;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "执行状态: 0 等待审核, 1 同意, 2 拒绝")
    private Integer status;
}
