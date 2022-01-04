package com.yb.mater.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @author lzb
 * @date 2021-01-10
 */
@Data
@TableName("yb_execute_materials")
@Accessors(chain = true)
@ApiModel(value = "ExecuteMaterials实体")
public class ExecuteMaterials implements Serializable {


    @ApiModelProperty(value = "唯一标识id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "物料id")
    @TableField("mat_id")
    private String matId;


    @ApiModelProperty(value = "物料名称")
    @TableField("mat_name")
    private String matName;

    @ApiModelProperty(value = "条码")
    @TableField("bar_code")
    private String barCode;


    @ApiModelProperty(value = "物料使用数量")
    @TableField("mat_num")
    private Integer matNum;


    @ApiModelProperty(value = "设备id")
    @TableField("ma_id")
    private Integer maId;


    @ApiModelProperty(value = "班次id")
    @TableField("ws_id")
    private Integer wsId;


    @ApiModelProperty(value = "排产单id")
    @TableField("wf_id")
    private Integer wfId;


    @ApiModelProperty(value = "操作用户")
    @TableField("us_id")
    private Integer usId;

    @ApiModelProperty(value = "总数")
    @TableField("total_num")
    private Integer totalNum;


    @ApiModelProperty(value = "托盘标识卡id")
    @TableField("et_id")
    private Integer etId;


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
}
