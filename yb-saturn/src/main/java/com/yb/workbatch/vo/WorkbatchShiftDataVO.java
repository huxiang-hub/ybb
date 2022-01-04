package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "根据排产id查询排产的详情信息")
public class WorkbatchShiftDataVO {

    @ApiModelProperty(value = "工单工序唯一id")
    Integer sdId;
    @ApiModelProperty(value = "排产id")
    Integer wfId;
    @ApiModelProperty(value = "工序ID")
    Integer prId;
    @ApiModelProperty(value = "设备id；可以为空")
    Integer maId;
    @ApiModelProperty(value = "工单编号")
    String wbNo;
    @ApiModelProperty(value = "产品名称")
    Integer pdId;
    @ApiModelProperty(value = "排产班次id")
    Integer wsId;
    @ApiModelProperty(value = "排产班次名称")
    String wsName;
    @ApiModelProperty(value = "产品名称")
    String pdName;
    @ApiModelProperty(value = "设备名称")
    String maName;
    @ApiModelProperty(value = "客户名称缩写")
    String cmName;


    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "排产计划数量")
    private Integer planNum;
    @ApiModelProperty(value = "该排产单已完成数")
    private Integer finishNum;
    @ApiModelProperty(value = "该排产单已完成数")
    private Integer wasteNum;

    @ApiModelProperty(value = "该工单的计划数量")
    Integer wbPlanNum;
    @ApiModelProperty(value = "工单的计划废品数")
    private Integer wbWasteNum;
    @ApiModelProperty(value = "该工单已完成的数量多少")
    Integer completeNum;

    @ApiModelProperty(value = "排产单状态:状态：-1未下发，0:待接单，1：生产中，2：生产完成（已上报）  3：未上报（结束生产） 4：未完成（已上报）")
    Integer shiftStatus;
    @ApiModelProperty(value = "是否自动接单0,否1,是")
    Integer isAuto = 0;

    @ApiModelProperty(value = "开始生产时间")
    private Date proBeginTime;
    @ApiModelProperty(value = "完成时间")
    private Date proFinishTime;
    @ApiModelProperty(value = "总共需要花费的总时间分钟数")
    private Integer planTime;
    @ApiModelProperty(value = "标准产能")
    private Integer speed;
    @ApiModelProperty(value = "生产准备时长")
    private Integer mouldStay;

}
