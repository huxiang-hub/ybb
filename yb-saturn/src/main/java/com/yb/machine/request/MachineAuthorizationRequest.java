package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/26 15:46
 **/
@ApiModel("设备授权请求类")
@Data
public class MachineAuthorizationRequest extends Query {

	@ApiModelProperty("设备Id")
	private List<Integer> maIds;

	@ApiModelProperty("岗位")
	private List<Integer> jobs;

	@ApiModelProperty("用户Id")
	private List<Integer> usIds;
}
