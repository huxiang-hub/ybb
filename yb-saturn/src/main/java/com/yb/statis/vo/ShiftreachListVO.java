package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "班次设备达成率返回数据对象")
public class ShiftreachListVO implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "机台名称")
    private String maName;
    @ApiModelProperty(value = "日期")
    private String targetDate;
    @ApiModelProperty(value = "班次名称")
    private String wsName;
    @ApiModelProperty(value = "机长")
    private String usName;
    @ApiModelProperty(value = "计划数")
    private Integer planNum;
    @ApiModelProperty(value = "领用数")
    private Integer productNum;
    @ApiModelProperty(value = "正品数")
    private Integer countNum;
    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;
    @ApiModelProperty(value = "计划达成率")
    private Double rateNum;
    @ApiModelProperty(value = "废品率")
    private Double wasteRate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "备注")
    private byte[] image;
}
