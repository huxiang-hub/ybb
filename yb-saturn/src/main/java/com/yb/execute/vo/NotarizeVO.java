package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "停机确认参数对象")
public class NotarizeVO implements Serializable {
    @ApiModelProperty(value = "需要确认的id")
    private List<Integer> idList;
    @ApiModelProperty(value = "停机理由")
    private String classify;
    @ApiModelProperty(value = "备注")
    private String remake;
    @ApiModelProperty(value = "停机理由")
    private Integer usId;
    @ApiModelProperty(value = "确认标识符 传1")
    private Integer handle;
}
