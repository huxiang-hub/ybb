package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "执行审批操作带附件参数对象")
public class DingInstanceExecute implements Serializable {

    @JsonProperty("actioner_userid")
    @ApiModelProperty(value = "操作人userid，可通过调用获取审批实例详情接口获取。", required = true)
    private String actionerUserid;
    @JsonProperty("process_instance_id")
    @ApiModelProperty(value = "审批实例id，可通过调用获取审批实例ID列表接口获取", required = true)
    private String processInstanceId;
    @ApiModelProperty(value = "操作评论，可为空")
    private String remark;
    @ApiModelProperty(value = "审批操作，取值:agree：同意, refuse：拒绝", required = true)
    private String result;
    @JsonProperty("task_id")
    @ApiModelProperty(value = "任务节点id，可通过调用获取审批实例详情接口获取", required = true)
    private Long taskId;
    @ApiModelProperty(value = "文件")
    private DingFile file;
}
