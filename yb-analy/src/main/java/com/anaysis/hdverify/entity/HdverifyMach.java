package com.anaysis.hdverify.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_hdverify_mach")
public class HdverifyMach {
    private Integer id;
    private Integer maId;//设备id
    private String maName;//设备名称
    private Integer maStartnum;//设备开始时间计数
    private Integer maEndnum;//设备结束时间计数
    private Integer maDiff;//设备计数差值数量
    private Integer bxId;//盒子的id
    private String bxUuid;//盒子唯一标识位
    private Integer bxStartnum;//盒子的开始计数
    private Integer bxEndnum;//盒子的结束计数
    private Integer bxDiff;//盒子的技术差异数量
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Integer stayTime;//间隔时间（秒为计数）
    private Double diffRate;//误差率
    private String operator;//操作人
    private Integer status;//验证状态：1未开始、2进行、3结束、4超时12小时、5废弃
    private Date createAt;//创建时间
}
