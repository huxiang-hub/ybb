package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 执行单_yb_execute_info实体类
 *
 * @author by SUMMER
 * @date 2020/4/27.
 */
@Data
@TableName("yb_execute_info")
@ApiModel(value = "ExecuteInfo对象", description = "执行单_yb_execute_info")
public class ExecuteInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "设备唯一标识")
    private Integer maId;   //设备id
    @ApiModelProperty(value = "工单排产唯一标识")
    private Integer sdId;    //排产id
    @ApiModelProperty(value = "排程id唯一主键")
    private Integer wfId;   //排产班次id

    //当前排产当次的开始时间
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    //当前排产当次的结束时间
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    //排产单的执行状态
    @ApiModelProperty(value = "执行状态：状态：0、接单1、执行中2、执行完成3、执行结束")
    private Integer status;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    //更新时间
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    //班次Id
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    //    //实际的班次开始时间
//    private Date sfStartTime;
//    //实际的班次结束时间
//    private Date sfEndTime;
    //C1开始时间
    @ApiModelProperty(value = "准备时间")
    private Date exeTime;
    //执行日期 2020-06-20
    @ApiModelProperty(value = "执行日期")
    private String targetDay;

    @ApiModelProperty(value = "操作人-本地操作id或者外部接口操作ID人员")
    private String usId;

    @ApiModelProperty(value = "工单号")
    private String wbNo;
}
