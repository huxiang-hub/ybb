package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_execute_traycard")
public class ExecuteTraycard implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /*工单ID*/
    @ApiModelProperty(value = "工单ID")
    private Integer sdId;
    /*排产单ID*/
    @ApiModelProperty(value = "排产单ID")
    private Integer wfId;
    /*第几台*/
    @ApiModelProperty(value = "第几台")
    private Integer trayNo;
    /*本台计数*/
    @ApiModelProperty(value = "本台计数")
    private Integer trayNum;
    /*计划总数*/
    @ApiModelProperty(value = "计划总数")
    private Integer planNum;
    /*总台数*/
    @ApiModelProperty(value = "总台数")
    private Integer totalNum;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "执行id")
    private Integer exId;
    /*打印次数*/
    @ApiModelProperty(value = "打印次数")
    private Integer printNum;
    /*托板占用数量*/
    @ApiModelProperty(value = "托板占用数量")
    private Integer layNum;
    /*库位id*/
    @ApiModelProperty(value = "库位id")
    private Integer mpId;
    /*备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "托盘编号")
    private String tdNo;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /*库位名称*/
    @ApiModelProperty(value = "库位名称")
    private String storePlace;
    /*操作人*/
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    /*创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /*更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    @ApiModelProperty(value = "工序名")

    @TableField(exist = false)
    private String prName;

    @ApiModelProperty(value = "0、生成（占位）1、入库2、出库")
    private Integer tyStatus;

    @ApiModelProperty(value = "0 未审核 1 通过")
    private Integer exStatus;

//    @ApiModelProperty(value = "审核前库位编码")
//    private String storeBefore;
//
//    @ApiModelProperty(value = "审核后库位编码")
//    private String storeAfter;

    @ApiModelProperty(value = "上报备注，编写理由，图片信息")
    private String reportMark;

}
