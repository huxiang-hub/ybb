package com.yb.quality.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdateOrSaveVO implements Serializable {

    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "表字段名")
    private List<String> fieldNameList;
    @ApiModelProperty(value = "字段值")
    private List<Object> valList;
}
