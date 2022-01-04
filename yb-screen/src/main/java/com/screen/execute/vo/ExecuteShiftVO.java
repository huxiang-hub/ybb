package com.screen.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工单执行运行界面接口", description = "机台正在执行的状态和当前数据信息")
public class ExecuteShiftVO {
    /**********************扩展信息****正在生产状态时才有如下信息****************/
    @ApiModelProperty(value = "执行Id唯一标识")
    Integer exId;
    @ApiModelProperty(value = "工单的排程计划数量")
    Integer shiftPlanNum;
    @ApiModelProperty(value = "当前工单计数；从工单开始")
    Integer currNum;
    @ApiModelProperty(value = "开始盒子计数信息")
    Integer startNum;
    @ApiModelProperty(value = "当前盒子的计数")
    Integer boxNum;

    @ApiModelProperty(value = "当前工单执行入库数量，以exId查询结果")
    Integer inStoreNum;
    @ApiModelProperty(value = "待报工数量；表示为打印卡的后计数")
    Integer waiteStoreNum;

    @ApiModelProperty(value = "工单停机总时长exId，按照秒统计：页面合计为分钟数")
    Integer prepareStay;
    @ApiModelProperty(value = "停机次数；生产准备次数")
    Integer prepareTime;
    @ApiModelProperty(value = "停机过程中的计数器的计数数据")
    Integer prepareNum;
    @ApiModelProperty(value = "运行时长，按照秒进行统计，页面合计为分钟数")
    Integer runStay;
    @ApiModelProperty(value = "运行阶段的计数信息数据")
    Integer runNum;

}
