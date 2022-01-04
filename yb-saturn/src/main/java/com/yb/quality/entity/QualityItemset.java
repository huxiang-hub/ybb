package com.yb.quality.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 质量检查内容定义表
 */
@Data
@TableName("yb_quality_itemset")
@ApiModel(value = "yb_quality_itemset", description = "质量检查内容定义表")
public class QualityItemset implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*关联模型id主键*/
    @ApiModelProperty(value = "关联模型id主键")
    private Integer qtId;
    /*工序分类*/
    @ApiModelProperty(value = "工序分类")
    private Integer pyId;
    /*工序分类名称*/
    @ApiModelProperty(value = "工序分类名称")
    private String pyName;
    /*字段总数量*/
    @ApiModelProperty(value = "字段总数量")
    private Integer colNum;
    /*字段定义名称；数据字段名称*/
    @ApiModelProperty(value = "字段定义名称；数据字段名称")
    private String colName;
    /*显示名称*/
    @ApiModelProperty(value = "显示名称")
    private String colShow;
    /*字段类型1：1单选、2多选、3文本*/
    @ApiModelProperty(value = "字段类型1：1单选、2多选、3文本")
    private Integer colType;
    /*字段数据选择用竖线分隔；单选多选选项；默认值*/
    @ApiModelProperty(value = "字段数据选择用竖线分隔；单选多选选项；默认值")
    private String colInfo;
    @ApiModelProperty(value = "字段数据选择用竖线分隔；为选择value值信息")
    private String colVal;
    /*备注说明；注意事项*/
    @ApiModelProperty(value = "备注说明；注意事项")
    private String colDesc;
    /*是否有图片、视频0否1是*/
    @ApiModelProperty(value = "是否有图片、视频0否1是")
    private Integer isImg;
    /*首检 1是、0否*/
    @ApiModelProperty(value = "首检 1是、0否")
    private Integer firstCheck;
    /*巡检 1是、0否*/
    @ApiModelProperty(value = "巡检 1是、0否")
    private Integer roundCheck;
    /*自检 1是、0否*/
    @ApiModelProperty(value = "自检 1是、0否")
    private Integer selfCheck;
    /*状态：0未启用1已生成启用2停用*/
    @ApiModelProperty(value = "状态：0未启用1已生成启用2停用")
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
