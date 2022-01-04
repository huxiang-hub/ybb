package com.erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author my
 * #Description
 */
@Data
@TableName("XYPartsOutPut")
public class XYPartsOutPut {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("deviceId")
    private Integer deviceID;

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
    @TableField("endTime")
    private Date endTime;
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 调机数
     */
    @TableField("adjustOutput")
    private Integer adjustOutput;

    /**
     * 良品数
     */
    @TableField("goodOutput")
    private Integer goodOutput;

    /**
     * 1调机状态，2正式生产状态
     */
    @TableField("wOStatus")
    private Integer wOStatus;

    @TableField("maId")
    private Integer maId;
}
