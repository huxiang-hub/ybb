package com.yb.rule.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lzb
 * @Date 2021/3/9 8:59
 **/
@ApiModel("设备模式规则分页VO")
@Data
public class RuleMachineVo {

	@ApiModelProperty("主键")
	private int id;

	@ApiModelProperty("设备名称")
	private String maName;

	@ApiModelProperty("设备ID")
	private int maId;

	@ApiModelProperty("规则类型1、自动打印2按工序排产 数据字典：maRule")
	private String rmType;

	@ApiModelProperty("操作人")
	private String  usName;

	@ApiModelProperty("用户id")
	private int usId;

	@ApiModelProperty("状态：1.启用 0.禁用")
	private int isUsed;

	@ApiModelProperty("创建时间")
	private Date createAt;

	@ApiModelProperty("更新时间")
	private Date updateAt;

}
