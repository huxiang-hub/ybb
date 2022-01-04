package com.yb.supervise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_supervise_interval")
public class SuperviseInterval {
    private Integer id;

    private Integer maId;//设备id
    private String  uuid;//唯一标识位
    private String status;//统计计数 1运行2停机3故障4离线
    private Integer startNum;//开始时间计数
    private Integer endNum;//结束时间计数
    private Integer pcout;//当前盒子的计数
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String diffTime;//间隔时间
    private Double currSpeed;//区间速度啊
    private String targetDay;//记录日期
    private Integer targetHour;//记录时间小时
    private Integer targetMin;//记录时间分钟
    private Date createAt;//创建时间
    private Date updateAt;//创建时间
}
