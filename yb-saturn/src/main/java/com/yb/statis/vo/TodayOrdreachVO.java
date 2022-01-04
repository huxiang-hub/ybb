package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2020/10/31 18:42
 **/
@Data
@ApiModel(value = "当天达成率汇总vo")
public class TodayOrdreachVO {
    @ApiModelProperty("设备类型")
    private String MaType;
    @ApiModelProperty("当天实际总数量")
    private Integer realNum;
    @ApiModelProperty("当天计划总数量")
    private Integer planNum;
}
