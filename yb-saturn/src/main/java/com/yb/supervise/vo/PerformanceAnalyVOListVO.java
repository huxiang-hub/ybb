package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 印聊绩效分析listvo
 * @Author my
 * @Date Created in 2020/7/31 16
 */
@ApiModel("印聊绩效分析listvo")
@Data
public class PerformanceAnalyVOListVO {

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("良品数")
    private Integer completNum;

    @ApiModelProperty("废品数")
    private Integer wasteNum;

    @ApiModelProperty("所属时间")
    private String date;
}
