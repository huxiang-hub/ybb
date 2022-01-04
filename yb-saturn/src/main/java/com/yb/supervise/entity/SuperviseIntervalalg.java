package com.yb.supervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_supervise_intervalalg")
@ApiModel("设备数据记录表_yb_supervise_intervalalg")
public class SuperviseIntervalalg implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "唯一编号uuid")
    private String uuid;
    @ApiModelProperty(value = "统计计数 1运行2停机3故障4离线")
    private Integer status;
    @ApiModelProperty(value = "接收盒子数据")
    private Integer number;
    @ApiModelProperty(value = "开始时间计数")
    private Integer startNum;
    @ApiModelProperty(value = "结束时间计数")
    private Integer endNum;
    @ApiModelProperty(value = "间隔计数")
    private Integer pcout;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "间隔时间（秒）")
    private Integer diffTime;
    @ApiModelProperty(value = "区间速度")
    private Double currSpeed;
    @ApiModelProperty(value = "日期时间实例：20200314")
    private Character targetTime;
    @ApiModelProperty(value = "分类模式1整点2半点")
    private Integer model;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
