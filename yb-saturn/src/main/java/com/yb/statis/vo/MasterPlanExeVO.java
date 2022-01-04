package com.yb.statis.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/23 20:41
 */
@ApiModel("主计划VO")
@Data
public class MasterPlanExeVO {

    @ApiModelProperty("工序名称")
    private String prName;

    @JsonIgnore
    @ApiModelProperty("计划时间")
    private Integer planTime;

    @ApiModelProperty("剩余时间")
    private BigDecimal lastTime;

    @ApiModelProperty("预交货时间")
    private Date predictDeliveryTime;

    @ApiModelProperty("实际货时间")
    private Date realDeliveryTime;

    @ApiModelProperty("当前时间差")
    private BigDecimal timeDifference;

    @ApiModelProperty("完成百分比")
    private BigDecimal rate;

    @ApiModelProperty("计划总时间")
    private BigDecimal totalPlanTime;

    /**
     * 实际开始时间不返回给前端
     */
    @JsonIgnore
    private Date actuallyStarttime;
}
