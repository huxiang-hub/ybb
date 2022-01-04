package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

import java.util.Date;

/**
 * @Description: 主计划查询请求
 * @Author my
 * @Date Created in 2020/7/23 19:30
 */
@ApiModel("主计划查询请求")
@Data
public class MasterPlanRequest extends Query {

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String odNo;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    private Integer prId;

    /**
     * 查询时间
     */
    @ApiModelProperty("查询时间")
    private Date queryTime;

}
