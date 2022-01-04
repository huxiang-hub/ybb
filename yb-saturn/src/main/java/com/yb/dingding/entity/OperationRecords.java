package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "操作记录列表。")
public class OperationRecords implements Serializable {
    @ApiModelProperty(value = "操作时间")
    private String date;
    @JsonProperty("operation_type")
    @ApiModelProperty(value = "操作类型：EXECUTE_TASK_NORMAL：正常执行任务, EXECUTE_TASK_AGENT：代理人执行任务, APPEND_TASK_BEFORE：前加签任务, APPEND_TASK_AFTER：后加签任务, REDIRECT_TASK：转交任务, START_PROCESS_INSTANCE：发起流程实例, TERMINATE_PROCESS_INSTANCE：终止(撤销)流程实例, FINISH_PROCESS_INSTANCE：结束流程实例, ADD_REMARK：添加评论, redirect_process：审批退回")
    private String operationType;
    @JsonProperty("operation_result")
    @ApiModelProperty(value = "操作结果：AGREE：同意, REFUSE：拒绝, NONE")
    private String operationResult;
    @ApiModelProperty(value = "操作人userid")
    private String userid;
    @ApiModelProperty(value = "评论内容。审批操作附带评论时才返回该字段。")
    private String remark;
}
