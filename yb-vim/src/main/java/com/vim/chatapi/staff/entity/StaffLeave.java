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
@TableName("yb_staff_leave")
@ApiModel(value = "StaffFlow", description = "yb_staff_leave")
public class StaffLeave implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("rectify_time")
    private Date rectifyTime;
    @ApiModelProperty("reasons")
    private String reasons;
    @ApiModelProperty("status")
    private Integer status;
    @ApiModelProperty("创建时间")
    private Date createAt;
}
