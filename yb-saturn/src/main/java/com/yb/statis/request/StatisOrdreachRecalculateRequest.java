package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * @Description: 小时达成率重新算请求类
 * @Author my
 * @Date Created in 2020/7/21 15:57
 */

@Data
@ApiModel("小时达成率重新算请求类")
public class StatisOrdreachRecalculateRequest {


    /**
     * 查询日期
     */
    @ApiModelProperty(value = "查询日期(必传)",required = true)
    @NotNull(message = "查询日期不能为空")
    private Date sdDate;

    @ApiModelProperty(value = "班次id(必传)",required = true)
    @NotNull(message = "班次id不能为空")
    private Integer classifyId;
}
