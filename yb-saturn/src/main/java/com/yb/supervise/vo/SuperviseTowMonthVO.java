package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2020/11/16 17:37
 **/
@Data
@ApiModel(value = "上个月及本月设备生产数量统计vo")
public class SuperviseTowMonthVO {
    @ApiModelProperty("设备id")
    private Integer id;
    @ApiModelProperty("设备名称")
    private String maName;
    @ApiModelProperty("上月数量")
    private Integer lastMonthSum;
    @ApiModelProperty("本月数量")
    private Integer curMonthSum;
}
