package com.anaysis.entity;

import java.math.BigDecimal;

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
@TableName("yb_machine_bsinfo")
@Accessors(chain = true)
@ApiModel(value = "设备扩展信息_yb_machine_bsinfoMachineBsinfo实体")
public class MachineBsinfo implements Serializable {


    @ApiModelProperty(value = "id主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "设备id")
    @TableField("ma_id")
    private Integer maId;


    @ApiModelProperty(value = "设备出厂编号")
    @TableField("serialno")
    private String serialno;


    @ApiModelProperty(value = "出厂日期")
    @TableField("out_date")
    private String outDate;


    @ApiModelProperty(value = "设备重量（单位V）")
    @TableField("weight")
    private Integer weight;


    @ApiModelProperty(value = "外形尺寸(长*宽*高单位cm)")
    @TableField("size")
    private String size;


    @ApiModelProperty(value = "设备功率（单位KW）")
    @TableField("power")
    private BigDecimal power;


    @ApiModelProperty(value = "设备电压（单位V）")
    @TableField("voltage")
    private Integer voltage;


    @ApiModelProperty(value = "厂家电话")
    @TableField("phone")
    private String phone;


    @ApiModelProperty(value = "联系方式（李经理-1380000）")
    @TableField("contact")
    private String contact;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

}
