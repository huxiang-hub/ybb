package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_supervise_intervalalg")
public class SuperviseShiftcount {
    private Integer id;
    private Integer maId;//设备id
    private String uuid;//唯一标识位
    private String status;//统计计数 1运行2停机3故障4离线
    private Integer number;//盒子上传的数量mc
    private Integer startNum;//开始时间计数
    private Integer endNum;//结束时间计数
    private Integer pcout;//当前盒子的计数
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Integer diffTime;//间隔时间
    private Double currSpeed;//区间速度啊
    private String targetTime;//记录日期
    private Integer model;//分类模式1整点2半点0开始结束类型
    private Date createAt;//创建时间
    private Date updateAt;//创建时间

    /**
     * 能耗计数
     */
    private Integer energyNum;

    /**
     * 前面IN计数
     */
    private Integer inNum;

    /**
     * 前面输入IN的间隔计数
     */
    private Integer inPcout;

}
