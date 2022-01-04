package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "执行审批操作带附件返回对象")
public class InstanceExecuteResult implements Serializable {
    private int errcode;
    private String errmsg;
    private boolean result;
    @JsonProperty("request_id")
    private String requestId;
}
