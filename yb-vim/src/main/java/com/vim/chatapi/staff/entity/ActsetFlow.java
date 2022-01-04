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
@TableName("yb_actset_flow")
@ApiModel(value = "StaffFlow", description = "yb_actset_flow")
public class ActsetFlow  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("af_name")
    private Integer afName;
    @ApiModelProperty("flow_data")
    private String flowData;
    @ApiModelProperty("leve")
    private Integer leve;
    @ApiModelProperty("target")
    private Integer target;
    @ApiModelProperty("dp_id")
    private Integer dpId;
    @ApiModelProperty("us_id")
    private Integer usId;
    @ApiModelProperty("limit_time")
    private Integer limitTime;
    @ApiModelProperty("is_reason")
    private Integer isReason;
    @ApiModelProperty("create_at")
    private Date createAt;





}
