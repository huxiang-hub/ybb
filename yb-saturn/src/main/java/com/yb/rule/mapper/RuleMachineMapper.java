package com.yb.rule.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.rule.entity.RuleMachine;
import com.yb.rule.request.RuleMachineRequest;
import com.yb.rule.response.RuleMachineVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/9 8:37
 **/
@Mapper
public interface RuleMachineMapper {
	/**
	 *设备模式规则集合
	 **/
	IPage<RuleMachineVo> machineRuleList(IPage<RuleMachineVo> page, @Param("request") RuleMachineRequest request);

	/**
	 * 批量删除
	 * */
	void deleteRuleMachine(@Param("request") RuleMachineRequest request);

	/**
	 * 设备模式规则状态修改
	 * */
	int updateStatus(@Param("ruleMachine") RuleMachine ruleMachine);

	/**
	 * 添加设备的模式
	 * */
	int ruleMachineAdd(@Param("ruleMachines") List<RuleMachine> ruleMachines);

	/**
	 * 通过设备id和规则类型进行查询
	 * */
	int tableCount(@Param("maId") int maId,@Param("rmType") String rmType);
}
