package com.yb.rule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.TenantEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lzb
 * @Date 2021/3/9 16:26
 **/
@Data
@TableName("yb_rule_machine")
public class RuleMachine implements Serializable {

	@ApiModelProperty("主键")
	private int id;

	/**
	 * 设备id
	 * */
	private int maId;

	/**
	 * 规则类型
	 * */
	private String rmType;

	/**
	 * 用户id
	 * */
	private int usId;

	/**
	 * 状态
	 * */
	private int isUsed;

	/**
	 * 创建时间
	 * */
	private Date createAt;

	/**
	 * 更新时间
	 * */
	private Date updateAt;
}
