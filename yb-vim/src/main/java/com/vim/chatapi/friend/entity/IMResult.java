package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IMResult implements Serializable {
    @JsonProperty("ResultItem")
    private List<ResultItem> ResultItem;
    @JsonProperty("ActionStatus")
    private String ActionStatus;
    @JsonProperty("ErrorCode")
    private Integer ErrorCode;
    @JsonProperty("ErrorInfo")
    private String ErrorInfo;
    @JsonProperty("ErrorDisplay")
    private String ErrorDisplay;
    @JsonProperty("Fail_Account")
    private List<String> Fail_Account;
}
