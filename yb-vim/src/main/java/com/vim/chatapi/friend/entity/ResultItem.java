package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultItem implements Serializable {

    @JsonProperty("To_Account")
    private String To_Account;
    @JsonProperty("ResultCode")
    private Integer ResultCode;
    @JsonProperty("ResultInfo")
    private String ResultInfo;

}
