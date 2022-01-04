package com.yb.execute.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yb.execute.entity.ExecuteInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 执行单_yb_execute_info实体类
 *
 * @author by SUMMER
 * @date 2020/4/27.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteInfoVO对象", description = "执行单_yb_execute_info")
public class ExecuteInfoVO extends ExecuteInfo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "上报唯一标识id")
    private Integer bfId;
    @ApiModelProperty(value = "盒子计数")
    private Integer boxNum;
    @ApiModelProperty(value = "上报作业数量")
    private Integer productNum;
    @ApiModelProperty(value = "上报良品数")
    private Integer countNum;
    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;
    @ApiModelProperty(value = "托盘数量")
    private Integer traycardNum;
    @ApiModelProperty(value = "执行唯一标识")
    private Integer exId;
    @ApiModelProperty(value = "操作人名称")
    private String usName;

    @ApiModelProperty(value = "是否上报0未上报1已上报")
    private Integer handle;
    @ApiModelProperty(value = "上报时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;

    @ApiModelProperty(value = "审核状态0、未审核1、通过2、未通过3、部分审核")
    private Integer exStatus;

}
