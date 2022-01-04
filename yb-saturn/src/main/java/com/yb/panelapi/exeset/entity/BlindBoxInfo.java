package com.yb.panelapi.exeset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@TableName("_yb_supervise_boxinfo,yb_machine_mixbox id=id")
@Api(value = "盒子绑定设备")
@Data
public class BlindBoxInfo  implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "盒子的唯一标识uuid")
    private String uuId;

    @ApiModelProperty(value = "盒子的绑定状态")
    private int active;
    private  Integer maId;


}
