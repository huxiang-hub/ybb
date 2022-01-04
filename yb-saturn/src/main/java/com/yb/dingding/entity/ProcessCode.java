package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "获取模板code")
public class ProcessCode implements Serializable {
    private int errcode;
    @JsonProperty("process_code")
    private String processCode;
    private String errmsg;
    @JsonProperty("request_id")
    private String requestId;
}
