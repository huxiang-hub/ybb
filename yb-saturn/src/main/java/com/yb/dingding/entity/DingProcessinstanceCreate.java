package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DingProcessinstanceCreate implements Serializable {
    private int errcode;
    @JsonProperty("process_instance_id")
    private String processInstanceId;
    private String errmsg;
    @JsonProperty("request_id")
    private String requestId;
}
