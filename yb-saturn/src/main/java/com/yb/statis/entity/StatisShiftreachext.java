package com.yb.statis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_statis_shiftreachext")
public class StatisShiftreachext implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /*统计班次达成率_yb_statis_shiftreach的主键关联*/
    private Integer sfId;
    /*设备类型划分*/
    private Integer maType;
    /*印数套数*/
    private Integer printNum;
    /*ps版数-模数*/
    private Integer psNum;
    /*工单数*/
    private Integer ordNum;
    /*校板数量*/
    private Integer compareNum;
    /*创建时间*/
    private Date createAt;
    /*更新时间*/
    private Date updateAt;
}
