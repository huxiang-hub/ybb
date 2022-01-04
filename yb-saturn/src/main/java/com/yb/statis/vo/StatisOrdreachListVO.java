package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 实时达成率VO
 * @Author my
 * @Date Created in 2020/6/19 14:25
 */
@Data
@ApiModel("小时达成率lisvo")
public class StatisOrdreachListVO implements Serializable {

    @ApiModelProperty("设备名")
    private String name;

    List<StatisOrdreachListRateVO> statisOrdreachListRateVOS;
}
