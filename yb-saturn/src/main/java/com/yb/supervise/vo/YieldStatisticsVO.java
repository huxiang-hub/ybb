package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/31 13:55
 */
@ApiModel("印聊产能统计vo")
@Data
public class YieldStatisticsVO {

    @ApiModelProperty("总计划产量")
    private Integer totalPlanNum = 0;

    @ApiModelProperty("总完成产量")
    private Integer totalRelyNum = 0;

    @ApiModelProperty("总达成率")
    private BigDecimal totalRate ;

    @ApiModelProperty("产能分析信息")
    private List<YieldStatisticsListVO> statisticsListVOList;
}
