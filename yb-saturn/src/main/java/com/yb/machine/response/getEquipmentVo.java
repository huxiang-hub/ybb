package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.core.mp.support.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/25 9:44
 **/
@Data
@ApiModel(value = "获取设备Id参数对象")
public class getEquipmentVo extends Query implements Serializable {

	@Autowired
	private List<Integer> maIdList;

	@Autowired
	private List<Integer> usIdList;

	@Autowired
	private List<String> maNameList;
}
