package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "yb_quality_bfwaste")
@ApiModel(value = "废品上报_yb_quality_bfwaste")
public class QualityBfwaste implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    @ApiModelProperty(value = "上报id")
    private Integer bfId;
    @ApiModelProperty(value = "执行单")
    private Integer exId;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "废品类型名称")
    private String wasteType;
    @ApiModelProperty(value = "废品类型id")
    private Integer wasteTypeId;
    @ApiModelProperty(value = "数量")
    private Integer wasteNum;
    @ApiModelProperty(value = "盒子数量")
    private Integer startNum;
    @ApiModelProperty(value = "盒子数量")
    private Integer endNum;
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    @ApiModelProperty(value = "结束时间")
    private Date endAt;
    @ApiModelProperty(value = "上报状态0未上报1已上报'")
    private Integer reportStatus;
    @ApiModelProperty(value = "上报时间")
    private Date reportTime;
    @ApiModelProperty(value = "上报操作人")
    private Integer usId;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
}
