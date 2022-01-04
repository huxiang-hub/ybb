package com.yb.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("yb_base_classinfo")
@ApiModel(value = "BaseClassinfo对象", description = "班组信息_yb_base_classinfo")
public class BaseClassinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 车间ID
     */
    @ApiModelProperty(value = "车间ID")
    private Integer dpId;
    /**
     * 班组名称
     */
    @ApiModelProperty(value = "班组名称")
    private String bcName;
    /**
     * 班组人数
     */
    @ApiModelProperty(value = "班组人数")
    private Integer bcNum;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 班次ID
     */
    @ApiModelProperty(value = "班次ID")
    private Integer wsId;
    /**
     * 是否停用1启用0停用
     */
    @ApiModelProperty(value = "是否停用1启用0停用")
    private Integer isUsed;
}
