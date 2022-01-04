package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FormComponentValues implements Serializable {
    @JsonProperty("component_type")
    @ApiModelProperty(value = "组件类型")
    private String componentType;
    @ApiModelProperty(value = "标签名")
    private String name;
    @ApiModelProperty(value = "组件ID")
    private String id;
    @ApiModelProperty(value = "标签值")
    private String value;
    @JsonProperty("ext_value")
    @ApiModelProperty(value = "标签扩展值")
    private String extValue;
    @JsonProperty("main_process_instance_id")
    @ApiModelProperty(value = "主流程实例标识")
    private String mainProcessInstanceId;
}
