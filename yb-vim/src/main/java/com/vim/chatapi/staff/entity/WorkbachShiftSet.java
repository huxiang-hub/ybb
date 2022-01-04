package com.vim.chatapi.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("yb_workbatch_shiftset")
public class WorkbachShiftSet implements Serializable {
    /***
     *部门名称
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty()
    private Integer dbId;
    @ApiModelProperty()
    private Integer usId;
    @ApiModelProperty()
    private Integer model;
    @ApiModelProperty("ck_name")
    private Integer ckName;
    @ApiModelProperty("stay_time")
    private Integer stayTime;
    @ApiModelProperty("start_date")
    private Date startDate;
    @ApiModelProperty("end_date")
    private Date endDate;
    @ApiModelProperty("start_time")
    private Date startTime;
    @ApiModelProperty("end_time")
    private Date endTime;
    @ApiModelProperty("create_at")
    private Data createAt;
    @ApiModelProperty("update")
    private Date update;
}
