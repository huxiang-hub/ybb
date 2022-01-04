package com.yb.system.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

/**
 * @Author lzb
 * @Date 2021/3/5 14:54
 **/
@ApiModel("设备授权请求类")
@Data
public class RoleAuthor extends Query {

	@ApiModelProperty("设备Id")
	private int ma_id;

	@ApiModelProperty("岗位")
	private int jobs;

	@ApiModelProperty("用户Id")
	private int us_id;
}
