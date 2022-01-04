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
@TableName("yb_mater_mtinfo")
@Accessors(chain = true)
@ApiModel(value = "物料信息MaterMtinfo实体")
public class MaterMtinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "物料名称")
    @TableField("ml_name")
    private String mlName;


    @ApiModelProperty(value = "物料编号（同步字段）")
    @TableField("ml_no")
    private String mlNo;


    @ApiModelProperty(value = "物料分类")
    @TableField("mc_id")
    private Integer mcId;


    @ApiModelProperty(value = "物料类型1、大宗原料2、生产辅料")
    @TableField("mold")
    private Integer mold;


    @ApiModelProperty(value = "型号")
    @TableField("model")
    private String model;


    @ApiModelProperty(value = "材质")
    @TableField("material")
    private String material;


    @ApiModelProperty(value = "规格")
    @TableField("specification")
    private String specification;


    @ApiModelProperty(value = "尺寸")
    @TableField("size")
    private String size;


    @ApiModelProperty(value = "品牌")
    @TableField("brand")
    private String brand;


    @ApiModelProperty(value = "厂家")
    @TableField("manufactor")
    private String manufactor;


    @ApiModelProperty(value = "本地数据1是0否（0非本租户信息，为行业同步数据）")
    @TableField("islocal")
    private Integer islocal;


    @ApiModelProperty(value = "是否删除1是0否（若为行业同步数据表示非物理删除，做删除标志，表示不能更新同步数据）")
    @TableField("isdel")
    private Integer isdel;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "最后更新时间")
    @TableField("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;


    @ApiModelProperty(value = "ERP的UUID")
    @TableField("erp_id")
    private String erpId;

}
