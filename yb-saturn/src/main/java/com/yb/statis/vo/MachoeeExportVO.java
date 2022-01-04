package com.yb.statis.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 处理导出所需数据格式
 */
@Data
public class MachoeeExportVO implements Serializable {
    /*一个设备一个sheet*/
    private String maName;
    /*表数据*/
    private List<MachoeeExcelExportVO> machoeeExcelExportVOList;
    /*---------计算总的值-------*/
    /*生产准备和*/
    private Integer prepareStaySum;
    /*设备故障和*/
    private Integer faultStaySum;
    /*品质故障和*/
    private Integer qualityStaySum;
    /*计划安排停机和*/
    private Integer planStaySum;
    /*管理停止和*/
    private Integer manageStaySum;
    /*磨损更换和*/
    private Integer abrasionStaySum;
    /*产品切换和*/
    private Integer mouldStaySum;
    /*休息吃饭和*/
    private Integer restStaySum;
    /*A正常出勤时间和*/
    private Integer workStaySum;
    /*可用稼动时间和*/
    private Integer standardRuntimeSum;
    /*计划稼动时间和*/
    private Integer planutilizeStaySum;
    /*实际稼动时间和*/
    private Integer factutilizeStaySum;
    /*应产数和*/
    private Integer taskCountSum;
    /*良品数和*/
    private Integer nodefectCountSum;
    /*作业数<张和*/
    private Integer workCountSum;
    /*实际能力生产性<张/h>和*/
    private Integer factSpeedSum;
    /*理论能力生产性和（张/h)*/
    private Integer normalSpeedStaySum;
    /*时间稼动率E*/
    private Double utilizeRateAvg;
    /*良品率G*/
    private Double yieldRateAvg;
    /*性能稼动率J*/
    private Double performRateAvg;
    /*OEE设备综合效率K*/
    private Double gatherRateAvg;

}
