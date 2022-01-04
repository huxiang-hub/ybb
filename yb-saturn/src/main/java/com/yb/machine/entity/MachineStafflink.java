package com.yb.machine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_machine_stafflink")
@ApiModel(value = "人员和机台绑定_yb_machine_stafflink")
public class MachineStafflink implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "设备唯一id")
    private Integer maId;
    @ApiModelProperty(value = "用户唯一id")
    private Integer usId;
    @ApiModelProperty(value = "岗位（角色）1.机长2.班长3.车间主管4排产员")
    private Integer jobs;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
}
