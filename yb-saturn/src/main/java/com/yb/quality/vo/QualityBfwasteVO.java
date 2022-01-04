package com.yb.quality.vo;

import com.yb.quality.entity.QualityBfwaste;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "机台自检返回对象")
public class QualityBfwasteVO implements Serializable {

    @ApiModelProperty(value = "排程单id")
    private Integer wfId;
    @ApiModelProperty(value = "执行id")
    private Integer exId;
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "客户名称")
    private String cmName;
//    @ApiModelProperty(value = "自检数据")
//    private List<QualityBfVO> qualityBfVOS;
}
