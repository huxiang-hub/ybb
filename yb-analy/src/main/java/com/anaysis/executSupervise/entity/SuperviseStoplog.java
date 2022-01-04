package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *
 * @author my
 * @date 2020-06-16
 * Description: 停机记录_yb_supervise_stoplog，小停机记录log日志信息，超过X分钟以下记录
 */
@Data
@TableName("yb_supervise_stoplog")
public class SuperviseStoplog implements Serializable {

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
     * 排产id（可选）
     */
    private Integer sdId;

    /**
     * 操作人
     */
    private String usIds;

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
