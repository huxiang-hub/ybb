package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteExamine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 上报审核表_yb_execute_examine视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteExamineVO对象", description = "修改确认表_yb_execute_examine")
public class ExecuteExamineVO extends ExecuteExamine {
    private static final long serialVersionUID = 1L;
    private Integer esId;


    @ApiModelProperty(value = "工单号")
    private String wbNo;

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "产品编号")
    private String pdCode;

    @ApiModelProperty(value = "接单时间")
    private Date bstartTime;

    @ApiModelProperty(value = "结束时间")
    private Date bendTime;

    @ApiModelProperty(value = "工序名称")
    private String prName;

    @ApiModelProperty(value = "设备名称")
    private String maName;

    @ApiModelProperty(value = "排产数量")
    private Integer wsPlanNum;

    @ApiModelProperty(value = "班次放数")
    private Integer wsWasteNum;

    @ApiModelProperty(value = "排产日期")
    private String wsSdDate;

    @ApiModelProperty(value = "排产班次")
    private String ckName;

    @ApiModelProperty(value = "设备计数")
    private Integer boxNum;

    @ApiModelProperty(value = "上报生产总数")
    private Integer productNum;

    @ApiModelProperty(value = "准备数量")
    private Integer readyNum;

    @ApiModelProperty(value = "上报良品数")
    private Integer countNum;

    @ApiModelProperty(value = "上报废品数")
    private Integer wasteNum;

    @ApiModelProperty(value = "原上报生产总数")
    private Integer alterProductNum;

    @ApiModelProperty(value = "原上报良品数量")
    private Integer alterCountNum;

    @ApiModelProperty(value = "原上报废品数量")
    private Integer alterWasteNum;

    @ApiModelProperty(value = "订单计划数")
    private Integer planNum;

    @ApiModelProperty(value = "工单放数")
    private Integer extraNum;

    @ApiModelProperty(value = "已完成数量")
    private Integer completeNum;

    @ApiModelProperty(value = "上报人名称")
    private String handleUsname;

    @ApiModelProperty(value = "上报日期")
    private Date handleTime;

    @ApiModelProperty(value = "班次状态")
    private Integer shiftStatus;

    @ApiModelProperty(value = "审核状态")
    private Integer exStatus;





    @ApiModelProperty(value = "班次工单id")
    /*班次工单id*/
    private Integer wfId;
    @ApiModelProperty(value = "工单数量")
    /*工单数量*/
    private Integer sdNum;
    @ApiModelProperty(value = "订单名称")
    /*订单名称*/
    private String odName;
    @ApiModelProperty(value = "订单编号")
    /*订单编号*/
    private String odNo;

    @ApiModelProperty(value = "上报人id")
    /*处理时间*/
    private Integer handleUsid;

    @ApiModelProperty(value = "批次数量")
    /*批次数量*/
    private Integer batchNum;

    @ApiModelProperty(value = "部件名称")
    /*部件名称*/
    private String ptName;

    @ApiModelProperty(value = "操作人")
    /*操作人*/
    private String userName;

    @ApiModelProperty(value = "产品尺寸")
    private String pdSize;

    @ApiModelProperty(value = "上机尺寸")
    private String operateSize;

    @ApiModelProperty(value = "原料名称")
    private String materialName;

    /**
     * 订单开始时间
     */
    @ApiModelProperty(value = "订单开始时间")
    private Date startTime;

    /**
     * 订单结束时间
     */
    @ApiModelProperty(value = "订单结束时间")
    private Date endTime;

    @ApiModelProperty(value = "库区id")
    private Integer areaId;

    /**
     * 托盘编号
     */
    @ApiModelProperty(value = "托盘编号")
    private String tdNo;
    /**
     * 第几托
     */
    @ApiModelProperty(value = "第几托")
    private String trayNo;

    @ApiModelProperty(value = "工单id")
    private Integer sdId;

    @ApiModelProperty(value = "设备id")
    /*设备id*/
    private Integer maId;

    @ApiModelProperty(value = "产品编号")
    /*产品编号*/
    private String pdNo;

}
