package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
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
@TableName("yb_process_workinfo")
@Accessors(chain = true)
@ApiModel(value = "工序表--租户的工序内容（可以依据行业模版同步）ProcessWorkinfo实体")
public class ProcessWorkinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "工序名称(比对同步数据关键字)")
    @TableField("pr_name")
    private String prName;


    @ApiModelProperty(value = "工序编号")
    @TableField("pr_no")
    private String prNo;


    @ApiModelProperty(value = "显示顺序")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "状态 1启用0停用")
    @TableField("status")
    private Integer status;


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


    @ApiModelProperty(value = "erp的uuid")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "工序损耗比例")
    @TableField("loss_factor")
    private BigDecimal lossFactor;

}
