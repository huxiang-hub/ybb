package com.vim.chatapi.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_workbatch_shiftinfo")
public class WorkbatchShiftinfo  implements Serializable {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    @ApiModelProperty("db_id")
    private Integer dbId;
    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("model")
    private Integer model;
    @ApiModelProperty("ck_name")
    private Integer ckName;
    @ApiModelProperty("is_work")
    private Integer isWork;
    @ApiModelProperty("check_date")
    private Date checkDate;
    @ApiModelProperty("start_time")
    private Date startTime;
    @ApiModelProperty("end_time")
    private Date endTime;
    @ApiModelProperty("stay_time")
    private Integer stayTime;
    @ApiModelProperty("ck_status")
    private Integer ckStatus;
    @ApiModelProperty("create_at")
    private Date createAt;

}
