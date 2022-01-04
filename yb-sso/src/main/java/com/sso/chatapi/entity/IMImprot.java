package com.sso.chatapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class IMImprot implements Serializable {

    @JsonProperty("Identifier")
    private String Identifier;//用户名
    @JsonProperty("Nick")
    private String Nick;//用户呢称
    @JsonProperty("FaceUrl")
    private String FaceUrl;//头像
}
