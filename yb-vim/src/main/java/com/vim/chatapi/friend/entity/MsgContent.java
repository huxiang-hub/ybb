package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MsgContent implements Serializable {

    @JsonProperty("Data")
    private String Data;
    @JsonProperty("Desc")
    private String Desc;
}
