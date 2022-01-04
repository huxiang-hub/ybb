package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDate;

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
 * @date 2020-11-25
 */
@Data
@TableName("yb_base_staffext")
@Accessors(chain = true)
@ApiModel(value = "基础信息表_yb_base_staffextBaseStaffext实体")
public class BaseStaffext implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "人员ID")
    @TableField("sf_id")
    private Integer sfId;


    @ApiModelProperty(value = "性别 1男0女")
    @TableField("sex")
    private Integer sex;


    @ApiModelProperty(value = "学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6")
    @TableField("education")
    private Integer education;


    @ApiModelProperty(value = "出生年月")
    @TableField("birthday")
    private String birthday;


    @ApiModelProperty(value = "身份证号")
    @TableField("idcard")
    private String idcard;


    @ApiModelProperty(value = "身份证地址")
    @TableField("idaddr")
    private String idaddr;


    @ApiModelProperty(value = "籍贯（出生地）")
    @TableField("hometown")
    private String hometown;


    @ApiModelProperty(value = "现居住址")
    @TableField("curraddr")
    private String curraddr;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "erpUUID")
    @TableField("erp_id")
    private String erpId;

}
