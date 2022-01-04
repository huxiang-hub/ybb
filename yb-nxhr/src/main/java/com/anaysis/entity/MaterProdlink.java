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
 * @date 2020-11-30
 */
@Data
@TableName("yb_mater_prodlink")
@Accessors(chain = true)
@ApiModel(value = "产品物料关系MaterProdlink实体")
public class MaterProdlink implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "产品id")
    @TableField("pd_id")
    private Integer pdId;


    @ApiModelProperty(value = "物料id")
    @TableField("ml_id")
    private Integer mlId;


    @ApiModelProperty(value = "工序id")
    @TableField("pr_id")
    private Integer prId;


    @ApiModelProperty(value = "物料数量")
    @TableField("mt_num")
    private Integer mtNum;


    @ApiModelProperty(value = "产品数量")
    @TableField("pd_num")
    private Integer pdNum;


    @ApiModelProperty(value = "材质")
    @TableField("material")
    private String material;


    @ApiModelProperty(value = "型号")
    @TableField("model")
    private String model;


    @ApiModelProperty(value = "规格")
    @TableField("specification")
    private String specification;


    @ApiModelProperty(value = "尺寸")
    @TableField("size")
    private String size;


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


    @ApiModelProperty(value = "部件id")
    @TableField("pt_id")
    private Integer ptId;

}
