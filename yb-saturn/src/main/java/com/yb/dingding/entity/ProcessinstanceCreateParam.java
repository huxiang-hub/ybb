package com.yb.dingding.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ProcessinstanceCreateParam implements Serializable {
    @ApiModelProperty(value = "审核流程模板名称")
    private String templateName;
    @ApiModelProperty(value = "用户的钉钉id")
    private String userId;
    @ApiModelProperty(value = "审核流程模板中的输入框名称和值,key为名称,value为值")
    private Map<String, String> map;
}
