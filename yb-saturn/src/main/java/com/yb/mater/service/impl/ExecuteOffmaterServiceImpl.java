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
package com.yb.mater.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.execute.vo.TraycardDataVO;
import com.yb.execute.vo.TraycardTextVO;
import com.yb.mater.entity.ExecuteOffmater;
import com.yb.mater.mapper.ExecuteOffmaterMapper;
import com.yb.mater.service.IExecuteOffmaterService;
import com.yb.mater.vo.ExecuteOffmaterVO;
import com.yb.panelapi.user.mapper.BaseFactoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物料退料管理_yb_execute_offmater 服务实现类
 *
 * @author BladeX
 * @since 2021-01-18
 */
@Service
public class ExecuteOffmaterServiceImpl extends ServiceImpl<ExecuteOffmaterMapper, ExecuteOffmater> implements IExecuteOffmaterService {

	@Autowired
	private ExecuteOffmaterMapper executeOffmaterMapper;
	@Autowired
	private BaseFactoryMapper baseFactoryMapper;

	@Override
	public IPage<ExecuteOffmaterVO> selectExecuteOffmaterPage(IPage<ExecuteOffmaterVO> page, ExecuteOffmaterVO executeOffmater) {
		return page.setRecords(baseMapper.selectExecuteOffmaterPage(page, executeOffmater));
	}

	@Override
	public List<ExecuteOffmater> getListByWfId(Integer wfId) {
		return executeOffmaterMapper.getListByWfId(wfId);
	}
	@Override
	public List<ExecuteOffmaterVO> getListcsByWfId(Integer wfId) {
		return executeOffmaterMapper.getListcsByWfId(wfId);
	}


	@Override
	public List<TraycardTextVO> getExecuteMaterialsPrint(List<Integer> idList) {
		List<TraycardDataVO> traycardDataVOList = null;
		List<ExecuteOffmater> executeOffmaters = executeOffmaterMapper.selectBatchIds(idList);
		if(!executeOffmaters.isEmpty()){
			Integer etId = executeOffmaters.get(0).getEtId();
			if(etId != null){
				/*半成品,托盘里找数据*/
				traycardDataVOList = executeOffmaterMapper.getMaterialsPrint(idList);
			}else {
				/*物料,退料表找数据*/
				traycardDataVOList =
						executeOffmaterMapper.getExecuteMaterialsPrint(idList);
			}
		}
		List<TraycardTextVO> textList = new ArrayList<>();
		TraycardTextVO text;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		for(TraycardDataVO traycardDataVO : traycardDataVOList){
			text = new TraycardTextVO();
			String refNowDay;
			String time = "";
			Date startTime = traycardDataVO.getStartTime();//开始时间
			if(startTime != null){
				refNowDay = DateUtil.refNowDay(startTime);
				time = format.format(startTime);
			}else {
				refNowDay = DateUtil.refNowDay();
			}
			text.setWbNo(traycardDataVO.getWbNo());
			text.setMaName(traycardDataVO.getMaName());
			text.setNum(traycardDataVO.getNum());
			text.setPdName(traycardDataVO.getPdName());
			text.setUsName(traycardDataVO.getUsName());
			text.setWsName(traycardDataVO.getCkName());
			text.setPrName(traycardDataVO.getPrName());
			text.setStNo(traycardDataVO.getStNo());
			text.setTrayNo(traycardDataVO.getTrayNo());
			text.setPrintTime(new Date());
			text.setId(traycardDataVO.getId());
			text.setMpId(traycardDataVO.getMpId());
			text.setPrintNum(traycardDataVO.getPrintNum());
			text.setTdNo(traycardDataVO.getTdNo());
			text.setProductTime(refNowDay + " " + time +
					"~" + format.format(date));//开始时间~打印时间
			textList.add(text);
		}
		return textList;
	}

	@Override
	public List<Integer> getEtIdListByIdList(List<Integer> idList) {
		return executeOffmaterMapper.executeOffmaterMapper(idList);
	}

	@Override
	public void updatePrintNum(List<Integer> idList) {
		for (Integer id : idList) {
			ExecuteOffmater executeOffmater = executeOffmaterMapper.selectById(id);
			Integer printNum = executeOffmater.getPrintNum();
			if (printNum == null) {
				executeOffmater.setPrintNum(1);
			} else {
				executeOffmater.setPrintNum(printNum + 1);
			}
			executeOffmater.setPrintTime(LocalDateTime.now());
			executeOffmaterMapper.updateById(executeOffmater);
		}
	}

	@Override
	public String generateNxwlBarCode() {
		String barCode = executeOffmaterMapper.getNxwlBarCode();
		int i = 0;
		if (null != barCode) {
			String s = barCode.substring(5);
			i = Integer.parseInt(s);
			if (i == 999999) {
				i = 0;
			}
		}
		String hasRezo = String.format("%06d", (i + 1));
		return "NXWL-" + hasRezo;
	}


}
