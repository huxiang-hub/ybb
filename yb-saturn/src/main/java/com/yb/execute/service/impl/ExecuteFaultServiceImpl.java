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
package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseDict;
import com.yb.base.mapper.BaseDictMapper;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.mapper.ExecuteFaultMapper;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.execute.service.IExecuteFaultService;
import com.yb.execute.vo.*;
import com.yb.panelapi.order.entity.DownFaultReportDto;
import com.yb.panelapi.user.utils.R;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备停机故障记录表_yb_execute_fault 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteFaultServiceImpl extends ServiceImpl<ExecuteFaultMapper, ExecuteFault> implements IExecuteFaultService {

    @Autowired
    private ExecuteFaultMapper faultMapper;

    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private BaseDictMapper baseDictMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;

    @Override
    public IPage<ExecuteFaultVO> selectExecuteFaultPage(IPage<ExecuteFaultVO> page, ExecuteFaultVO executeFault) {
        return page.setRecords(faultMapper.selectExecuteFaultPage(page, executeFault));
    }

    @Override
    public int getCount(Integer mId) {
        return faultMapper.getCount(mId);
    }

    @Override
    public R faultReport(DownFaultReportDto faultReportDto) {
        if(faultReportDto==null){
            return R.error("没有上报数据");
        }
        //停机上报信息
        ExecuteFault executeFault = faultReportDto.getExecuteFault();
        if (executeFault==null){
            return R.error("上报的停机数据为空");
        }
        Date currTime = new Date();
        ExecuteFault executeFault1 = faultMapper.selectById(executeFault.getId());
        executeFault1.setStatus(executeFault.getStatus());
        executeFault1.setClassify(executeFault.getClassify());
        executeFault1.setHandleTime(currTime);
        faultMapper.updateById(executeFault);

        return R.ok();
    }

    @Override
    public List<ExecuteFaultVO> getDownFaultList(Integer id) {
        return faultMapper.selectByOrderId(id);
    }

    @Override
    public List<ExecuteFault> getClassfyList() {
        return faultMapper.getClassfyList();
    }

    @Override
    public IPage<ExecuteFaultVO> selectExecuteFaultPages(IPage<ExecuteFaultVO> page, ExecuteFaultVO executeFault) {
        return page.setRecords(faultMapper.selectExecuteFaultPages(page, executeFault));
    }

    @Override
    public List<ExecuteFaultWfIdVO> getExecuteFaultList(ExecuteFaultParamVO executeFaultParamVO) {
        Integer maId = executeFaultParamVO.getMaId();//设备id
        List<ExecuteFaultWfIdVO> executeFaultWfIdVOList = new ArrayList<>();
        ExecuteFault faultVO = null;
        try {
            /*查询需要主动上报的停机*/
            faultVO = faultMapper.getStopInitiative(maId);
        }catch (Exception e){
            e.printStackTrace();
            log.error("同一设备的停机结束时间大于1条记录为null");
        }
        /*先查询书记字典,根据时间段分类*/
        List<BaseDict> baseDictList =
                baseDictMapper.selectList(new QueryWrapper<BaseDict>().eq("dt_type", "stopTime").ne("p_id", 0));
        if(baseDictList.isEmpty()){
            log.error("数据字典的停机持续时长区间不存在");
            return null;
        }
        /*根据班次id查询所需的班次时间*/
        ExecuteFaultShiftTimeVO shiftSetTime = getShiftSetTime(executeFaultParamVO);
        if(shiftSetTime == null){
            return null;
        }
        String startDate = shiftSetTime.getStartDate();//开始时间
        String endDate = shiftSetTime.getEndDate();//结束时间
        /*查询所有需要的停机信息*/
        List<FaultMachineVO > faultList = faultMapper.getExecuteFaultList(maId, startDate, endDate);
        /*处理查询出的停机数据,返回所需的map*/
        Map<Integer, Map<String, List<FaultMachineVO>>> faultMap = getExecuteFaultMapGroupByTime(baseDictList, faultList);
        /*获取停机列表中所有的wfId*/
        Set<Integer> wfIdSet = getWfIdSet(faultMap);
        if(faultVO != null){
            wfIdSet.add(faultVO.getWfId());
        }
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = null;
        if(!wfIdSet.isEmpty()){
            /*根据wfId查询所有的排产信息*/
            workbatchOrdlinkVOList =
                    workbatchOrdlinkMapper.getWorkbatchOrdlinkVOListByWfIdList(wfIdSet);
        }
        /*返回的list*/
        List<ExecuteFaultMachineVO> executeFaultMachineVOList;
        boolean status = true;//状态标识
        for(Integer wfId : faultMap.keySet()){//处理成返回所需的对象
            WorkbatchOrdlinkVO ordlinkVO = null;
            ExecuteFaultWfIdVO executeFaultWfIdVO = new ExecuteFaultWfIdVO();
            if(wfId != null){
                if(faultVO != null){
                    if(wfId.equals(faultVO.getWfId())){
                        executeFaultWfIdVO.setStartTime(faultVO.getStartAt());
                        executeFaultWfIdVO.setId(faultVO.getId());
                        status = false;
                    }
                }
                /*查询排产单相关信息*/
                ordlinkVO = getWorkbatchOrdlinkVO(wfId, workbatchOrdlinkVOList);
            }else {
                if(faultVO != null){
                    if(faultVO.getWfId() == null){
                        executeFaultWfIdVO.setStartTime(faultVO.getStartAt());
                        executeFaultWfIdVO.setId(faultVO.getId());
                        status = false;
                    }
                }
            }
            Integer totalDowntime = 0;
            Integer totalDownNum = 0;
            executeFaultMachineVOList = new ArrayList<>();
            Map<String, List<FaultMachineVO>> stringListMap = faultMap.get(wfId);

            for(BaseDict baseDict : baseDictList){
                String dtValue = baseDict.getDtValue();
                ExecuteFaultMachineVO executeFaultMachineVO = new ExecuteFaultMachineVO();
                List<FaultMachineVO> executeFaultList = stringListMap.get(dtValue);
                Integer AllStopNum = 0;//分段总停机次数
                Integer durations = 0;
                Integer unconfirmedNum = 0;
                if(executeFaultList != null && !executeFaultList.isEmpty()){
                    AllStopNum = executeFaultList.size();
                    for(FaultMachineVO executeFault : executeFaultList){
                        Integer duration = (int) Math.round(executeFault.getDuration() / (double) 60);//持续时长
                        durations += duration;//分段时间总停机时长
                        if(executeFault.getHandle() == 0){
                            unconfirmedNum++;//分段未确认数
                        }
                    }
                }
                totalDowntime += durations;//排产单总停机时长
                totalDownNum += AllStopNum;//排产单总停机次数
                executeFaultMachineVO.setDtValue(dtValue);
                executeFaultMachineVO.setAllStopNum(AllStopNum);//分段总停机次数
                executeFaultMachineVO.setAllStopTime(durations);//分段时间总停机时长
                executeFaultMachineVO.setUnconfirmedNum(unconfirmedNum);
                executeFaultMachineVO.setExecuteFaultList(executeFaultList);
                executeFaultMachineVOList.add(executeFaultMachineVO);

            }
            executeFaultWfIdVO.setWfId(wfId);
            /*处理停机数据中产品名称等数据*/
            executeFaultWfIdVO = processingExecuteFaultWorkbatchOrdlinkVO(executeFaultWfIdVO, ordlinkVO);
            executeFaultWfIdVO.setTotalDownNum(totalDownNum);
            executeFaultWfIdVO.setTotalDowntime(totalDowntime);
//            executeFaultWfIdVO.setUserName(usName);
            executeFaultWfIdVO.setExecuteFaultMachineVOList(executeFaultMachineVOList);
            executeFaultWfIdVOList.add(executeFaultWfIdVO);
        }
        if(status && faultVO != null){//标识主动上报未用到,如果未使用则加入新的排产单停机中
            WorkbatchOrdlinkVO workbatchOrdlinkVO = null;
            Integer voWfId = faultVO.getWfId();
            if(voWfId != null){
                /*查询排产单相关信息*/
                workbatchOrdlinkVO = getWorkbatchOrdlinkVO(voWfId, workbatchOrdlinkVOList);
            }
            ExecuteFaultWfIdVO executeFaultWfIdVO = new ExecuteFaultWfIdVO();
            executeFaultWfIdVO.setStartTime(faultVO.getStartAt());
            executeFaultWfIdVO.setId(faultVO.getId());
            executeFaultWfIdVO.setTotalDowntime(0);
            executeFaultWfIdVO.setTotalDownNum(0);
            executeFaultWfIdVO.setWfId(voWfId);
            /*处理停机数据中产品名称等数据*/
            executeFaultWfIdVO = processingExecuteFaultWorkbatchOrdlinkVO(executeFaultWfIdVO, workbatchOrdlinkVO);
            if(voWfId == null){//如果排产班次id为空,则放在第一条
                executeFaultWfIdVOList.add(0, executeFaultWfIdVO);
            }else {
                executeFaultWfIdVOList.add(executeFaultWfIdVO);
            }
        }
        return executeFaultWfIdVOList;
    }

    @Override
    public Integer getAllUnconfirmedNum(ExecuteFaultParamVO executeFaultParamVO) {
        Integer maId = executeFaultParamVO.getMaId();
        /*获取当前班次的开始时间和结束时间*/
        ExecuteFaultShiftTimeVO shiftSetTime = getShiftSetTime(executeFaultParamVO);
        if(shiftSetTime == null){//班次信息不存在
            return null;
        }
        Integer AllUnconfirmedNum = 0;
        if(shiftSetTime != null){
            AllUnconfirmedNum =
                    faultMapper.getAllUnconfirmedNum(shiftSetTime.getStartDate(), shiftSetTime.getEndDate(), maId);
        }
        return AllUnconfirmedNum;
    }

    @Override
    public IPage<ExecuteFaultVO> executeFaultList(ExecuteFaultRequest executeFaultRequest, IPage<ExecuteFaultVO> page) {
        String endTime = executeFaultRequest.getEndTime();
        String startTime = executeFaultRequest.getStartTime();
        String nowDay = DateUtil.refNowDay();
        if(StringUtil.isEmpty(startTime)){
            executeFaultRequest.setStartTime(nowDay);
        }
        if(StringUtil.isEmpty(endTime)){
            executeFaultRequest.setEndTime(nowDay);
        }
        List<ExecuteFaultVO> executeFaultList = faultMapper.executeFaultList(executeFaultRequest, page);
        return page.setRecords(executeFaultList);
    }

    /**
     * 根据wfId获取所需的排产信息
     * @param wfId
     * @param workbatchOrdlinkVOList
     * @return
     */
    private WorkbatchOrdlinkVO getWorkbatchOrdlinkVO(Integer wfId, List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList){
        if(workbatchOrdlinkVOList != null){//处理排产相关信息
            for(WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlinkVOList){
                if(wfId.equals(workbatchOrdlinkVO.getWfId())){
                    return workbatchOrdlinkVO;
                }
            }
        }
        return null;
    }

    /**
     * 处理停机数据中产品名称等数据
     * @param executeFaultWfIdVO
     * @param ordlinkVO
     * @return
     */
    private ExecuteFaultWfIdVO processingExecuteFaultWorkbatchOrdlinkVO(ExecuteFaultWfIdVO executeFaultWfIdVO, WorkbatchOrdlinkVO ordlinkVO){
        if(ordlinkVO != null){
            executeFaultWfIdVO.setCmName(ordlinkVO.getCmName());
            executeFaultWfIdVO.setPdName(ordlinkVO.getPdName());
            executeFaultWfIdVO.setWbNo(ordlinkVO.getWbNo());
            executeFaultWfIdVO.setPlanNum(ordlinkVO.getPlanNum());
            executeFaultWfIdVO.setFinishNum(ordlinkVO.getFinishNum());
        }
        return executeFaultWfIdVO;
    }


    /**
     * 获取停机中存在的所有wfId
     * @param faultMap
     * @return
     */
    private Set<Integer> getWfIdSet(Map<Integer, Map<String, List<FaultMachineVO>>>  faultMap){
        Set<Integer> wfIdSet = new HashSet<>();
        if(faultMap != null){
            for(Integer key : faultMap.keySet()){
                wfIdSet.add(key);
            }
        }
        return wfIdSet;
    }

    /**
     * 获取当前班次的开始时间和结束时间
     * @param executeFaultParamVO
     * @return
     */
    private ExecuteFaultShiftTimeVO getShiftSetTime(ExecuteFaultParamVO executeFaultParamVO){
        Integer maId = executeFaultParamVO.getMaId();
        Integer wsId = executeFaultParamVO.getWsId();
        String targetDay = executeFaultParamVO.getTargetDay();
        ExecuteFaultShiftTimeVO executeFaultShiftTimeVO = new ExecuteFaultShiftTimeVO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        if(StringUtil.isEmpty(targetDay)){
            targetDay = DateUtil.refNowDay();
        }
        WorkbatchMachShiftVO shiftByMaIdAndWsId = workbatchShiftMapper.findShiftByMaIdAndWsId(maId, wsId);
        if(shiftByMaIdAndWsId == null){
            log.error("班次不存在,请检查班次id是否正确");
            return null;
        }
        Date date = new Date();
        Date startTime = shiftByMaIdAndWsId.getStartTime();
        Date endTime = shiftByMaIdAndWsId.getEndTime();
        String startDate = targetDay + " " + dateFormat.format(startTime);//开始时间
        String endDate = targetDay + " " + dateFormat.format(endTime);//结束时间
        if(startTime.getTime() > endTime.getTime()){//晚班,判断结束时间加一天或者开始时间减一天
            if(DateUtil.toDate(endDate, "yyyy-MM-dd HH:mm:ss").getTime() < date.getTime()){//判断是否已经过0点,如果没过结束时间加一天,如果过了开始时间减一天
                endDate = DateUtil.format(DateUtil.addDayForDate(date, 1)) + " " + endTime;
            }else {
                startDate = DateUtil.format(DateUtil.addDayForDate(date, -1)) + " " + startTime;
            }
        }
        executeFaultShiftTimeVO.setStartDate(startDate);
        executeFaultShiftTimeVO.setEndDate(endDate);
        return executeFaultShiftTimeVO;
    }



    /**
     * 处理停机数据,按排产id分组
     * @param faultList
     * @return
     */
    private Map<Integer, List<FaultMachineVO>> getExecuteFaultGroupByWfId(List<FaultMachineVO> faultList){
        Map<Integer, List<FaultMachineVO>> executeFaultMap = new LinkedHashMap<>();
        for(FaultMachineVO fault : faultList){//把所有的停机按wfId分类存map
            Integer wfId = fault.getWfId();
            if(executeFaultMap.containsKey(wfId)){
                executeFaultMap.get(wfId).add(fault);
            }else {
                List<FaultMachineVO> executeFaults = new ArrayList<>();
                executeFaults.add(fault);
                executeFaultMap.put(wfId, executeFaults);
            }
        }
        return executeFaultMap;
    }

    /**
     * 把按排产id分组的停机信息再按时间段分组
     * @param baseDictList
     * @param faultList
     * @return
     */
    private Map<Integer, Map<String, List<FaultMachineVO>>> getExecuteFaultMapGroupByTime( List<BaseDict> baseDictList, List<FaultMachineVO> faultList){
        /*处理停机数据,按排产id分组*/
        Map<Integer, List<FaultMachineVO>> executeFaultMap = getExecuteFaultGroupByWfId(faultList);
        Map<Integer, Map<String, List<FaultMachineVO>>> faultMap = new LinkedHashMap<>();//一时间段未key的map
        Map<String, List<FaultMachineVO>> map;
        /*每个时间段存放的停机对象*/
        for(Integer wfId : executeFaultMap.keySet()){//把所有的停机按时间和wfId的层集存放
            List<FaultMachineVO> faults = executeFaultMap.get(wfId);
            map = new LinkedHashMap<>();
            for(BaseDict baseDict : baseDictList){
                for(FaultMachineVO executeFault : faults){
                    Integer duration = (int) Math.round(executeFault.getDuration() / (double) 60);//秒转化为分(采用四舍五入)
                    String dtValue = baseDict.getDtValue();
                    String[] dtValues = dtValue.split("-");
                    String MinTime = dtValues[0];//时间段最小值
                    String MaxTime = dtValues[1];//时间段最大值
                    if("∞".equals(MaxTime)){
                        if(Integer.valueOf(MinTime) <= duration){
                            if(map.containsKey(dtValue)){
                                map.get(dtValue).add(executeFault);
                            }else {
                                List<FaultMachineVO> executeFaultList = new ArrayList<>();
                                executeFaultList.add(executeFault);
                                map.put(dtValue, executeFaultList);
                            }
                        }
                    }else {
                        if(Integer.valueOf(MinTime) <= duration && Integer.valueOf(MaxTime) > duration){
                            if(map.containsKey(dtValue)){
                                map.get(dtValue).add(executeFault);
                            }else {
                                List<FaultMachineVO> executeFaultList = new ArrayList<>();
                                executeFaultList.add(executeFault);
                                map.put(dtValue, executeFaultList);
                            }
                        }
                    }
                }
                faultMap.put(wfId, map);
            }
        }
        return faultMap;
    }

}
