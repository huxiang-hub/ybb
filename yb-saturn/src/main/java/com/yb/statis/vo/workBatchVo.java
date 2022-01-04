package com.yb.statis.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class workBatchVo {
    //产品名称
    private String pdName;
    //部件名称
    private String ptName;
    //计划数
    private Integer planNum;
    //良品数量
    private Integer nodefectCount;
    //达成率
    private Double reachRate;
    //良品率
    private Double goodRate;
    //实际产能
    private Integer normalSpeed;
    //性能稼动率
    private Double speendRate;
    //废品数量
    private Integer watesCount;
    //废品率
    private Double watesRate;
    //应出勤f
    private Integer dutyNum;
    //实际出勤
    private Double realDutyNum;
    //po不足
    private Double poRate;
    //投入数
    private Integer workCount;
    private Integer realSpeed;
    //机台名称
    private String machineName;
    //批次编号
    private String batchNo;
    //工序名称
    private String prName;
    //计划生产时间段
    private String planDateTime;
    //实际生产时间段
    private String actualDateTime;
    //标准产能
    private Integer factSpeed;
    private String userName;
    private Integer index;//序号
}
