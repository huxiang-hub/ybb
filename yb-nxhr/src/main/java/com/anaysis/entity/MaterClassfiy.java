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
@TableName("yb_mater_classfiy")
@Accessors(chain = true)
@ApiModel(value = "物料分类MaterClassfiy实体")
public class MaterClassfiy implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "物料分类")
    @TableField("mc_type")
    private String mcType;


    @ApiModelProperty(value = "分类名称")
    @TableField("mc_name")
    private String mcName;


    @ApiModelProperty(value = "物料编号")
    @TableField("mc_no")
    private String mcNo;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "ERP的UUID")
    @TableField("erp_id")
    private String erpId;

}
