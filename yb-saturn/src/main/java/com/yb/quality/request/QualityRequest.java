package com.yb.quality.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "查询质量巡检参数对象")
public class QualityRequest extends Query implements Serializable {

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "检查类型: first_check 首检、round_check 巡检、self_check 自检", required = true)
    private String checkType;

    @ApiModelProperty("工单号")
    private String wbNo;

    @ApiModelProperty("巡检人")
    private String usName;

    @ApiModelProperty(value = "设备id集合")
    private List<Integer> maIdList;

    @ApiModelProperty(value = "工序分类id", required = true)
    private List<Integer> pyIds;
}
