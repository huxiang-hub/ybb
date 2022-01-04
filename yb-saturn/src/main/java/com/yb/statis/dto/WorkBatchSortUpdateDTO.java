package com.yb.statis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/26 12:11
 */
@ApiModel("班次排序修改传输对象")
@Data
public class WorkBatchSortUpdateDTO {

    @ApiModelProperty(value = "sdId", required = true)
    @NotNull(message = "sdId不能为空")
    private Integer sdId;

    @ApiModelProperty("排序")
    private String sdSort;

}
