package com.yb.exeset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_exeset_faultwaste")
@ApiModel(value = "ExesetFault对象", description = "故障停机设置_yb_exeset_faultwaste")
public class ExesetFaultwaste implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "流水ID")
    private Integer id;
    @ApiModelProperty(value = "设备Id")
    private Integer maId;
    @ApiModelProperty(value = "故障分类Id")
    private Integer mfId;
    @ApiModelProperty(value = "故障分类名称")
    private String fname;
    @ApiModelProperty(value = "故障分类编码")
    private String fvalue;
    @ApiModelProperty(value = "限制时间")
    private Integer overTime;
    @ApiModelProperty(value = "废品数（分钟）")
    private Integer waste;
    @ApiModelProperty(value = "创建时间设定（分钟）")
    private Date createAt;
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    @ApiModelProperty(value = "更新时间（分钟）")
    private Date updateAt;



}
