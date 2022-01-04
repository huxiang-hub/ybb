package com.yb.statis.vo;

import lombok.Data;

import java.util.List;

@Data
public class StatisShiftreachExcelExportVO {
    /*设备类型*/
    private String maType;
    /*设备类型值*/
    private String maTypeValue;
    /*日期*/
    private String targetDay;
    /*图片保存对象*/
    private ReachImageVO reachImageVO;
//    /*红旗*/
//    private byte[] redBanner;
//    /*微笑*/
//    private byte[] smile;
//    /*难过*/
//    private byte[] sorrily;
//    /*炸弹*/
//    private byte[] bomb;
    /*一页数据*/
    private List<StatisShiftreachVO> statisShiftreachVOList;
}
