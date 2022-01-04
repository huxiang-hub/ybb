package com.anaysis.executSupervise.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/28.
 */
@Data
public class PopUpEntity {

    private Integer popModel;   //弹窗类型  1 停机。2质检

    private Integer qaModel;    //质检类型  1 时间。2 数量

    private Integer disapper;  //弹窗消失时间

    private Integer redCount;    //小红点数量

    private OrderInfo orderInfo;

    private Date startTime;    //质检开始时间。给前台推送消息用。

    private Date endTime;    //质检结束时间。给前台推送消息用。

    private Double duration;    //时间间隔。

    //质量检测数量限制
    private Integer limitNum;


    private Integer esId;   //执行表Id

    private Integer faultId;   //停机Id

    private Integer qualityId;    //质检Id

}
