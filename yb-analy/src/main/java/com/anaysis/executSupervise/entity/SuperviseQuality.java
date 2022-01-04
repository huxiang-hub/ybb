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
 * Description: 当前质检设备表
 */
@Data
@TableName("yb_supervise_quality")
public class SuperviseQuality implements Serializable {

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
     * 当前停留时间（秒）
     */
    private Integer stayTime;

    /**
     * 规则时间大于5分钟
     */
    private Integer regular;

    /**
     * 当前超过多少分钟数
     */
    private Integer overTime;

    /**
     * 按照规则已经存入
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
}
