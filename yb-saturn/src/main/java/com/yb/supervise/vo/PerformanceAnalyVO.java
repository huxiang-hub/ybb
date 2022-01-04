package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 印聊绩效分析vo
 * @Author my
 * @Date Created in 2020/7/31 14:55
 */
@ApiModel("印聊绩效分析vo")
@Data
public class PerformanceAnalyVO {

    @ApiModelProperty("产能统计")
    private Integer statisticsNum;

    @ApiModelProperty("良品数")
    private Integer goodNum;

    @ApiModelProperty("废品数")
    private Integer wasteNum;

    @ApiModelProperty("绩效分析信息")
    private List<PerformanceAnalyVOListVO> performanceAnalyVOListVOS;
}
