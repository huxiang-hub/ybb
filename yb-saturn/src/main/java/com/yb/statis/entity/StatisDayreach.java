package com.yb.statis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_statis_dayreach")
public class StatisDayreach implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /*设备分类*/
    private Integer maType;
    /*日期*/
    private String targetDay;
    /*负责人ID*/
    private Integer responsible;
    /*负责人姓名*/
    private String respName;
    /*计划总产能*/
    private Integer planCount;
    /*实际生产数*/
    private Integer realCount;
    /*达成率*/
    private Double rateNum;
    /*备注说明*/
    private String remark;
    /*创建时间*/
    private Date createAt;
    /*更新时间*/
    private Date updateAt;
}
