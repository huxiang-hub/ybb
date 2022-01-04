package com.yb.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "创建群参数对象")
public class DingChatCreateParam implements Serializable {
    @ApiModelProperty(value = "群名")
    private String name;
    @ApiModelProperty(value = "群主的userid，")
    private String owner;
    @ApiModelProperty(value = "群成员列表(成员id)")
    private List<String> useridlist;
}
