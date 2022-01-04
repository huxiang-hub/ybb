package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/23 14:37
 */
@ApiModel("班次类型列表VO")
@Data
public class ShiftTypeListVO {

    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型 2车间 ，5工序(设备类型)")
    private Integer model;
}
