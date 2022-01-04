package com.yb.statis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @author my
 * @date 2020-07-23
 * Description: 小时达成率-备注_yb_statis_reachremark
 */
@Data
@TableName("yb_statis_reachremark")
public class StatisReachremark implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小时达成率id
     */
    private Integer srId;

    /**
     * 生产准备时间
     */
    private Integer proReadyTime;

    /**
     * 设备故障时间
     */
    private Integer deviceFaultTime;

    /**
     * 品质实验时间
     */
    private Integer qualityTestTime;

    /**
     * 品质实验时间
     */
    private Integer typeSwitchTime;

    /**
     * 管理停止时间
     */
    private Integer manageStopTime;

    /**
     * 其他损失时间(分钟)
     */
    private Integer otherLossTime;

    /**
     * 其他损失事由
     */
    private String otherLossCause;
}
