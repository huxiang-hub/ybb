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
 * @date 2020-11-25
 */
@Data
@TableName("yb_process_machlink")
@Accessors(chain = true)
@ApiModel(value = "设备工序关联表yb_process_machlinkProcessMachlink实体")
public class ProcessMachlink implements Serializable {


    @ApiModelProperty(value = "id主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "设备id")
    @TableField("ma_id")
    private Integer maId;


    @ApiModelProperty(value = "工序id")
    @TableField("pr_id")
    private Integer prId;


    @ApiModelProperty(value = "标准时速")
    @TableField("speed")
    private Integer speed;


    @ApiModelProperty(value = "准备时间（min）")
    @TableField("prepare_time")
    private Integer prepareTime;


    @ApiModelProperty(value = "持续运行为正式生产 分钟数")
    @TableField("keep_run")
    private Integer keepRun;

}
