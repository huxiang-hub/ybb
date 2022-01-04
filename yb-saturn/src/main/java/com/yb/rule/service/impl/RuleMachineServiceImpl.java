package com.yb.rule.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.rule.entity.RuleMachine;
import com.yb.rule.mapper.RuleMachineMapper;
import com.yb.rule.service.RuleMachineService;
import com.yb.rule.request.RuleMachineRequest;
import com.yb.rule.response.RuleMachineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/9 9:30
 **/
@Service
public class RuleMachineServiceImpl implements RuleMachineService {

	@Autowired
	private RuleMachineMapper ruleMachineMapper;

	@Override
	public IPage<RuleMachineVo> machineRuleList(IPage<RuleMachineVo> page, RuleMachineRequest request) {
		IPage<RuleMachineVo> machineVoIPage=null;
		if(request.getMaIdList()!=null && request.getMaIdList().size()!=0){
			machineVoIPage=ruleMachineMapper.machineRuleList(page,request);
		}
		return machineVoIPage;
	}

	@Override
	public void deleteRuleMachine(RuleMachineRequest request) {
		if(request.getSelectMaIds()!=null && request.getSelectMaIds().size()>0){
			ruleMachineMapper.deleteRuleMachine(request);
		}
	}

	@Override
	public boolean updateStatus(RuleMachine ruleMachine) {
		int n=0;
		boolean flag=false;
		if(ruleMachine!=null){
			n=ruleMachineMapper.updateStatus(ruleMachine);
		}
		if(n>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public int ruleMachineAdd(List<RuleMachine> ruleMachines) {
		int n=0;
		if (ruleMachines!=null){
			n=ruleMachineMapper.ruleMachineAdd(ruleMachines);
		}
		return n;
	}

}
