package com.yb.execute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlanMachineVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "计划准备时间")
    private List<PrepareVO> prepareVOList;//计划准备时间
    @ApiModelProperty(value = "计划排产单时间")
    private List<ProductionVO> productionVOList;//计划排产单时间
    @ApiModelProperty(value = "计划吃饭时间")
    private List<MealStayVO> MealStayVOList;//计划吃饭时间
}
