package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "在制品工序相关信息")
public class ArticlesProcessVO implements Serializable {
    private Integer sdId;
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    @ApiModelProperty(value = "批次编号")
    private String wbNo;
    @ApiModelProperty(value = "工序排序")
    private Integer sortNum;
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    @ApiModelProperty(value = "作业数")
    private Integer planNum;
    @ApiModelProperty(value = "计划数")
    private Integer planNumber;
    @ApiModelProperty(value = "完成数(良品数)")
    private Integer completeNum;
    @ApiModelProperty(value = "废品数")
    private Integer waste;
    @ApiModelProperty(value = "放数")
    private Integer extraNum;
    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完10强制结束")
    private String status;

}
