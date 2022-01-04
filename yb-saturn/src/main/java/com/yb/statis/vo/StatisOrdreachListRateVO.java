package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 设备实时达成率VO
 * @Author my
 * @Date Created in 2020/6/19 17:09
 */
@Data
@ApiModel("设备实时达成率信息VO")
public class StatisOrdreachListRateVO {

    /**
     * id
     */
    private Integer id;

    @ApiModelProperty("小时")
    private Integer hour;

    /**
     * 达成率
     */
    @ApiModelProperty("达成率")
    private BigDecimal reachRate;

    /**
     * 实际数量
     */
    @ApiModelProperty("实际数量")
    private Integer realCount;

    /**
     * 标准数量
     */
    @ApiModelProperty("标准数量")
    private Integer standardNum;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String pdName;

    /**
     * 备注信息
     */
    @ApiModelProperty("备注信息")
    private String remark;

    /**
     * 工单单号
     */
    @ApiModelProperty("工单单号")
    private String wbNo;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String cmName;

    /**
     * 是否锁定 0不锁1锁定
     */
    private Integer reachIslock;

    private Integer planTime;//计划时间

}
