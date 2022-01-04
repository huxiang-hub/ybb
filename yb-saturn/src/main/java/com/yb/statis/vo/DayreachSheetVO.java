package com.yb.statis.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DayreachSheetVO implements Serializable {

    /*按日期分sheet*/
    private String targetDay;
    /*图标*/
    private ReachImageVO reachImageVO;
    /*月*/
    private String mounth;
    /*日*/
    private String day;
    /*一个sheet数据*/
    List<StatisDayreachVO> statisDayreachVOList;
}
