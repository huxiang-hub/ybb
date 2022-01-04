/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Supplier;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.vo.TraycardTextVO;
import com.yb.mater.entity.ExecuteMaterials;
import com.yb.mater.entity.ExecuteOffmater;
import com.yb.mater.service.IExecuteMaterialsService;
import com.yb.mater.service.IExecuteOffmaterService;
import com.yb.mater.vo.ExecuteMaterialsVO;
import com.yb.mater.vo.ExecuteOffmaterVO;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.IStoreOutlogService;
import com.yb.stroe.service.StoreSeatService;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springblade.core.boot.ctrl.BladeController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * 物料退料管理_yb_execute_offmater 控制器
 *
 * @author BladeX
 * @since 2021-01-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executeoffmater")
@Api(value = "物料退料管理_yb_execute_offmater", tags = "物料退料管理_yb_execute_offmater接口")
public class ExecuteOffmaterController extends BladeController {

	private final IExecuteOffmaterService executeOffmaterService;
	@Autowired
	private IExecuteMaterialsService executeMaterialsService;
	@Autowired
	private IStoreInlogService storeInlogService;
	@Autowired
	private ExecuteTraycardService executeTraycardService;
	@Autowired
	private IStoreInventoryService storeInventoryService;
	@Autowired
	private ISuperviseExecuteService superviseExecuteService;
	@Autowired
	private IWorkbatchOrdlinkService workbatchOrdlinkService;
	@Autowired
	private StoreSeatService seatService;
	@Autowired
	private IStoreOutlogService storeOutlogService;
	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入executeOffmater")
	public R<ExecuteOffmater> detail(ExecuteOffmater executeOffmater) {
		ExecuteOffmater detail = executeOffmaterService.getOne(Condition.getQueryWrapper(executeOffmater));
		return R.data(detail);
	}

	/**
	 * 物料退料管理_yb_execute_offmater
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "根据工单id查询下料信息")
	public R<List<ExecuteOffmater>> list(ExecuteOffmater executeOffmater) {
		List<ExecuteOffmater> pages = executeOffmaterService.getListByWfId(executeOffmater.getWfId());
		return R.data(pages);
	}

	/**
	 * 物料退料管理_yb_execute_offmater
	 */
	@GetMapping("/listcs")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "根据工单id查询下料信息(工单详情扩展接口)")
	public R<List<ExecuteOffmaterVO>> listcs(ExecuteOffmater executeOffmater) {
		List<ExecuteOffmaterVO> pages = executeOffmaterService.getListcsByWfId(executeOffmater.getWfId());
		return R.data(pages);
	}

	/**
	 * 自定义分页 物料退料管理_yb_execute_offmater
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入executeOffmater")
	public R<IPage<ExecuteOffmaterVO>> page(ExecuteOffmaterVO executeOffmater, Query query) {
		IPage<ExecuteOffmaterVO> pages = executeOffmaterService.selectExecuteOffmaterPage(Condition.getPage(query), executeOffmater);
		return R.data(pages);
	}

	/**
	 * 新增 物料退料管理_yb_execute_offmater
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入executeOffmater")
	public R save(@Valid @RequestBody ExecuteOffmater executeOffmater) {
		return R.status(executeOffmaterService.save(executeOffmater));
	}

	/**
	 * 修改 物料退料管理_yb_execute_offmater
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入executeOffmater")
	public R update(@Valid @RequestBody ExecuteOffmater executeOffmater) {
		return R.status(executeOffmaterService.updateById(executeOffmater));
	}

	/**
	 * 新增或修改 物料退料管理_yb_execute_offmater
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入executeOffmater")
	public R submit(@Valid @RequestBody ExecuteOffmater executeOffmater) {
		return R.status(executeOffmaterService.saveOrUpdate(executeOffmater));
	}


	/**
	 * 删除 物料退料管理_yb_execute_offmater
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(executeOffmaterService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 物料退料
	 * @param material
	 * @return
	 */
	@ApiOperation("物料退料")
	@GetMapping("goBack")
	@Transactional(rollbackFor = Exception.class)
	public R<?> materialGoBack(ExecuteMaterialsVO material) {
		ExecuteMaterials executeMaterials = executeMaterialsService.getById(material.getId());
		if (material.getMatNum() > executeMaterials.getMatNum()) {
			return R.fail("退料数量不能大于剩余量!");
		}
		if (material.getMatNum() == 0) {
			return R.fail("退料数量不能等于0!");
		}
		Integer seatId = material.getSeatId(); //传入对应的库位信息
		synchronized (this){
			/*先查询该库位是否有剩余*/
			Integer usableNum = storeInventoryService.getUsableNum(seatId);
			if(usableNum <= 0){
				return R.fail("当前库位已满,请选择其他库位");
			}
			StoreSeat seat = seatService.getById(seatId);
			executeMaterials.setMatNum(executeMaterials.getMatNum() - material.getMatNum());
			executeMaterialsService.updateById(executeMaterials);
			ExecuteOffmater executeOffmater = new ExecuteOffmater();
			executeOffmater.setMatId(material.getMatId());
			executeOffmater.setMatName(material.getMatName());
			executeOffmater.setMatNum(material.getMatNum());
			executeOffmater.setTotalNum(material.getTotalNum());
			executeOffmater.setUnit("米");//设定单位
			executeOffmater.setMaId(material.getMaId());
			executeOffmater.setWsId(material.getWsId());
			executeOffmater.setWfId(material.getWfId());
			executeOffmater.setUsId(material.getUsId());
			executeOffmater.setUpId(material.getId());
			executeOffmater.setSeatId(material.getSeatId());
			executeOffmater.setStatus(1);
			String tdNo2 = executeTraycardService.getTdNo2();
//			if (material.getBarCode().contains("NXHR-")) {
//				executeOffmater.setBarCode(executeMaterials.getBarCode());
//			} else {
//				executeOffmater.setBarCode(tdNo2);
//			}
			Integer newEtId = null;
			ExecuteTraycard executeTraycard = executeTraycardService.getById(material.getEtId());
			if (material.getBarCode().contains("NXHR-")) {
				if (null == executeTraycard) {
					return R.fail("托盘有误");
				}
				executeTraycard.setId(null);
				executeTraycard.setTrayNum(material.getMatNum());
				executeTraycard.setMpId(material.getSeatId());
				executeTraycard.setStorePlace(seat.getStNo());
				executeTraycard.setTdNo(tdNo2);
				executeTraycard.setPrintNum(null);
				// 更新托盘数量和库位
				executeTraycardService.save(executeTraycard);
				newEtId = executeTraycard.getId();
			} else {
				ExecuteTraycard erpExecuteTraycard = new ExecuteTraycard();
//				erpExecuteTraycard.setSdId();
//				erpExecuteTraycard.setWfId();
//				erpExecuteTraycard.setTrayNo();
//				erpExecuteTraycard.setTrayNum(material.getMatNum());
//				erpExecuteTraycard.setPlanNum(material.getMatNum());
//				erpExecuteTraycard.setTotalNum(material.getMatNum());
//				erpExecuteTraycard.setMaId(material.getMaId());
//				erpExecuteTraycard.setPrintNum(0);
//				erpExecuteTraycard.setLayNum(material.getMatNum());
//				erpExecuteTraycard.setMpId(material.getSeatId());
//				erpExecuteTraycard.setTdNo(tdNo2);
//				erpExecuteTraycard.setStorePlace(seat.getStNo());
//				erpExecuteTraycard.setUsId(material.getUsId());
//				erpExecuteTraycard.setPrName(material.getMatName());
//				executeTraycardService.save(erpExecuteTraycard);
				newEtId = erpExecuteTraycard.getId();
			}
			// 添加台账
			StoreInventory inventory = new StoreInventory();
			if(executeTraycard != null){
				Integer sdId = executeTraycard.getSdId();
				WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getBaseMapper().selectById(sdId);
				if(workbatchOrdlink != null){
					inventory.setStSize(workbatchOrdlink.getOperateSize());
				}
				inventory.setStType(1);
				inventory.setStId(executeTraycard.getMaId());
				inventory.setStNo(seat.getStNo());
				inventory.setEtId(material.getEtId());
				inventory.setLayNum(executeTraycard.getLayNum());
				inventory.setStatus(2);
				inventory.setEtPdnum(material.getMatNum());
				inventory.setAreaId(seat.getSrId());
				inventory.setStId(seat.getId());
				inventory.setStNo(seat.getStNo());
				inventory.setEtId(newEtId);
				storeInventoryService.save(inventory);
			} else {
				// 物料添加库存
				StoreInventory inventory1 = new StoreInventory();
				inventory1.setStType(3);
				inventory1.setStId(seat.getId());
				inventory1.setStNo(seat.getStNo());
				inventory1.setStSize(seat.getSize());
				inventory1.setMlId(material.getMatId());
				inventory1.setLayNum(material.getMatNum());
				inventory1.setStatus(2);
				inventory1.setEtPdnum(material.getMatNum());
				inventory1.setAreaId(seat.getSrId());
				storeInventoryService.save(inventory1);
			}
			if (!material.getBarCode().contains("NXHR-")) {
				executeOffmater.setBarCode(executeOffmaterService.generateNxwlBarCode());
			} else {
				executeOffmater.setBarCode(tdNo2);
			}
			executeOffmater.setEtId(inventory.getEtId());
			// 保存退料信息
			executeOffmaterService.save(executeOffmater);
		}
		return R.success("退料成功");
	}

	@GetMapping("/getExecuteMaterialsPrint")
	@ApiOperation(value = "获取退料打印数据", notes = "传入退料的记录ids,多条记录用逗号隔开")
	public R<List<TraycardTextVO>> getExecuteMaterialsPrint(@ApiParam("退料记录的id") @RequestParam("ids") String ids) {
		if(StringUtil.isEmpty(ids)){
			return R.fail("传入的ids不能为空");
		}
		return R.data(executeOffmaterService.getExecuteMaterialsPrint(Func.toIntList(ids)));
	}

	@GetMapping("/updateMaterialsPrintNum")
	@ApiOperation(value = "修改退料打印次数", notes = "传入退料的记录ids,多条记录用逗号隔开")
	@Transactional(rollbackFor = Exception.class)
	public R updateMaterialsPrintNum(@ApiParam("退料记录的id") @RequestParam("ids") String ids,
									 @ApiParam("设备id") @RequestParam("maId") Integer maId) {
		List<Integer> idList = Func.toIntList(ids);
		if(idList.isEmpty()){
			return R.fail("传入的ids不能为空");
		}
		executeOffmaterService.updatePrintNum(idList);
		/*根据退料查询标识卡id*/
		List<Integer> etIdList = executeOffmaterService.getEtIdListByIdList(idList);
		try {
			/*台账写入日志*/
			List<StoreInlog> storeInlogList = new ArrayList<>();
			if(!etIdList.isEmpty()){
				/*修改库位占用状态*/
				storeInventoryService.updateStatus(etIdList, 1);
				/*修改标识卡打印次数*/
				executeTraycardService.updatePrintNumList(etIdList, null, null);
//				/*工单实时执行表*/
//				SuperviseExecute superviseExecute =
//						superviseExecuteService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
//				Integer operator = superviseExecute.getOperator();//机台操作人员
				StoreInlog storeInlog;
				for (Integer etId : etIdList) {
					if(etId == null || etId == 0){
						continue;
					}
					storeInlog = new StoreInlog();
					ExecuteOffmater offmater =
							executeOffmaterService.getOne(new QueryWrapper<ExecuteOffmater>().eq("et_id", etId));
					Integer printNum = offmater.getPrintNum();
					Integer usId = offmater.getUsId();
					ExecuteTraycard executeTraycard = executeTraycardService.getById(etId);
					if(printNum == 1){
						StoreInventory storeInventory =
								storeInventoryService.getOne(new QueryWrapper<StoreInventory>().eq("et_id", etId));
						String stSize = storeInventory.getStSize();
						String mlId = storeInventory.getMlId();
						storeInlog.setEtPdnum(executeTraycard.getTrayNum());
						storeInlog.setEtId(etId);
						storeInlog.setLayNum(executeTraycard.getLayNum());
						storeInlog.setStId(executeTraycard.getMpId());
						storeInlog.setStNo(executeTraycard.getStorePlace());
						storeInlog.setStSize(stSize);
						storeInlog.setUsId(usId);
						storeInlog.setStType(storeInventory.getStType());
						storeInlog.setMlId(mlId);
						storeInlog.setOperateType(1);
						storeInlog.setCreateAt(LocalDateTime.now());
						storeInlogList.add(storeInlog);
					}
				}
			}else {
				List<String> mlIdList = new ArrayList<>();
				/*查询物料id*/
				List<ExecuteOffmater> executeOffmaters = executeOffmaterService.listByIds(idList);
				executeOffmaters.stream().filter(e -> !StringUtil.isEmpty(e.getMatId())).forEach(e ->{
					String matId = e.getMatId();//物料id
					mlIdList.add(matId);
					Integer printNum = e.getPrintNum();
					if (printNum == 1) {
						StoreInventory storeInventory =
								storeInventoryService.getOne(new QueryWrapper<StoreInventory>().eq("ml_id", matId));
						String stSize = storeInventory.getStSize();
						StoreInlog storeInlog = new StoreInlog();
						storeInlog.setEtPdnum(storeInventory.getEtPdnum());
						storeInlog.setEtId(storeInventory.getEtId());
						storeInlog.setLayNum(storeInventory.getLayNum());
						storeInlog.setStId(e.getSeatId());
						storeInlog.setStNo(e.getStoreSeatNo());
						storeInlog.setStSize(stSize);
						storeInlog.setUsId(e.getUsId());
						storeInlog.setStType(storeInventory.getStType());
						storeInlog.setMlId(matId);
						storeInlog.setOperateType(1);
						storeInlog.setCreateAt(LocalDateTime.now());
						storeInlogList.add(storeInlog);
					}
				});
				if(!mlIdList.isEmpty()){
					/*修改库位占用状态*/
					storeInventoryService.updateStatusBymlIdList(mlIdList, 1);
				}
			}
			storeInlogService.saveBatch(storeInlogList);
		}catch (Exception e){
			e.printStackTrace();
			return R.fail("修改失败");
		}
		return R.success("修改成功");
	}
}
