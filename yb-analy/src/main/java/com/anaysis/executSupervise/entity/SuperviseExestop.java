package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * @author my
 * @date 2020-06-16
 * Description: 当前停机表_yb_supervise_exestop
 */
@Data
@TableName("yb_supervise_exestop")
public class SuperviseExestop implements Serializable {

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
     * 结束时间*
     */
    private Date endTime;

    /**
     * 当前停留时间（秒）*
     */
    private Integer stayTime;

    /**
     * 规则时间大于X分钟
     */
    private Integer regular;

    /**
     * 当前超过多少分钟数*
     */
    private Integer overTime;

    /**
     * 按照规则已经存入log记录0未记录1已记录2已完成
     */
    private Integer status;

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
     * 是否弹框停机
     */
    private Boolean popDown;

    /**
     * 设备停机故障记录id
     */
    private Integer faultId;

}
