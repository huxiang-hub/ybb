package com.sso.chatapi.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class IMResult implements Serializable {
    @JsonProperty("ActionStatus")
    private String ActionStatus;
    @JsonProperty("ErrorCode")
    private Integer ErrorCode;
    @JsonProperty("ErrorInfo")
    private String ErrorInfo;
}
