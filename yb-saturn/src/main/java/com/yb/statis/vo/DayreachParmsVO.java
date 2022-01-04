package com.yb.statis.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DayreachParmsVO implements Serializable {

    /*日期*/
    private String targetDay;
    /*设备类型*/
    private Integer maType;
    /*开始日期*/
    private String startDay;
    /*借宿日期*/
    private String endDay;

}
