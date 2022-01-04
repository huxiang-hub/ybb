package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-12-10
 */
@Data
@TableName("yb_base_classinfo")
@Accessors(chain = true)
@ApiModel(value = "BaseClassinfo实体")
public class BaseClassinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "车间id")
    @TableField("dp_id")
    private Integer dpId;


    @ApiModelProperty(value = "班组名称")
    @TableField("bc_name")
    private String bcName;


    @ApiModelProperty(value = "班组人数")
    @TableField("bc_num")
    private Integer bcNum;


    @ApiModelProperty(value = "顺序默认100")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "班次id")
    @TableField("ws_id")
    private Integer wsId;


    @ApiModelProperty(value = "是否停用1启用0停用")
    @TableField("is_used")
    private Integer isUsed;


    @ApiModelProperty(value = "erp班组对应设备id")
    @TableField(exist = false)
    private String erpMachineId;

}
