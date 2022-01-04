package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "钉钉审核流程详情表对象")
public class ProcessinstanceGet implements Serializable {
    @ApiModelProperty(value = "返回码, 0表示成功")
    private int errcode;
    @ApiModelProperty(value = "返回码描述")
    private String errmsg;
    @JsonProperty("process_instance")
    @ApiModelProperty(value = "审批实例详情")
    private ProcessInstance processInstance;
    @JsonProperty("request_id")
    @ApiModelProperty(value = "请求ID。")
    private String requestId;
    @ApiModelProperty(value = "审核类型1、盘点2、报废、3机长")
    private Integer spMold;
}
