package com.vim.chatapi.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_staff_dayoff")
@ApiModel(value = "StaffDayoff", description = "yb_staff_dayoff")
public class StaffDayoff  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("model")
    private Integer model;
    @ApiModelProperty("start_date")
    @JsonFormat(pattern = "yy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @ApiModelProperty("end_date")
    @JsonFormat(pattern = "yy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    @ApiModelProperty("期间时长3.5天（三天半）")
    private Double duration;
    @ApiModelProperty("审核状态：1提交(待审批)2同意3不同意")
    private Integer status;
    @ApiModelProperty("备注原因")
    private String reasons;
    @ApiModelProperty("创建时间")
    private Date createAt;












}
