package com.yb.order.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/4/13.
 */
@Data
public class OrderSchedule implements Serializable {

    private Integer id;
    private Integer odId;
    private Integer wbId;
    private Integer sdId;
    private Integer maId;
    private Integer reportNum;
    private Integer wasteNum;
    private Integer nowNum;
    private Date updateAt;
    private Double total;
    private Date startTime;
    private Date endTime;

}
