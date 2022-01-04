package com.yb.rule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.rule.entity.RuleMachine;
import com.yb.rule.mapper.RuleMachineMapper;
import com.yb.rule.request.RuleMachineRequest;
import com.yb.rule.response.RuleMachineVo;
import com.yb.rule.service.RuleMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/9 8:36
 **/
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/ruleMachine")
@Api(tags = "设备规则")
public class RuleMachineController extends BladeController {

	@Autowired
	private RuleMachineService ruleMachineService;
	@Autowired
	private RuleMachineMapper ruleMachineMapper;
	/**
	 * 获取设备规则表集合
	 * */
	@RequestMapping("/getRuleMachine")
	@ApiOperation(value = "获取设备模式规则信息")
	public R<IPage<RuleMachineVo>> getRuleMachine(@RequestBody RuleMachineRequest request){
		IPage<RuleMachineVo> rList=null;
		if(request.getMaIdList()!=null && request.getMaIdList().size()!=0){
			rList =ruleMachineService.machineRuleList(Condition.getPage(request),request);
		}
		return R.data(rList);
	}

	/**
	 * 修改状态
	 * */
	@RequestMapping("/updateStatus")
	@ApiOperation(value = "修改设备模式规则状态信息")
	public R updateStatus(@RequestBody RuleMachine ruleMachine) {
		boolean flag=false;
		if(ruleMachine!=null){
			flag=ruleMachineService.updateStatus(ruleMachine);
		}
		if(flag){
			return R.success("状态已更改");
		}
		return R.fail("状态更改失败！");
	}

	/**
	 * 添加
	 * */
	@RequestMapping("/ruleMachineAdd")
	@ApiOperation(value = "添加设备模式规则")
	public R ruleMachineAdd(@RequestBody RuleMachineRequest request){
		boolean flag=false;
		int n=0;
		List<RuleMachine> ruleMachineList= new ArrayList<>();
		if(request!=null){
			for(int i=0;i<request.getMaIdList().size();i++){
				if(ruleMachineMapper.tableCount(request.getMaIdList().get(i),request.getRmType())>0){
					return R.fail("设备已存在该规则类型！");
				}
						RuleMachine ruleMachine = new RuleMachine();
						ruleMachine.setMaId(request.getMaIdList().get(i));
						ruleMachine.setRmType(request.getRmType());
						ruleMachine.setIsUsed(request.getStatus());
						ruleMachine.setUsId(SaSecureUtil.getUserId());
						ruleMachineList.add(ruleMachine);
				}
			}
		if(ruleMachineList.size()>0){
			n=ruleMachineMapper.ruleMachineAdd(ruleMachineList);
		}
		if(n>0){
			return R.success("添加成功");
		}
		return R.fail("添加失败");
	}

	/**
	 * 删除
	 * */

	@PostMapping("/deleteRuleMachine")
	@ApiOperation(value = "删除")
	public R deleteRuleMachine(@RequestBody RuleMachineRequest request) {
		if(request!=null){
			ruleMachineService.deleteRuleMachine(request);
		}
		return R.success("删除成功");
	}
}
