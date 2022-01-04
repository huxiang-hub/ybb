package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/13
 **/
@Data
@ApiModel("上报废品vo")
public class ExecuteWastReportVO {

    @ApiModelProperty(value = "执行单id")
    private Integer bfId;

    @ApiModelProperty(value = "当前生产工序id")
    private Integer exPrid;

    @ApiModelProperty(value = "抽检人员ID")
    private Integer usId;

    @ApiModelProperty(value = "质检类型1订单巡检2车间巡检3公司巡检4上报废品")
    private String wsType;

    @ApiModelProperty("废品-数量集合")
    List<WastTypeSum> wastTypeSumList;

}
