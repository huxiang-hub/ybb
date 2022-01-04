package com.yb.workbatch.vo;

import  com.yb.workbatch.entity.Workbyma;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.springblade.core.tool.node.INode;

import java.util.List;

/**
 * VIEW视图实体类
 *
 * @author BladeX
 * @since 2021-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbymaVO对象", description = "VIEW")
public class WorkbymaVO extends Workbyma {
	private static final long serialVersionUID = 1L;

	/*
	 * id
	 * */
	@ApiModelProperty(value = "ID")
	private Integer id;


	List<Workbyma> children;

}
