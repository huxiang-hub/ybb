package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

/**
 * @Description: 班次设定分页请求
 * @Author my
 * @Date Created in 2020/10/23 13:54
 */
@ApiModel("班次设定分页请求")
@Data
public class WorkbatchShiftsetPageRequest  extends Query {

    @ApiModelProperty("分类：1公司2车间部门3工序4设备5设备类型")
    private Integer model;

    @ApiModelProperty("分类对应的id")
    private Integer dbId;
}
