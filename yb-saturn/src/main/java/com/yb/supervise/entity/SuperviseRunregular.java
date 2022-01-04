package com.yb.supervise.entity;

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
@TableName("yb_supervise_runregular")

public class SuperviseRunregular implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String uuid;
    private Integer maId;
    private Integer sdId;//排产单信息
    private Integer number;//当前盒子的计数
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Integer stayTime;//停留时间（秒）
    private Integer regular;//规则时间 比如5分钟，就是大于5分钟的规则
    private Integer overTime;//超过分钟数
    private String usIds;//操作人
    private Integer status;//状态 1记录2已经结束状态
    private Integer isWaring;//是否预警 是否预警0否1已预警
    private String targetDay;//日期
    private Integer targetHour;//小时
    private Integer targetMin;//分
    private Date createAt;//创建时间
    private Date updateAt;//更新时间

}
