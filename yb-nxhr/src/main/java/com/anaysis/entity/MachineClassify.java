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
@TableName("yb_machine_classify")
@Accessors(chain = true)
@ApiModel(value = "设备型号_yb_mach_classifyMachineClassify实体")
public class MachineClassify implements Serializable {


    @ApiModelProperty(value = "流水号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "品牌")
    @TableField("brand")
    private String brand;


    @ApiModelProperty(value = "型号")
    @TableField("model")
    private String model;


    @ApiModelProperty(value = "规格")
    @TableField("specs")
    private String specs;


    @ApiModelProperty(value = "图片")
    @TableField("image")
    private String image;


    @ApiModelProperty(value = "出厂标准速度")
    @TableField("speed")
    private Integer speed;


    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


    @ApiModelProperty(value = "厂家")
    @TableField("manufacturer")
    private String manufacturer;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "erpUUID")
    @TableField("erp_id")
    private String erpId;

}
