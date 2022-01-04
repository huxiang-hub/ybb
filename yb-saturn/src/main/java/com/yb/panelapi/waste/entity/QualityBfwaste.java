package com.yb.panelapi.waste.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_quality_bfwaste")
public class QualityBfwaste {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //工序id
    private Integer prId;
    //上报id
    private Integer bfId;
    //订单id
    private Integer orderId;
    //设备id
    private Integer maId;
    //废品类型名称
    private String wasteType;
    //废品类型id
    private Integer wasteTypeId;
    //数量
    private Integer wasteNum;
    //上报时间
    private Date reportTime;


}