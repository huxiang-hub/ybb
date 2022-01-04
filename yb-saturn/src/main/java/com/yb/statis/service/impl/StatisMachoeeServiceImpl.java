/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.statis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.statis.excelUtils.Page;
import com.yb.statis.utils.WeekOfDate;
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.mapper.StatisMachoeeMapper;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.vo.*;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  服务实现类
 *
 * @author Blade
 * @since 2020-04-17
 */
@Service
public class StatisMachoeeServiceImpl extends ServiceImpl<StatisMachoeeMapper, StatisMachoee> implements IStatisMachoeeService {

	@Autowired
	private HttpServletResponse response;
	@Autowired
	private StatisMachoeeMapper statisMachoeeMapper;
	@Autowired
	private WorkbatchShiftMapper workbatchShiftMapper;
	@Autowired
	private MachineMainfoMapper machineMainfoMapper;

	@Override
	public IPage<StatisMachoeeVO> selectStatisMachoeePage(IPage<StatisMachoeeVO> page, StatisMachoeeVO statisMachoee) {
		return page.setRecords(baseMapper.selectStatisMachoeePage(page, statisMachoee));
	}

	@Override
	public List<StatisMachoeeVO> getOeeRateByDay(String maId, String prId, String dpId, String checkDate, ArrayList<String> conditionArrHour, ArrayList<String> conditionArrMin) {
		return statisMachoeeMapper.getOeeRateByDay(maId,prId,dpId,checkDate,conditionArrHour,conditionArrMin);
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getCkMachoee(String nowDate, Integer maId) {
		List<StatisMachoeeVO> statisMachoeeVOList = statisMachoeeMapper.getCkMachoee(nowDate, maId);
		Map<String, BigDecimal> map;
		Map<String, Map<String, BigDecimal>> map1 = new TreeMap<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(StatisMachoeeVO statisMachoeeVO : statisMachoeeVOList){
			map = new TreeMap();
			String oeDate = statisMachoeeVO.getOeDate();//日期
			String sfName = statisMachoeeVO.getSfName();//班次名称
			BigDecimal gatherRate = statisMachoeeVO.getGatherRate();
			Date date = null;
			try {
				date = simpleDateFormat.parse(oeDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String weekOfDate = WeekOfDate.getWeekOfDate(date);
			if(map1.containsKey(weekOfDate)){
				map.put(sfName, gatherRate);
				map1.get(weekOfDate).putAll(map);
			}else {
				map.put(sfName, gatherRate);
				map1.put(weekOfDate, map);
			}
		}
		BigDecimal gatherRate = new BigDecimal(0);//班次补0
		List<WorkbatchMachShiftVO> workbatchShiftList = workbatchShiftMapper.findByMaId(maId);
		for(WorkbatchMachShiftVO workbatchMachShiftVO : workbatchShiftList){
			String ckName = workbatchMachShiftVO.getCkName();
			for(String key : map1.keySet()){
				Map<String, BigDecimal> map2 = map1.get(key);
				if(!map2.containsKey(ckName)){
					map2.put(ckName, gatherRate);
				}
				map1.put(key, map2);
			}
		}
		String[] weekDays = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
		for(String s : weekDays){//星期补0
			if(!map1.containsKey(s)){
				Map<String, BigDecimal> map4 = new TreeMap<>();
				for(WorkbatchMachShiftVO workbatchMachShiftVO : workbatchShiftList){
					map4.put(workbatchMachShiftVO.getCkName(), gatherRate);
				}
				map1.put(s, map4);
			}
		}
		return map1;
	}

	@Override
	public Double getMachoeeByWsIdANDMaId(String oeDate, Integer maId, Integer wsId) {
		return statisMachoeeMapper.getMachoeeByWsIdANDMaId(oeDate, maId, wsId);
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getDayMachoee(String dateTime, Integer maType) {
		List<Integer> maIds = new ArrayList<>();
		QueryWrapper<MachineMainfo> machineMainfoQueryWrapper = new QueryWrapper<>();
		if(maType != null && maType != 0){
			machineMainfoQueryWrapper.eq("ma_type", maType);
		}
		List<MachineMainfo> machineMainfoList =
				machineMainfoMapper.selectList(machineMainfoQueryWrapper);
		for(MachineMainfo machineMainfo : machineMainfoList){
			maIds.add(machineMainfo.getId());
		}
		List<StatisMachoeeVO> statisMachoeeVOList = statisMachoeeMapper.getDayMachoee(dateTime, maIds);
		Map<String, BigDecimal> map;
		Map<String, Map<String, BigDecimal>> map1 = new LinkedHashMap<>();
		for(StatisMachoeeVO statisMachoeeVO : statisMachoeeVOList){
			map = new TreeMap();
			String oeDate = statisMachoeeVO.getOeDate();//日期
			String maName = statisMachoeeVO.getMaName();//设备名称
			BigDecimal gatherRate = statisMachoeeVO.getGatherRate();
			if(map1.containsKey(oeDate)){
				map.put(maName, gatherRate);
				map1.get(oeDate).putAll(map);
			}else {
				map.put(maName, gatherRate);
				map1.put(oeDate, map);
			}
		}

		BigDecimal gatherRate = new BigDecimal(0);//设备补0
		for(MachineMainfo machineMainfo : machineMainfoList){
			String maName = machineMainfo.getName();
			for(String key : map1.keySet()){
				Map<String, BigDecimal> map2 = map1.get(key);
				if(!map2.containsKey(maName)){
					map2.put(maName, gatherRate);
				}
				map1.put(key, map2);
			}
		}
		//本周所有日期
		List<String> weekDateList = WeekOfDate.getWeekDate(dateTime);//传入日期所在周的所有日期
		for(String s : weekDateList){//星期补0
			if(!map1.containsKey(s)){
				Map<String, BigDecimal> map4 = new TreeMap<>();
				for(MachineMainfo machineMainfo : machineMainfoList){
					map4.put(machineMainfo.getName(), gatherRate);
				}
				map1.put(s, map4);
			}
		}
		return map1;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getCkDateMachoee(String dateTime, Integer maId) {

		List<StatisMachoeeVO> statisMachoeeVOList = statisMachoeeMapper.getCkMachoee(dateTime, maId);
		Map<String, BigDecimal> map;
		Map<String, Map<String, BigDecimal>> map1 = new TreeMap<>();
		for(StatisMachoeeVO statisMachoeeVO : statisMachoeeVOList){
			map = new TreeMap();
			String oeDate = statisMachoeeVO.getOeDate();//日期
			String sfName = statisMachoeeVO.getSfName();//班次名称
			BigDecimal gatherRate = statisMachoeeVO.getGatherRate();
			if(map1.containsKey(oeDate)){
				map.put(sfName, gatherRate);
				map1.get(oeDate).putAll(map);
			}else {
				map.put(sfName, gatherRate);
				map1.put(oeDate, map);
			}
		}
		BigDecimal gatherRate = new BigDecimal(0);//班次补0
		List<WorkbatchMachShiftVO> workbatchShiftList = workbatchShiftMapper.findByMaId(maId);
		for(WorkbatchMachShiftVO workbatchMachShiftVO : workbatchShiftList){
			String ckName = workbatchMachShiftVO.getCkName();
			for(String key : map1.keySet()){
				Map<String, BigDecimal> map2 = map1.get(key);
				if(!map2.containsKey(ckName)){
					map2.put(ckName, gatherRate);
				}
				map1.put(key, map2);
			}
		}
		List<String> weekDateList = WeekOfDate.getWeekDate(dateTime);
//		String[] weekDays = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
		for(String s : weekDateList){//星期补0
			if(!map1.containsKey(s)){
				Map<String, BigDecimal> map4 = new TreeMap<>();
				for(WorkbatchMachShiftVO workbatchMachShiftVO : workbatchShiftList){
					map4.put(workbatchMachShiftVO.getCkName(), gatherRate);
				}
				map1.put(s, map4);
			}
		}
		return map1;
	}

	@Override
	public List<StatisMachoeeVO> getCkMachTypeOee(String dateTime, Integer maType, Integer wsId) {
		return statisMachoeeMapper.getCkMachTypeOee(dateTime, maType, wsId);
	}

	@Override
	public Boolean machoeeExcelExport(String targetDay) {
		/*查询导出所需数据*/
		if(StringUtil.isEmpty(targetDay)){
			targetDay = DateUtil.refNowDay();
		}
		List<MachoeeExcelExportVO> machoeeExcelExportVOList = statisMachoeeMapper.machoeeExcelExport(targetDay);
		if(machoeeExcelExportVOList.isEmpty()){
			return false;
		}
		/*处理数据*/
		List<MachoeeExportVO> machoeeExportVOList = machoeeMassaging(machoeeExcelExportVOList);

		String templatePath = "model/machineOeeModel.xls";
		BufferedOutputStream bos = null;//导出到网页
		List<Page> page = individual(machoeeExportVOList); // 一张表一个对象数据
		Map<String, Object> model = new HashMap<>();
		model.put("pages", page);
		model.put("sheetNames", getSheetName(page));
		model.put("slName", getSheetName(page));
		try {
			bos = ExportlUtil.getBufferedOutputStream("设备oee统计.xls", response);//返回前端处理
			JxlsUtils jxlsUtils = new JxlsUtils();
			jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端处理
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将数据获取的数据封装成一页一个人的List
	 */
	public static List<Page> individual(List<MachoeeExportVO> list) {
		List<Page> pages = new ArrayList<Page>();
		for (int i = 0; i < list.size(); i++) {
			Page p = new Page();
			p.setOnlyOne(list.get(i));
			String maName = list.get(i).getMaName().replace("*", "");
			p.setSheetName(maName);
			//设置sheet名称
			pages.add(p);
		}
		return pages;
	}

	/**
	 * Excel 的分页名（页码）的封装
	 * 此方法用来获取分好页的页名信息，将信息放入一个链表中返回
	 */
	public static ArrayList<String> getSheetName(List<Page> page) {
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < page.size(); i++) {
			al.add(page.get(i).getSheetName());
		}
		return al;
	}

	/**
	 * 处理导出数据格式
	 * @param machoeeExcelExportVOList
	 * @return
	 */
	private List<MachoeeExportVO> machoeeMassaging(List<MachoeeExcelExportVO> machoeeExcelExportVOList) {
		Map<String, List<MachoeeExcelExportVO>> MachoeeExcelExportVOMap = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		for (MachoeeExcelExportVO machoeeExcelExportVO : machoeeExcelExportVOList) {//处理数据格式,设备名称相同的放在同一个map
			machoeeExcelExportVO.setOeDate(sdf.format(DateUtil.changeDay(machoeeExcelExportVO.getOeDate())));
			String maName = machoeeExcelExportVO.getMaName();
			if (MachoeeExcelExportVOMap.containsKey(maName)) {//如果key已存在,则查出来追加
				MachoeeExcelExportVOMap.get(maName).add(machoeeExcelExportVO);
			} else {//否则新建list
				List<MachoeeExcelExportVO> machoeeExcelExportVOS = new ArrayList<>();
				machoeeExcelExportVOS.add(machoeeExcelExportVO);
				MachoeeExcelExportVOMap.put(maName, machoeeExcelExportVOS);
			}
		}

		/*生产准备和*/
		Integer prepareStaySum = 0;
		Integer faultStaySum = 0;
		Integer qualityStaySum = 0;
		Integer planStaySum = 0;
		Integer manageStaySum = 0;
		Integer abrasionStaySum = 0;
		Integer mouldStaySum = 0;
		Integer restStaySum = 0;
		Integer workStaySum = 0;
		Integer standardRuntimeSum = 0;
		Integer planutilizeStaySum = 0;
		Integer factutilizeStaySum = 0;
		Integer taskCountSum = 0;
		Integer nodefectCountSum = 0;
		Integer workCountSum = 0;
		Integer factSpeedSum = 0;
		Integer normalSpeedStaySum = 0;
		/*时间稼动率E*/
		Double utilizeRateAvg = 0.0;
		/*良品率G*/
		Double yieldRateAvg = 0.0;
		/*性能稼动率J*/
		Double performRateAvg = 0.0;
		List<MachoeeExportVO> machoeeExportVOList = new ArrayList<>();
		/*遍历map,一个map就是一个对象*/
		for(String key : MachoeeExcelExportVOMap.keySet()){
			MachoeeExportVO machoeeExportVO = new MachoeeExportVO();
			machoeeExportVO.setMaName(key);
			List<MachoeeExcelExportVO> machoeeExcelExportVOS = MachoeeExcelExportVOMap.get(key);
			for(MachoeeExcelExportVO machoeeExcelExportVO : machoeeExcelExportVOS){
				Integer prepareStay = machoeeExcelExportVO.getPrepareStay() == null ? 0 : machoeeExcelExportVO.getPrepareStay();//生产准备
				prepareStaySum += prepareStay;
				Integer factSpeed = machoeeExcelExportVO.getFactSpeed() == null ? 0 : machoeeExcelExportVO.getFactSpeed();
				factSpeedSum += factSpeed;
				Integer workCount = machoeeExcelExportVO.getWorkCount() == null ? 0 : machoeeExcelExportVO.getWorkCount();
				workCountSum += workCount;
				Integer factutilizeStay = machoeeExcelExportVO.getFactutilizeStay() == null ? 0 : machoeeExcelExportVO.getFactutilizeStay();
				factutilizeStaySum += factutilizeStay;
				Integer faultStay = machoeeExcelExportVO.getFaultStay() == null ? 0 : machoeeExcelExportVO.getFaultStay();
				faultStaySum += faultStay;
				Integer mouldStay = machoeeExcelExportVO.getMouldStay();
				mouldStay = mouldStay == null ? 0 : mouldStay;
				mouldStaySum += mouldStay;
				Integer nodefectCount = machoeeExcelExportVO.getNodefectCount();
				nodefectCount = nodefectCount == null ? 0 : nodefectCount;
				nodefectCountSum += nodefectCount;
				Integer normalSpeed = machoeeExcelExportVO.getNormalSpeed();
				normalSpeed = normalSpeed == null ? 0 : normalSpeed;
				normalSpeedStaySum += normalSpeed;
				Integer planStay = machoeeExcelExportVO.getPlanStay();
				planStay = planStay == null ? 0 : planStay;
				planStaySum += planStay;
				Integer planutilizeStay = machoeeExcelExportVO.getPlanutilizeStay();
				planutilizeStay = planutilizeStay == null ? 0 : planutilizeStay;
				planutilizeStaySum += planutilizeStay;
				Integer qualityStay = machoeeExcelExportVO.getQualityStay();
				qualityStay = qualityStay == null ? 0 : qualityStay;
				qualityStaySum += qualityStay;
				Integer restStay = machoeeExcelExportVO.getRestStay();
				restStay = restStay == null ? 0 : restStay;
				restStaySum += restStay;
				Integer standardRuntime = machoeeExcelExportVO.getStandardRuntime();
				standardRuntime = standardRuntime == null ? 0 : standardRuntime;
				standardRuntimeSum += standardRuntime;
				Integer taskCount = machoeeExcelExportVO.getTaskCount();
				taskCount = taskCount == null ? 0 : taskCount;
				taskCountSum += taskCount;
				Integer workStay = machoeeExcelExportVO.getWorkStay();
				workStay = workStay == null ? 0 : workStay;
				workStaySum += workStay;
				Integer abrasionStay = machoeeExcelExportVO.getAbrasionStay();
				abrasionStay = abrasionStay == null ? 0 : abrasionStay;
				abrasionStaySum += abrasionStay;
				Integer manageStay = machoeeExcelExportVO.getManageStay();
				manageStay = manageStay == null ? 0 : manageStay;
				manageStaySum += manageStay;
			}
			/*生产准备和*/
			machoeeExportVO.setPrepareStaySum(prepareStaySum);
			/*磨损更换和*/
			machoeeExportVO.setAbrasionStaySum(abrasionStaySum);
			/*实际能力生产性<张/h>和*/
			machoeeExportVO.setFactSpeedSum(factSpeedSum);
			/*实际稼动时间和*/
			machoeeExportVO.setFactutilizeStaySum(factutilizeStaySum);
			/*设备故障和*/
			machoeeExportVO.setFaultStaySum(faultStaySum);
			/*管理停止和*/
			machoeeExportVO.setManageStaySum(manageStaySum);
			/*产品切换和*/
			machoeeExportVO.setMouldStaySum(mouldStaySum);
			/*良品数和*/
			machoeeExportVO.setNodefectCountSum(nodefectCountSum);
			/*理论能力生产性和（张/h)*/
			machoeeExportVO.setNormalSpeedStaySum(normalSpeedStaySum);
			/*计划安排停机和*/
			machoeeExportVO.setPlanStaySum(planStaySum);
			/*计划稼动时间和*/
			machoeeExportVO.setPlanutilizeStaySum(planutilizeStaySum);
			/*品质故障和*/
			machoeeExportVO.setQualityStaySum(qualityStaySum);
			/*休息吃饭和*/
			machoeeExportVO.setRestStaySum(restStaySum);
			/*可用稼动时间和*/
			machoeeExportVO.setStandardRuntimeSum(standardRuntimeSum);
			/*应产数和*/
			machoeeExportVO.setTaskCountSum(taskCountSum);
			/*作业数<张和*/
			machoeeExportVO.setWorkCountSum(workCountSum);
			/*A正常出勤时间和*/
			machoeeExportVO.setWorkStaySum(workStaySum);
			if(planutilizeStaySum != null && planutilizeStaySum != 0){//时间稼动率
				utilizeRateAvg = factutilizeStaySum / (double)planutilizeStaySum;
			}
			machoeeExportVO.setUtilizeRateAvg(utilizeRateAvg);
			/*良品率*/
			if(workCountSum != null && workCountSum != 0){
				yieldRateAvg = nodefectCountSum / (double)workCountSum;
			}
			machoeeExportVO.setYieldRateAvg(yieldRateAvg);
			/*性能稼动率J*/
			if(normalSpeedStaySum != null && normalSpeedStaySum != 0){
				performRateAvg = factSpeedSum / (double)normalSpeedStaySum;
			}
			machoeeExportVO.setPerformRateAvg(performRateAvg);
			machoeeExportVO.setGatherRateAvg(utilizeRateAvg * yieldRateAvg * performRateAvg);
			machoeeExportVO.setMachoeeExcelExportVOList(machoeeExcelExportVOS);
			machoeeExportVOList.add(machoeeExportVO);
		}
		return machoeeExportVOList;
	}
}
