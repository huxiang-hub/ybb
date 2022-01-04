package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-11-26
 */
@Data
@TableName("yb_prod_partsinfo")
@Accessors(chain = true)
@ApiModel(value = "ProdPartsinfo实体")
public class ProdPartsinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "父节点ID")
    @TableField("pid")
    private Integer pid;


    @ApiModelProperty(value = "产品ID")
    @TableField("pd_id")
    private Integer pdId;


    @ApiModelProperty(value = "部件类型")
    @TableField("pt_type")
    private Integer ptType;


    @ApiModelProperty(value = "部件名称")
    @TableField("pt_name")
    private String ptName;


    @ApiModelProperty(value = "部件编号")
    @TableField("pt_no")
    private String ptNo;


    @ApiModelProperty(value = "1原料2部件+部件3原料+部件")
    @TableField("pt_classify")
    private Integer ptClassify;


    @ApiModelProperty(value = "部件ids；|竖线分隔|")
    @TableField("pt_ids")
    private String ptIds;


    @ApiModelProperty(value = "1产品，2模板")
    @TableField("pd_type")
    private Integer pdType;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "更新时间")
    @TableField("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;


    @ApiModelProperty(value = "ERP的UUID")
    @TableField("erp_id")
    private String erpId;

}
