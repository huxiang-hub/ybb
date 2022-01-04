package com.yb.quality.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddColsVO implements Serializable {

    @ApiModelProperty(value = "工序分类id")
    /*工序分类id*/
    private Integer pyId;
    @ApiModelProperty(value = "工序分类名称")
    /*工序分类名称*/
    private String pyName;
    @ApiModelProperty(value = "检查类型, first_check 首检、round_check 巡检、self_check 自检")
    /*检查类型*/
    private String checkType;
    @ApiModelProperty(value = "字段名")
    private String colName;
    @ApiModelProperty(value = "显示名称")
    private String colShow;
    @ApiModelProperty(value = "备注说明；注意事项")
    private String colDesc;
    @ApiModelProperty(value = "字段类型1：1单选、2多选、3文本")
    private Integer colType;
    @ApiModelProperty(value = "是否有图片、视频0否1是")
    private Integer isImg;
    @ApiModelProperty(value = "字段数据选择用竖线分隔；单选多选选项；默认值(显示值)")
    private String colInfo;
    @ApiModelProperty(value = "字段数据选择用竖线分隔；为选择value值信息")
    private String colVal;
}
