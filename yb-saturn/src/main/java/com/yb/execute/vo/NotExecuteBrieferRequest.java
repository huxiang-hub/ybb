package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "查询未上报数据参数对象")
public class NotExecuteBrieferRequest implements Serializable {

    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "设备列表")
    private List<Integer> maIdList;
}
