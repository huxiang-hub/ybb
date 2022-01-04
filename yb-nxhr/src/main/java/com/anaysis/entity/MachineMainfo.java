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
 * @date 2020-11-26
 */
@Data
@TableName("yb_machine_mainfo")
@Accessors(chain = true)
@ApiModel(value = "设备_yb_mach_mainfoMachineMainfo实体")
public class MachineMainfo implements Serializable {


    @ApiModelProperty(value = "流水号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "设备型号id")
    @TableField("mt_id")
    private Integer mtId;


    @ApiModelProperty(value = "设备编号")
    @TableField("mno")
    private String mno;


    @ApiModelProperty(value = "设备分类：maType 数据字典")
    @TableField("ma_type")
    private String maType;


    @ApiModelProperty(value = "设备名称")
    @TableField("name")
    private String name;


    @ApiModelProperty(value = "车间Id")
    @TableField("dp_id")
    private Integer dpId;


    @ApiModelProperty(value = "工序---指定主要工序")
    @TableField("pro_id")
    private Integer proId;


    @ApiModelProperty(value = "设备排序")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "是否为工序接单操作0按设备接单1是按工序接单；")
    @TableField("is_recepro")
    private Integer isRecepro;


    @ApiModelProperty(value = "erpUUID")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "输入单位：maUnit数据字典")
    @TableField("in_unit")
    private String inUnit;


    @ApiModelProperty(value = "匹配输出单位：maUnit数据字典")
    @TableField("out_unit")
    private String outUnit;


    @ApiModelProperty(value = "辅助单位：maUnit数据字典")
    @TableField("auxiliary_unit")
    private String auxiliaryUnit;

    @ApiModelProperty(value = "工序code ")
    @TableField(exist = false)
    private String prCode;

}
