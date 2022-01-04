package com.yb.quality.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "机台自检数据")
public class QualityBfVO implements Serializable {


    private Integer id;
    @ApiModelProperty(value = "执行id")
    private Integer exId;
    @ApiModelProperty(value = "上报状态0未上报1已上报")
    private Integer reportStatus;
    @ApiModelProperty(value = "废品类型名称")
    private String wasteType;
    @ApiModelProperty(value = "废品类型id")
    private String wasteTypeId;
    @ApiModelProperty(value = "生产数量")
    private Integer productNum;
    @ApiModelProperty(value = "抽检数量")
    private Integer quantityDeclared;
    @ApiModelProperty(value = "废品数量")
    private Integer wasteNum;
    @JsonFormat(pattern="HH:mm")
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    @JsonFormat(pattern="HH:mm")
    @ApiModelProperty(value = "结束时间")
    private Date endAt;
    @ApiModelProperty(value = "总耗时(秒)")
    private Double hourTotal;

}
