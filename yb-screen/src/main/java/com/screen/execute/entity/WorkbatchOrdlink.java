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
 * 生产排产表yb_workbatch_ordlink实体类
 *
 * @author BladeX
 * @since 2021-03-25
 */
@Data
@TableName("yb_workbatch_ordlink")
@ApiModel(value = "WorkbatchOrdlink对象", description = "生产排产表yb_workbatch_ordlink")
public class WorkbatchOrdlink implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键id")
	@TableId(
		value = "id",
		type = IdType.ASSIGN_ID
	)
	private Long id;
	/**
	 * 批次ID
	 */
	@ApiModelProperty(value = "批次ID")
	private Integer wbId;
	/**
	 * 流水号-设备id（可选）
	 */
	@ApiModelProperty(value = "流水号-设备id（可选）")
	private Integer maId;
	/**
	 * 工序id
	 */
	@ApiModelProperty(value = "工序id")
	private Integer prId;
	/**
	 * 工艺路线(系统生成)
	 */
	@ApiModelProperty(value = "工艺路线(系统生成)")
	private String prRoute;
	/**
	 * 上机尺寸
	 */
	@ApiModelProperty(value = "上机尺寸")
	private String operateSize;
	/**
	 * 工序计划总数-作业总数（含放数）
	 */
	@ApiModelProperty(value = "工序计划总数-作业总数（含放数）")
	private Integer planNum;
	/**
	 * 计划放数
	 */
	@ApiModelProperty(value = "计划放数")
	private Integer extraNum;
	/**
	 * 应交数量（达到的最终良品数）
	 */
	@ApiModelProperty(value = "应交数量（达到的最终良品数）")
	private Integer planNumber;
	/**
	 * 完成数量（已完成良品）
	 */
	@ApiModelProperty(value = "完成数量（已完成良品）")
	private Integer completeNum = 0;
	/**
	 * 计划分钟（分钟）
	 */
	@ApiModelProperty(value = "计划分钟（分钟）")
	private Integer planMin;
	/**
	 * 状态（-1外部接入0起草1下发2未完成3已完成9未排完10强制结束）
	 */
	@ApiModelProperty(value = "状态（-1外部接入0起草1下发2未完成3已完成9未排完10强制结束） '")
	private Integer status;
	/**
	 * 待排数量（状态9的数据）
	 */
	@ApiModelProperty(value = "待排数量（状态9的数据）")
	private Integer arrangeNum;
	/**
	 * 是否分版0否1是
	 */
	@ApiModelProperty(value = "是否分版0否1是")
	private Integer isModulus;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;
	/**
	 * 工序排序
	 */
	@ApiModelProperty(value = "工序排序")
	private Integer sort;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateAt;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createAt;


}
