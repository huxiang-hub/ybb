package com.yb.workbatch.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PlanStateTime implements Serializable {

    private Integer maId;//设备id
    private String maName;//设备名称
    private Integer wsId;//班次id
    private Date proBeginTime;//开始时间
    private Date proFinishTime;//结束时间
    private Integer mouldStay;//换膜时长
    private String mealOnetime;//吃饭时间
    private String mealSecondtime;//吃饭时间
    private String mealThirdtime;//吃饭时间
    private String sdDate;//排产日期
}
