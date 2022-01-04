package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "排产详情图返回的排产数据对象")
public class WorkbatchShiftDetailVO implements Serializable {

    @ApiModelProperty(value = "排产单状态 (0起草1发布2正在生产3已完成4已挂起5废弃 -1待排产ERP接入 6驳回7已排产8部分完成9未排完)")
    private Integer status;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "批次")
    private String wbNo;
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    @ApiModelProperty(value = "完成数")
    private Integer finishNum;
    @ApiModelProperty(value = "开始生产时间")
    private Date proBeginTime;
    @ApiModelProperty(value = "完成时间")
    private Date proFinishTime;
    @ApiModelProperty(value = "标准产能")
    private Integer speed;
    @ApiModelProperty(value = "生产准备时长")
    private Integer mouldStay;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
}
