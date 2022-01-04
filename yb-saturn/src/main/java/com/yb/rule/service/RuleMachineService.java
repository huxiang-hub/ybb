package com.yb.rule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.rule.entity.RuleMachine;
import com.yb.rule.request.RuleMachineRequest;
import com.yb.rule.response.RuleMachineVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author lzb
 * @Date 2021/3/9 9:29
 **/
@Component
public interface RuleMachineService {

	IPage<RuleMachineVo> machineRuleList(IPage<RuleMachineVo> page, @Param("request") RuleMachineRequest request);

	/**
	 * 批量删除
	 * */
	void deleteRuleMachine(@Param("request") RuleMachineRequest request);

	/**
	 * 设备模式规则状态修改
	 * */
	boolean updateStatus(@Param("ruleMachine") RuleMachine ruleMachine);

	/**
	 * 添加设备的模式
	 * */
	int ruleMachineAdd(@Param("ruleMachines") List<RuleMachine> ruleMachines);
}
