package com.yb.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/9 9:16
 **/
@Data
@ApiModel(value = "获取设备Id参数对象")
public class RuleMachineRequest extends Query implements Serializable {

	@ApiModelProperty("设备Ids")
	private List<Integer> maIdList;

	@ApiModelProperty("用户Ids")
	private List<Integer> usIdList;

	@ApiModelProperty("选择的设备id")
	private List<Integer> selectMaIds;

	@ApiModelProperty("删除：规则类型集合")
	private List<String> rmTypes;

	@ApiModelProperty("类型分类")
	private String rmType;

	@ApiModelProperty("状态")
	private int status;
}
