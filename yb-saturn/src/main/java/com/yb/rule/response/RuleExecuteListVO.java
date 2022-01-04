package com.yb.rule.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/23 16:36
 */
@Data
@ApiModel("换膜/产能规则设定List VO")
public class RuleExecuteListVO {

    private Integer Id;

    @ApiModelProperty("条件内容json")
    private String condition;

    @ApiModelProperty("换膜时间")
    private Integer mouldTime;

    @ApiModelProperty("标准时速（小时）标准产能")
    private Integer capacity;
}
