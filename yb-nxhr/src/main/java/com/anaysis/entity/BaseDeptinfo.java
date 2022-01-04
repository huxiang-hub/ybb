package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lzb
 * @date 2020-11-25
 */
@Data
@TableName("yb_base_deptinfo")
@Accessors(chain = true)
@ApiModel(value = "部门表BaseDeptinfo实体")
public class BaseDeptinfo implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private String tenantId;


    @ApiModelProperty(value = "父主键")
    @TableField("p_id")
    private Integer pId;


    @ApiModelProperty(value = "部门名")
    @TableField("dp_name")
    private String dpName;


    @ApiModelProperty(value = "部门编号")
    @TableField("dp_num")
    private String dpNum;


    @ApiModelProperty(value = "部门全称")
    @TableField("full_name")
    private String fullName;


    @ApiModelProperty(value = "管理1/生产2/经营3")
    @TableField("classify")
    private Integer classify;


    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


    @ApiModelProperty(value = "是否已删除 /1 删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @ApiModelProperty(value = "erp的uuid")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "erp的父uuid")
    @TableField("erp_pid")
    private String erpPid;

}
