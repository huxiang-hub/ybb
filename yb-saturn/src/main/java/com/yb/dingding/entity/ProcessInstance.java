package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "审批实例详情")
public class ProcessInstance implements Serializable {

    @JsonProperty("form_component_values")
    @ApiModelProperty(value = "表单详情列表")
    private List<FormComponentValues> formComponentValues;
    @JsonProperty("create_time")
    @ApiModelProperty(value = "开始时间")
    private String createTime;
    @JsonProperty("attached_process_instance_ids")
    @ApiModelProperty(value = "审批附属实例列表，当已经通过的审批实例被修改或撤销，会生成一个新的实例，作为原有审批实例的附属。如果想知道当前已经通过的审批实例的状态，可以依次遍历它的附属列表，查询里面每个实例的biz_action。")
    private List<String> attachedProcessInstanceIds;
    @JsonProperty("originator_dept_name")
    @ApiModelProperty(value = "发起部门")
    private String originatorDeptName;
    @JsonProperty("originator_userid")
    @ApiModelProperty(value = "发起人的userid")
    private String originatorUserid;
    @ApiModelProperty(value = "审批实例标题")
    private String title;
    @ApiModelProperty(value = "审批结果：agree：同意, refuse：拒绝")
    private String result;
    @JsonProperty("originator_dept_id")
    @ApiModelProperty(value = "发起人的部门。-1表示根部门")
    private String originatorDeptId;
    @ApiModelProperty(value = "审批实例业务编号")
    @JsonProperty("business_id")
    private String businessId;
    @ApiModelProperty(value = "任务列表")
    private List<Tasks> tasks;
    @JsonProperty("biz_action")
    @ApiModelProperty(value = "审批实例业务动作：MODIFY：表示该审批实例是基于原来的实例修改而来,REVOKE：表示该审批实例是由原来的实例撤销后重新发起的,NONE表示正常发起")
    private String bizAction;
    @JsonProperty("operation_records")
    @ApiModelProperty(value = "操作记录列表")
    private List<OperationRecords> operationRecords;
    @ApiModelProperty(value = "审批状态：NEW：新创建, RUNNING：审批中, TERMINATED：被终止, COMPLETED：完成, CANCELED：取消")
    private String status;
    @JsonProperty("finish_time")
    @ApiModelProperty(value = "结束时间")
    private String finishTime;

}
