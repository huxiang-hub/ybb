package com.vim.chatapi.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_staff_uscheck")
@ApiModel(value = "StaffFlow", description = "yb_staff_uscheck")
public class StaffUscheck implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("class_num")
    private String classNum;
    @ApiModelProperty("ck_date")
    private String ckDate;
    @ApiModelProperty("in_time")
    private Date inTime;
    @ApiModelProperty("in_add")
    private String inAdd;
    @ApiModelProperty("in_lnglat")
    private String inLnglat;
    @ApiModelProperty("out_time")
    private Date outTime;
    @ApiModelProperty("out_add")
    private String outAdd;
    @ApiModelProperty("out_lnglat")
    private String outLnglat;
    @ApiModelProperty("create_at")
    private Date createAt;
    @ApiModelProperty("ck_status")
    private Integer ckStatus;

    @ApiModelProperty("model")
    private Integer model;


}
