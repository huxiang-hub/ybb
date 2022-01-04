package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author my
 * @date 2020-07-07
 * Description: 质检规则记录
 */
@Data
@TableName("yb_supervise_qualityregular")
public class SuperviseQualityregular implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盒子唯一标识
     */
    private String uuid;

    /**
     * 设备id
     */
    private Integer maId;

    /**
     * 开始盒子计数
     */
    private Integer number;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 停留时间(秒)
     */
    private Integer stayTime;

    /**
     * 超过分钟数
     */
    private Integer overTime;

    /**
     * 开始盒子数量
     */
    private Integer startNum;

    /**
     * 结束盒子数量
     */
    private Integer endNum;

    /**
     * 当前计数
     */
    private Integer currentNum;

    /**
     * 规则1、计数2、时间
     */
    private Integer regularModel;

    /**
     * 规则时间 例如：1、计数就为10000张2、时间累积60分钟
     */
    private Integer regular;

    /**
     * 排产id（可选）
     */
    private Integer sdId;

    /**
     * 操作人
     */
    private String usIds;

    /**
     * 1记录2已经结束状态
     */
    private Integer status;

    /**
     * 是否预警0否1已预警
     */
    private Integer isWaring;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 统计日期2020-06-06
     */
    private String targetDay;

    /**
     * 统计小时（0-23
     */
    private Integer targetHour;

    /**
     * 统计分钟（0-59)
     */
    private Integer targetMin;
}
