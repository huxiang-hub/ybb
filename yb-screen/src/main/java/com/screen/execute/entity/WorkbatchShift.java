package com.screen.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 排产班次设定_yb_workbatch_shift实体类
 *
 * @author BladeX
 * @since 2021-03-15
 */
@Data
@TableName("yb_workbatch_shift")
@ApiModel(value = "WorkbatchShift对象", description = "排产班次设定_yb_workbatch_shift")
public class WorkbatchShift implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键id")
	@TableId(
		value = "id",
		type = IdType.ASSIGN_ID
	)
	private Long id;

	/**
	 * 设备id
	 */
	@ApiModelProperty(value = "设备id")
	private Integer maId;
	/**
	 * 排产id
	 */
	@ApiModelProperty(value = "排产id")
	private Integer sdId;
	/**
	 * 班次id
	 */
	@ApiModelProperty(value = "班次id")
	private Integer wsId;
	/**
	 * 排班名称 白班/夜班/晚班/
	 */
	@ApiModelProperty(value = "排班名称 白班/夜班/晚班/")
	private String wsName;
	/**
	 * 排产顺序
	 */
	@ApiModelProperty(value = "排产顺序")
	private String sdSort;
	/**
	 * 排产日期
	 */
	@ApiModelProperty(value = "排产日期")
	private String sdDate;
	/**
	 * 预排排产顺序
	 */
	@ApiModelProperty(value = "预排排产顺序")
	private String advDate;
	/**
	 * 产品id
	 */
	@ApiModelProperty(value = "产品id")
	private Integer pdId;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "客户id")
	private Integer cmId;
	/**
	 * 计划排产生产数（含放数；拆单需要有放数比例）
	 */
	@ApiModelProperty(value = "计划排产生产数（含放数；拆单需要有放数比例）")
	private Integer planNum;
	/**
	 * 放数（废品数orlink的比例数）
	 */
	@ApiModelProperty(value = "放数（废品数orlink的比例数）")
	private Integer wasteNum;
	/**
	 * 计划时间(分钟)
	 */
	@ApiModelProperty(value = "计划时间(分钟)")
	private Integer planTime;
	/**
	 * 标准产能（小时）
	 */
	@ApiModelProperty(value = "标准产能（小时）")
	private Integer speed;
	/**
	 * 排产换模时间（分钟）
	 */
	@ApiModelProperty(value = "排产换模时间（分钟）")
	private Integer mouldShift;
	/**
	 * 标准换模时间（分钟）
	 */
	@ApiModelProperty(value = "标准换模时间（分钟）")
	private Integer mouldStay;
	/**
	 * 开始生产时间（排产）
	 */
	@ApiModelProperty(value = "开始生产时间（排产）")
	private Date proBeginTime;
	/**
	 * 完成生产时间（排产）
	 */
	@ApiModelProperty(value = "完成生产时间（排产）")
	private Date proFinishTime;
	/**
	 * 备注，不是生产类型备注说明
	 */
	@ApiModelProperty(value = "备注，不是生产类型备注说明")
	private String remark;
	/**
	 * 状态：-1未下发，0:待接单，1：生产中，2：生产完成（已上报）  3：未上报（结束生产） 4：未完成（已上报）
	 */
	@ApiModelProperty(value = "状态：-1未下发，0:待接单，1：生产中，2：生产完成（已上报）  3：未上报（结束生产） 4：未完成（已上报）")
	private Integer shiftStatus;
	/**
	 * 计划类型：1生产默认、2计划保养B-B2、3计划停机C-C2
	 */
	@ApiModelProperty(value = "计划类型：1生产默认、2计划保养B-B2、3计划停机C-C2")
	private String planType;
	/**
	 * 类型事件C-C2-R1、B-B2-P1
	 */
	@ApiModelProperty(value = "类型事件C-C2-R1、B-B2-P1")
	private String typeEvent;
	/**
	 * 排产人员
	 */
	@ApiModelProperty(value = "排产人员")
	private Integer usId;
	/**
	 * 是否自动接单0,否1,是（机台登录当前班次就自动接单）
	 */
	@ApiModelProperty(value = "是否自动接单0,否1,是（机台登录当前班次就自动接单）")
	private Integer isAuto;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateAt;
}
