package com.yb.statis.vo;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class StatisMachOrderVo {
    private Date exchangeTime;
    private String changeTime;//时间
    private String toWeek;//星期
    private String sfName;//班次
    private String dpName;//部门名称
    private Double  gatherRate;
    private Double  utilizeRate;
    private Double  performRate;
    private Double  yieldRate;
    private Double  equFailRate;
    private Integer  prepareStay;
    private Integer  planutilizeStay;
    private Integer  faultStay;
    private Integer  qualityStay;
    private Integer  mouldStay;
    private Integer  manageStay;
    private Integer rearchStay;
    private Integer  abrasionStay;
    private Integer  factutilizeStay;
    private String  mainProblem;
    private List<workBatchVo> workBatchLsit;
    private String classTranscationInfo;
    private Integer otherStay;
    private Integer dutyNumCount;//应出勤总人数
    private Double realDutyNumCount;//实际出勤总人数
    private Double poRateCount;//P/O不足总数
    private Integer planNumCount;//计划数总数
    private Integer worksCount;//投入数总数
    private Integer nodefectsCount;//良品数总数
    private Integer watesCountsCount;//废品数总数
    private Double reachRateAvg;//达成率平均数
    private Double goodRateAvg;//良品率平均数
    private Double watesRateAvg;//废品率平均数
    private Double speendRateAvg;//性能稼动率平均数
    private Integer normalSpeedAvg;//实际产能平均数
    private Integer factSpeedAvg;//标准产能平均数
    public StatisMachOrderVo(){
    }
    public StatisMachOrderVo(Date exchangeTime, String sfName, String dpName, Double gatherRate, Double utilizeRate, Double performRate, Double yieldRate, Double equFailRate, Integer prepareStay, Integer planutilizeStay, Integer faultStay, Integer qualityStay, Integer mouldStay, Integer manageStay, Integer rearchStay, Integer abrasionStay, Integer factutilizeStay, String mainProblem, List<workBatchVo> workBatchLsit, String classTranscationInfo, Integer otherStay) {
        this.exchangeTime = exchangeTime;
        this.sfName = sfName;
        this.dpName = dpName;
        this.gatherRate = gatherRate;
        this.utilizeRate = utilizeRate;
        this.performRate = performRate;
        this.yieldRate = yieldRate;
        this.equFailRate = equFailRate;
        this.prepareStay = prepareStay;
        this.planutilizeStay = planutilizeStay;
        this.faultStay = faultStay;
        this.qualityStay = qualityStay;
        this.mouldStay = mouldStay;
        this.manageStay = manageStay;
        this.rearchStay = rearchStay;
        this.abrasionStay = abrasionStay;
        this.factutilizeStay = factutilizeStay;
        this.mainProblem = mainProblem;
        this.workBatchLsit = workBatchLsit;
        this.classTranscationInfo = classTranscationInfo;
        this.otherStay = otherStay;
    }
}