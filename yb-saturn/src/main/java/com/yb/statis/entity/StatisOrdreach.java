package com.yb.statis.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author my
 * @date 2020-06-11
 * Description: 设备定时达成率_yb_statis_ordreach
 */
@Data
@TableName("yb_statis_ordreach")
@ApiModel(value = "小时达成率对象")
public class StatisOrdreach implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String maName;

    /**
     * 设备时速度
     */
    @ApiModelProperty(value = "设备时速度")
    private Integer speed;

    /**
     * 排产时速度（加换膜时间）
     */
    @ApiModelProperty(value = "排产时速度（加换膜时间）")
    private Integer planCount;

    /**
     * 实际数量
     */
    @ApiModelProperty(value = "实际数量")
    private Integer realCount;

    /**
     * 达成率
     */
    @ApiModelProperty(value = "达成率")
    private BigDecimal reachRate;

    /**
     * 24进制1-24时
     */
    @ApiModelProperty(value = "24进制1-24时")
    private Integer targetHour;

    /**
     * 目前小时默认为0:规则变化可能为15丶30丶45
     */
    @ApiModelProperty(value = "目前小时默认为0:规则变化可能为15丶30丶45")
    private Integer targetMin;

    /**
     * 统计时间2020-04-15
     */
    @ApiModelProperty(value = "统计时间2020-04-15")
    private String targetDay;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private String planNum;
    @ApiModelProperty(value = "计划时间")
    private Integer planTime;

    /**
     * 工单单号
     */
    @ApiModelProperty(value = "工单Id")
    private String sdId;

    /**
     * 执行单号
     */
    @ApiModelProperty(value = "执行单号")
    private Integer exId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "客户名称")
    private String pdName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remark;

    /**
     * 工单单号
     */
    @ApiModelProperty(value = "工单单号")
    private String wbNo;

    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;

    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    private String wsName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "小时达成率锁定系统统计 是否系统计算0不锁定1锁定(手工统计)")
    private Integer reachIslock;//小时达成率锁定系统统计 是否系统计算0不锁定1锁定(手工统计)

    private Date updateAt;

}
