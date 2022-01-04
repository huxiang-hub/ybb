package com.yb.quality.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "机台质量巡检选择项对象")
public class OptionVO implements Serializable {

    @ApiModelProperty(value = "数据字段名称")
    private String colName;
    @ApiModelProperty(value = "显示名")
    private String colshow;
    @ApiModelProperty(value = "显示类型:1单选、2多选、3文本")
    private Integer colType;
    @ApiModelProperty(value = "备注说明；注意事项")
    private String colDesc;
    @ApiModelProperty(value = "机台质量巡检选项卡")
    private List<TabControlVO> tabControlVOList;

}
