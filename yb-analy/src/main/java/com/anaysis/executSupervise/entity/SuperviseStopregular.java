package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author my
 * @date 2020-06-16
 * Description: '停机规则记录_yb_supervise_stopregular按照规则进行记录X分钟'
 */
@Data
@TableName("yb_supervise_stopregular")
public class SuperviseStopregular implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 盒子唯一标识
     */
    private String uuid;

    /**
     * 设备ID
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
     * 停留时间（秒）
     */
    private Integer stayTime;

    /**
     * 规则时间 比如5分钟，就是大于5分钟以上的规则
     */
    private Integer regular;

    /**
     * 超过分钟数*
     */
    private Integer overTime;

    /**
     * 排产id（可选）
     */
    private Integer sdId;

    /**
     * 操作人
     */
    private String usIds;

    /**
     * 1记录2已经结束停机状态
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
     * 统计小时（0-23）
     */
    private Integer targetHour;

    /**
     * 统计分钟（0-59）
     */
    private Integer targetMin;

}
