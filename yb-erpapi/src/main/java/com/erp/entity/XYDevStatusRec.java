package com.erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author my
 * #Description
 */
@Data
@TableName("XYDevStatusRec")
public class XYDevStatusRec {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("deviceId")
    private Integer deviceID;

    /**
     * 状态：1运行2停机（空转或带电停机）4离线（关机或网络中断）
     */
    @TableField("devStatus")
    private String deviceStatus;

    @TableField("boxNum")
    private Integer boxNum;

    @TableField("devSpeed")
    private BigDecimal devSpeed;

    /**
     * 班次ID
     */
    @TableField("shiftId")
    private Integer shiftId;

    /**
     * 排程的GUID
     */
    @TableField("planGUID")
    private String planGUID;

    @TableField("beginTime")
    private Date beginTime;

    @TableField("updateTime")
    private Date updateTime;

    /**
     * 1调机状态，2正式生产状态
     */
    @TableField("wOStatus")
    private Integer wOStatus;

    /**
     * 该设备最后执行排程单ID
     */
    @TableField("exeId")
    private Integer exeId;

    @TableField("maId")
    private Integer maId;
}
