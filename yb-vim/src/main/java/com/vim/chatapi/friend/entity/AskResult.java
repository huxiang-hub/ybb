package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AskResult implements Serializable {
    @JsonProperty("ActionStatus")
    private String ActionStatus;
    @JsonProperty("ErrorInfo")
    private String ErrorInfo;
    @JsonProperty("ErrorCode")
    private Integer ErrorCode;
    @JsonProperty("MsgTime")
    private Integer MsgTime;
    @JsonProperty("MsgKey")
    private String MsgKey;
}
