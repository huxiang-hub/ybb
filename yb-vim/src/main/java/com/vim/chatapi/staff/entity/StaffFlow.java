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
@TableName("yb_staff_flow")
@ApiModel(value = "StaffFlow", description = "yb_staff_flow")
public class StaffFlow  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("af_id")
    private Integer afId;
    @ApiModelProperty("db_id")
    private Integer dbId;
    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("check_time")
    private Date checkTime;
    @ApiModelProperty("result")
    private String result;
    @ApiModelProperty("status）")
    private Integer status;
    @ApiModelProperty("创建时间")
    private Date createAt;
}
