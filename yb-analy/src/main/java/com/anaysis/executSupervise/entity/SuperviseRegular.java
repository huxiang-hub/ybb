package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备状态间隔表
 * @Author my
 * @Date Created in 2020/6/9
 */
@Data
@TableName("yb_supervise_regular")

public class SuperviseRegular implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer maId;

    private String uuid;
    /**
     * 盒子状态 1运行2停机3故障4离线
     */
    private Integer status;
    /**
     * 开始盒子计数
     */
    private Integer startNum;
    /**
     * 结束盒子计数
     */
    private Integer endNum;
    /**
     * 当前盒子的计数
     */
    private Integer pcout;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 间隔时间（秒）
     */
    private Integer diffNum;
    /**
     * 区间速度
     */
    private Double currSpeed;
    /**
     * 小时
     */
    private Integer targetHour;
    /**
     * 分
     */
    private Integer targetMin;
    /**
     * 日期
     */
    private String targetDay;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 更新时间
     */
    private Date updateAt;

}
