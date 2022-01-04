package com.yb.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_quality_tacitly")
@ApiModel(value = "yb_quality_tacitly", description = "质量默认模型表")
public class QualityTacitly implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*工序分类id*/
    @ApiModelProperty(value = "工序分类id")
    private Integer pyId;
    /*工序分类名称*/
    @ApiModelProperty(value = "工序分类id")
    private String pyName;
    @ApiModelProperty(value = "检查类型: first_check 首检、round_check 巡检、self_check 自检")
    private String checkType;
    @ApiModelProperty(value = "表名")
    private String tabName;
    /*表名前缀：默认：yb_quality_*/
    @ApiModelProperty(value = "表名前缀：默认：yb_quality_")
    private String tabPrefix;
    /*默认填写字段：用竖线分隔|*/
    @ApiModelProperty(value = "默认填写字段：用竖线分隔|")
    private String colDefault;
    /*状态：0停用、1启用*/
    @ApiModelProperty(value = "状态：0停用、1启用")
    private Integer status;
    /*操作人*/
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    /*创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /*更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
