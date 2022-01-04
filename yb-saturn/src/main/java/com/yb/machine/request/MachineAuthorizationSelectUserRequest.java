package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.support.Query;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/25 9:23
 **/
@ApiModel("设备授权用户list请求类")
@Data
public class MachineAuthorizationSelectUserRequest extends Query{

	@ApiModelProperty("用户姓名")
	private String name;

	@ApiModelProperty("车间id")
	private int dpId;

	@ApiModelProperty("班组id")
	private int bcId;

}
