package com.yb.fz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_fz_macheset")
@ApiModel(value = "FzMcheset对象", description = "方正上报设定")
public class FzMacheset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**设备ID*/
    @ApiModelProperty(value = "设备ID")
    private Integer maId;
    /**显示名称*/
    @ApiModelProperty(value = "显示名称")
    private String disName;
    /**单位：分钟、张、米、小时*/
    @ApiModelProperty(value = "单位：分钟、张、米、小时")
    private String unit;
    /**类型：1、int2、string3、date*/
    @ApiModelProperty(value = "类型：1、int2、string3、date")
    private Integer model;
    /**顺序*/
    @ApiModelProperty(value = "顺序")
    private Integer sort;
    /**0停用1启用*/
    @ApiModelProperty(value = "0停用1启用")
    private Integer status;
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
}
