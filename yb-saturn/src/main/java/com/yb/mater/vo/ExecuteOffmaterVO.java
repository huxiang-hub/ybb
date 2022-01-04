/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yb.mater.vo;

import com.yb.mater.entity.ExecuteOffmater;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料退料管理_yb_execute_offmater视图实体类
 *
 * @author BladeX
 * @since 2021-01-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteOffmaterVO对象", description = "物料退料管理_yb_execute_offmater")
public class ExecuteOffmaterVO extends ExecuteOffmater {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "设备名称")
	private String maName;
	@ApiModelProperty(value = "班次名称")
	private String wsName;
}
