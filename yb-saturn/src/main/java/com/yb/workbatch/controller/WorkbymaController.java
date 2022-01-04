package com.yb.workbatch.controller;

import com.alibaba.fastjson.JSON;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.workbatch.service.IWorkbymaService;
import com.yb.workbatch.vo.WorkbymaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import  com.yb.workbatch.entity.Workbyma;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VIEW 控制器
 *
 * @author BladeX
 * @since 2021-01-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbyma")
@Api(value = "VIEW", tags = "VIEW接口")
public class WorkbymaController extends BladeController {

	private IWorkbymaService workbymaService;
	private IMachineMainfoService mainfoService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入workbyma")
	public R<WorkbymaVO> detail(WorkbymaVO workbyma) {
		WorkbymaVO detail = workbymaService.getOne(Condition.getQueryWrapper(workbyma));
		return R.data(detail);
	}

	/**
	 * 分页 VIEW
	 */
//	@GetMapping("/list")
//	@ApiOperationSupport(order = 2)
//	@ApiOperation(value = "分页", notes = "传入workbyma")
//	public R<IPage<Workbyma>> list(Workbyma workbyma, Query query) {
//		String sdDate = (workbyma!=null && workbyma.getSdDate()!=null)?workbyma.getSdDate():null;
//		sdDate = (sdDate!=null)?sdDate.substring(0,sdDate.indexOf("T")):null;
//		workbyma.setSdDate(sdDate);
//		IPage<Workbyma> pages = workbymaService.page(Condition.getPage(query), Condition.getQueryWrapper(workbyma));
//		return R.data(pages);
//	}

	/**
	 * 分页 VIEW
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入workbyma")
	public R<IPage<WorkbymaVO>>  listCol(WorkbymaVO workbyma, Query query) {

//		String sdDate = (workbyma!=null && workbyma.getSdDate()!=null)?workbyma.getSdDate():null;
//		sdDate = (sdDate!=null)?sdDate.substring(0,sdDate.indexOf("T")):null;
//		workbyma.setSdDate(sdDate);
		List<MachineMainfo> machineMainfosList=mainfoService.getListAll();
		List<WorkbymaVO> workbymasAll = workbymaService.getAllList(workbyma);
		int length=0;
		Map<Integer,WorkbymaVO> map=new HashMap<>();
		if(!workbymasAll.isEmpty()){
			for(MachineMainfo machineMainfo:machineMainfosList){
				for(WorkbymaVO workbyma1:workbymasAll){
					workbyma1.setId(workbyma1.getExId());
					length++;
					if(machineMainfo.getId().equals(workbyma1.getMaId())) {
						if (!map.containsKey(workbyma1.getMaId())) {
							map.put(machineMainfo.getId(), workbyma1);
						} else {
							WorkbymaVO vo = map.get(workbyma1.getMaId());
							List<Workbyma> wok = vo.getChildren();
							if (wok == null) {
								wok = new ArrayList<>();
							}
							wok.add(workbyma1);
							vo.setChildren(wok);
							map.put(workbyma1.getMaId(), vo);
						}
					}
				}
				if(!map.containsKey(machineMainfo.getId())){
					WorkbymaVO wvo=new WorkbymaVO();
					wvo.setId(machineMainfo.getId());
					wvo.setMaId(machineMainfo.getId());
					wvo.setMaType(machineMainfo.getMaType());
					wvo.setMaSort("未接单生产");
					wvo.setMaName(machineMainfo.getName());
					//wvo.setExId();
					map.put(machineMainfo.getId(),wvo);
				}
			}
		}

		List<WorkbymaVO> list = new ArrayList<WorkbymaVO>(map.values());
		IPage<WorkbymaVO> pages = Condition.getPage(query);
		pages.setRecords(list);
		pages.setTotal(list.size());
		System.out.println(JSON.toJSON(list));
		return R.data(pages);
	}

	/**
	 * 自定义分页 VIEW
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入workbyma")
	public R<IPage<WorkbymaVO>> page(WorkbymaVO workbyma, Query query) {
		IPage<WorkbymaVO> pages = workbymaService.selectWorkbymaPage(Condition.getPage(query), workbyma);
		return R.data(pages);
	}

	/**
	 * 新增 VIEW
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入workbyma")
	public R save(@Valid @RequestBody WorkbymaVO workbyma) {
		return R.status(workbymaService.save(workbyma));
	}

	/**
	 * 修改 VIEW
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入workbyma")
	public R update(@Valid @RequestBody WorkbymaVO workbyma) {
		return R.status(workbymaService.updateById(workbyma));
	}

	/**
	 * 新增或修改 VIEW
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入workbyma")
	public R submit(@Valid @RequestBody WorkbymaVO workbyma) {
		return R.status(workbymaService.saveOrUpdate(workbyma));
	}

	/**
	 * 删除 VIEW
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(workbymaService.removeByIds(Func.toLongList(ids)));
	}

}
