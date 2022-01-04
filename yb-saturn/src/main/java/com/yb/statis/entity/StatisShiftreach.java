package com.yb.statis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_statis_shiftreach")
public class StatisShiftreach implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*日期*/
    private String targetDay;
    /*班次id*/
    private Integer wsId;
    /*班次名称*/
    private String wsCkname;
    /*设备ID*/
    private Integer maId;
    /*设备名称*/
    private String maName;
    /*操作ID*/
    private Integer usId;
    /*操作人姓名*/
    private String usName;
    /*负责人ID*/
    private Integer responsible;
    /*负责人姓名*/
    private String respName;
    /*计划总产能*/
    private Integer planCount;
    /*实际生产数*/
    private Integer realCount;
    /*23.54*/
    private Double rateNum;
    /*平方数（米2）*/
    private Integer squareNum;
    /*备注说明）*/
    private String remark;
    /*保养时间（分钟）*/
    private Integer maintainTime;
    /*维修时间*/
    private Integer repairTime;
    /*计划停机时间*/
    private Integer planstopTime;
    /*创建时间*/
    private Date createAt;
    /*更新时间*/
    private Date updateAt;
}
