package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/26 11:29
 */
@ApiModel("排产修改小时达成率计划数")
@Data
public class WorkBatchOrdReachUpdateRequest {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "达成率id不能为空")
    private Integer id;

    @ApiModelProperty(value = "小时计划数量")
    private Integer planCount;

}
