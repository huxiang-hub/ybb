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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.vo.*;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.vo.PlanStateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 执行表状态_yb_execute_state 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteStateServiceImpl extends ServiceImpl<ExecuteStateMapper, ExecuteState> implements IExecuteStateService {

    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;

    @Override
    public IPage<ExecuteStateVO> selectExecuteStatePage(IPage<ExecuteStateVO> page, ExecuteStateVO executeState) {
        List<ExecuteStateVO> executeStateList =baseMapper.selectExecuteStatePage(page, executeState);
        return page.setRecords(executeStateList);
    }

    @Override

    public int insertByTime(String startAt, String endAt) {


        return stateMapper.insertByTime(startAt,endAt);
    }

    @Override
    public  List<ExecuteStateVO>  selectDayRecord(Date start_at) {

        return stateMapper.selectDayRecord(start_at);
    }

    @Override
    public int saveState(ExecuteState state) {
        return stateMapper.insert(state);

    }

    @Override
    public int insert(ExecuteState executeState) {
        executeState.setStatus("C");
        executeState.setEvent("C1");
        executeState.setStartAt(new Date());


        return stateMapper.insert(executeState);
    }

    /*
    /*获取当前人的执行状态表信息
   * 获取该机长已有的助理
   * */
    @Override
    public ExecuteState getLeaderAide(int userId) {

        return stateMapper.getLeaderAide(userId);
    }


    /***
     * 给机长增加助理
     * @param teamId
     * @return
     */
    @Override
    public boolean addAide(String teamId,int userId) throws SQLException {
        stateMapper.addAide(teamId,userId);
        return true;
    }

    @Override
    public ExecuteState getExecuteStateByEsId(Integer esId) {
        return stateMapper.getExecuteStateByEsId(esId);
    }

    @Override
    public boolean updataExecuteState(ExecuteState state) {
        return stateMapper.updataExecuteState(state);
    }

    @Override
    public ExecuteState getExecuteState() {
        return null;
        //return stateMapper.getExecuteState();
    }

    @Override
    public ExecuteState getExecuteStateById(Integer id) {
        return stateMapper.getExecuteStateById(id);
    }

    @Override
    public ExecuteStateVO getExecuteVoListBy(Date startTime, Date endTime, String type,Integer mtId) {
        return stateMapper.getExecuteVoListBy(startTime,endTime,type,mtId);
    }

    @Override
    public List<ExecuteStateVO> getAcceptedGoodsByTimeAndMa(Date startTime, Date endTime, Integer maId) {
        return stateMapper.getAcceptedGoodsByTimeAndMa(startTime,endTime,maId);
    }

    @Override
    public List<ExecuteStateVO> getExecuteFailStatus(Date startTime, Date endTime, Integer maId, String equipmentErr) {
        return stateMapper.getExecuteFailStatus(startTime,endTime,maId,equipmentErr);
    }

    @Override
    public List<ExecuteStateVO> getExecuteFailclassify(Date startTime, Date endTime, Integer maId, String rest) {
        return stateMapper.getExecuteFailclassify(startTime,endTime,maId,rest);
    }

    @Override
    public List<ExecuteStateVO> getExecuteExamine(Date startTime, Date endTime, String maId, Integer reprotType) {
        return stateMapper.getExecuteExamine(startTime,endTime,maId,reprotType);
    }

    @Override
    public List<String> getDistinceIdBy(String userId, Date beforeDate, Date endDate) {
        return stateMapper.getDistinceIdBy(userId,beforeDate,endDate);
    }

    @Override
    public ExecuteStateVO getRealTimebyCondition(Date startTime, Date endTime, String userIds) {
        return stateMapper.getRealTimebyCondition(startTime,endTime,userIds);
    }

    @Override
    public List<String> getSdIdbyConditon(Date startTime, Date endTime, Integer maId, String userId, String eventId) {
        return stateMapper.getSdIdbyConditon(startTime,endTime,maId,userId,eventId);
    }

    @Override
    public List<ExecuteState> getSdInfoByCondition(Integer sdId, String eventAccpt, String enventStop,Integer userId) {
        return stateMapper.getSdInfoByCondition(sdId,eventAccpt,enventStop,userId);
    }

    @Override
    public List<ExecuteStateVO> getAcceptedGoodsByTimeAndMaId(Date classStartTime, Date classEndTime, Integer maId) {
        return stateMapper.getAcceptedGoodsByTimeAndMaId(classStartTime,classEndTime,maId);
    }

    @Override
    public List<ExecuteStateVO> findMostEalryRecord(String parse,Integer userId) {
        return stateMapper.findMostEalryRecord(parse,userId);
    }

    @Override
    public List<ExecuteStateVO> findStartTimeByTime(Date tempTime, String parse, Integer userId) {
        return stateMapper.findStartTimeByTime(tempTime,parse,userId);
    }

    @Override
    public List<ExecuteStateVO> getExcuteStateDetailBysdId(String sdId,String maId) {
        return stateMapper.getExecuteStateDetailBysdId(sdId,maId);
    }

    @Override
    public List<ExecuteStateVO> getWorkbatchInfo(Date startTime, Date endTime) {
        return stateMapper.getWorkbatchInfo(startTime,endTime);
    }

    @Override
    public List<ExecuteState> getParaTimeByCondition(Date startTime, Date endTime, Integer userId, Integer mtId) {
        return stateMapper.getParaTimeByCondition(startTime,endTime,userId,mtId);
    }

    @Override
    public List<EquipmentVO> stateList(ExecuteStateParamVO executeStateVO) {
        Integer wsId = executeStateVO.getWsId();
        Integer maType = executeStateVO.getMaType();
        String endTime = executeStateVO.getEndTime();
        String startTime = executeStateVO.getStartTime();
        if(wsId == null || wsId == 0){
            endTime = endTime + " 23:59:59";
        }else {
            WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId,executeStateVO.getMaId());
            if(workbatchShiftset == null){
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String wfStartTime = sdf.format(workbatchShiftset.getStartTime());
            String wfEndTime = sdf.format(workbatchShiftset.getEndTime());
            startTime = startTime + " " + wfStartTime;
            executeStateVO.setStartTime(startTime);
            endTime = endTime + " " + wfEndTime;
        }
        executeStateVO.setEndTime(endTime);

        /*查询满足条件的所有设备状态数据*/
        List<ExecuteStateVO> executeStateVOList = stateMapper.stateList(executeStateVO);

        Map<Integer, List<ExecuteStateVO>> executeStateVOMap = new HashMap<>();//用于处理数据
        List<ExecuteStateVO> stateVOList;//同一设备的状态集合
        /*先把同一设备的放入一个集合*/
        Iterator<ExecuteStateVO> executeStateVOIterator = executeStateVOList.iterator();
        Set<Integer> maIdSet = new TreeSet<>();
        while (executeStateVOIterator.hasNext()) {
            ExecuteStateVO stateVO = executeStateVOIterator.next();
            Integer maId = stateVO.getMaId();
            maIdSet.add(maId);
            if (executeStateVOMap.containsKey(maId)) {//判断map中此key是否存在
                executeStateVOMap.get(maId).add(stateVO);
            } else {//如不存在key则创建list
                stateVOList = new ArrayList<>();
                stateVOList.add(stateVO);
                executeStateVOMap.put(stateVO.getMaId(), stateVOList);
            }
        }
        /*查询计划的时间段*/
        List<PlanStateTime> workbatchOrdlinkList = workbatchOrdlinkMapper.selectPlanMainch(wsId, maType, startTime, endTime, null);
        Map<Integer, List<PlanStateTime>> planStateTimeMap = new HashMap<>();
        for(PlanStateTime planStateTime : workbatchOrdlinkList){
            Integer maId = planStateTime.getMaId();
            if(planStateTimeMap.containsKey(maId)){
                planStateTimeMap.get(maId).add(planStateTime);
            }else {
                List<PlanStateTime> planStateTimeList = new ArrayList<>();
                planStateTimeList.add(planStateTime);
                planStateTimeMap.put(maId, planStateTimeList);
            }
        }
        List<EquipmentVO> executeStateVOTimeList = timeSeries(executeStateVOMap, planStateTimeMap);
        return executeStateVOTimeList;
    }

    @Override
    public List<ExecuteStateVO> getAcceptedGoodsByTime(Integer exId, Integer mtId) {
        return stateMapper.getAcceptedGoodsByTime(exId,mtId);
    }

    private List<EquipmentVO> timeSeries(Map<Integer, List<ExecuteStateVO>> executeStateVOMap, Map<Integer, List<PlanStateTime>> planStateTimeMap) {//设备的时序时间段处理
        List<EquipmentVO> executeStateVOTimeList = new ArrayList<>();
        PlanMachineVO planMachineVO;
        PrepareVO prepareVO;
        ProductionVO productionVO;
        DowntimeVO downtimeVO;
        MaintainVO maintainVO;
        List<PrepareVO> prepareVOList;
        List<ProductionVO> productionVOList;
        for(Integer key : planStateTimeMap.keySet()){
            /*---------------------计划----------------------------*/
            List<MealStayVO> mealStayVOList = new ArrayList<>();//计划吃饭时间
            List<PlanStateTime> planStateTimeList = planStateTimeMap.get(key);
            Iterator<PlanStateTime> ordlinkIterator = planStateTimeList.iterator();
            EquipmentVO executeStateVOTime = new EquipmentVO();
            planMachineVO = new PlanMachineVO();//计划对象
            prepareVOList = new ArrayList<>();//计划准备时间
            productionVOList = new ArrayList<>();//计划工单时间
            int meal = 0;
            Integer wsId = null;
            while (ordlinkIterator.hasNext()){
                PlanStateTime planStateTime = ordlinkIterator.next();
                String sdDate = planStateTime.getSdDate();
                String mealOnetime = planStateTime.getMealOnetime();
                String mealSecondtime = planStateTime.getMealSecondtime();
                String mealThirdtime = planStateTime.getMealThirdtime();
                if(meal == 0){
                    if(mealOnetime != null){//第一次吃饭时间
                        mealStayVOList = getMealStayVOList(mealStayVOList, mealOnetime, sdDate);
                    }
                    if(mealSecondtime != null){//第二次吃饭时间
                        mealStayVOList = getMealStayVOList(mealStayVOList, mealSecondtime, sdDate);
                    }
                    if(mealThirdtime != null){//第三次吃饭时间
                        mealStayVOList = getMealStayVOList(mealStayVOList, mealThirdtime, sdDate);
                    }
                    wsId = planStateTime.getWsId();
                }
                if(wsId != null && meal != 0){
                    if(!wsId.equals(planStateTime.getWsId())){
                        if(mealOnetime != null){//第一次吃饭时间
                            mealStayVOList = getMealStayVOList(mealStayVOList, mealOnetime, sdDate);
                        }
                        if(mealSecondtime != null){//第二次吃饭时间
                            mealStayVOList = getMealStayVOList(mealStayVOList, mealSecondtime, sdDate);
                        }
                        if(mealThirdtime != null){//第三次吃饭时间
                            mealStayVOList = getMealStayVOList(mealStayVOList, mealThirdtime, sdDate);
                        }
                        wsId = planStateTime.getWsId();
                    }
                }
                meal++;
                Date startTime = planStateTime.getProBeginTime();
                Date closeTime = planStateTime.getProFinishTime();
                Integer mouldStay = planStateTime.getMouldStay() == null ? 0 : planStateTime.getMouldStay();//换膜时间(分)
                Date mouldStaystartTime = null;
                if(startTime != null){
                    mouldStaystartTime = new Date(startTime.getTime() + (mouldStay * 60 * 1000));
                }
                if(mouldStay != null){
                    prepareVO = new PrepareVO();
                    prepareVO.setStartTime(startTime);//计划生产准备时间
                    prepareVO.setEndTime(mouldStaystartTime);
                    prepareVOList.add(prepareVO);
                }
                productionVO = new ProductionVO();//计划生产时间
                productionVO.setStartTime(mouldStaystartTime);
                productionVO.setEndTime(closeTime);
                productionVOList.add(productionVO);

            }
            if(!planStateTimeList.isEmpty()){
                String maName = planStateTimeList.get(0).getMaName();
                executeStateVOTime.setMachineName(maName);
            }
            executeStateVOTime.setMaId(key);
            planMachineVO.setPrepareVOList(prepareVOList);
            planMachineVO.setProductionVOList(productionVOList);
            planMachineVO.setMealStayVOList(mealStayVOList);
            executeStateVOTime.setPlanMachineVO(planMachineVO);

            /*---------------------实际------------------------------*/
            List<ExecuteStateVO> executeStateVOList = executeStateVOMap.get(key);
            if(executeStateVOList != null && !executeStateVOList.isEmpty()){
                prepareVOList = new ArrayList<>();//准备时间段
                productionVOList = new ArrayList<>();//生产时间段
                List<DowntimeVO> downtimeVOList = new ArrayList<>();//停机时间段
                List<IntervalVO> intervalVOList = new ArrayList<>();//间隔时间
                List<MaintainVO> maintainVOList = new ArrayList<>();//保养时间段
                IntervalVO intervalVO = null;
                for (ExecuteStateVO ecuteStateVO : executeStateVOList) {
                    Date startTime = ecuteStateVO.getStartAt();
                    Date endTime = ecuteStateVO.getEndAt();
                    switch (ecuteStateVO.getEvent()) {
                        case ("B1"): /*{//接单
//                            if (intervalVO != null) {//工单间隔结束时间
//                                intervalVO.setEndTime(startTime);
//                                intervalVOList.add(intervalVO);
//                            }
                            //准备时间段
                            prepareVO = new PrepareVO();
                            prepareVO.setStartTime(startTime);
                            prepareVO.setEndTime(endTime);
                            prepareVOList.add(prepareVO);
                            break;
                        }*/
                        case ("B3"): {//换膜(暂时列为生产准备)
                            //准备时间段
                            prepareVO = new PrepareVO();
                            prepareVO.setStartTime(startTime);
                            prepareVO.setEndTime(endTime);
                            prepareVOList.add(prepareVO);
                            break;
                        }
                        case ("B2"): {//保养时间
                            maintainVO = new MaintainVO();
                            maintainVO.setStartTime(startTime);
                            maintainVO.setEndTime(endTime);
                            maintainVOList.add(maintainVO);
                            break;
                        }
                        case ("C1"): /*{//正式生产
                        productionVO = new ProductionVO();
                        productionVO.setStartTime(startTime);
                        productionVO.setEndTime(endTime);
                        productionVOList.add(productionVO);
                        break;
                    }*/
                        case ("C4"): {//暂停(正式生产中时间)
                            productionVO = new ProductionVO();
                            productionVO.setStartTime(startTime);
                            productionVO.setEndTime(endTime);
                            productionVOList.add(productionVO);
                            break;
                        }
                        case ("C2"): {//停机
                            downtimeVO = new DowntimeVO();
                            downtimeVO.setStartTime(startTime);
                            downtimeVO.setEndTime(endTime);
                            downtimeVOList.add(downtimeVO);
                            break;
                        }
                        case ("D2"):
                        case ("D3"):
                        case ("D1"): {//结束生产
                            intervalVO = new IntervalVO();
                            intervalVO.setStartTime(startTime);//工单间隔开始时间
                            intervalVO.setEndTime(endTime);
                            intervalVOList.add(intervalVO);
                            break;
                        }
                    }
                }
                executeStateVOTime.setPrepareVOList(prepareVOList);//准备时间段
                executeStateVOTime.setProductionVOList(productionVOList);//生产时间段
                executeStateVOTime.setDowntimeVOList(downtimeVOList);//停机时间段
                executeStateVOTime.setIntervalVOList(intervalVOList);//间隔时间
                executeStateVOTime.setMaintainVOList(maintainVOList);//保养时间段
            }
            executeStateVOTimeList.add(executeStateVOTime);
        }

        return executeStateVOTimeList;
    }

    /**
     * 处理吃饭时间
     * @param mealStayVOList
     * @param mealTime
     * @param sdDate
     * @return
     */
    private List<MealStayVO> getMealStayVOList(List<MealStayVO> mealStayVOList, String mealTime, String sdDate){
        MealStayVO mealStayVO = new MealStayVO();
        Date[] mealOneTime = getMealTime(mealTime, sdDate);
        mealStayVO.setStartTime(mealOneTime[0]);
        mealStayVO.setEndTime(mealOneTime[1]);
        mealStayVOList.add(mealStayVO);
        return mealStayVOList;
    }

    /**
     * 分割班次吃饭时间,返回数组
     * @param mealString
     * @param targeDay
     * @return
     */
    private Date[] getMealTime(String mealString, String targeDay){
        String[] split = mealString.split("~");
        String s1 = split[0];
        String s2 = split[1];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date[] dates = new Date[2];
        try {
            Date date = simpleDateFormat.parse(s1);
            Date date1 = simpleDateFormat.parse(s2);
            Date toDate = DateUtil.toDate(targeDay + " " + DateUtil.format(date, "HH:mm:ss"), null);
            dates[0] = toDate;
            Date toDate1 = DateUtil.toDate(targeDay + " " + DateUtil.format(date1, "HH:mm:ss"), null);
            dates[1] = toDate1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }


    @Override
    public List<String> stateMachineList(ExecuteStateParamVO executeStateVO) {
        List<ExecuteStateVO> executeStateVOList = stateMapper.stateList(executeStateVO);
        Map<Integer, List<ExecuteStateVO>> executeStateVOMap = new HashMap<>();//用于处理数据
        List<ExecuteStateVO> stateVOList;//同一设备的状态集合
        /*先把同一设备的放入一个集合*/
        Iterator<ExecuteStateVO> executeStateVOIterator = executeStateVOList.iterator();
        while (executeStateVOIterator.hasNext()) {
            ExecuteStateVO stateVO = executeStateVOIterator.next();
            Integer maId = stateVO.getMaId();
            if (executeStateVOMap.containsKey(maId)) {//判断map中此key是否存在
                executeStateVOMap.get(maId).add(stateVO);
            } else {//如不存在key则创建list
                stateVOList = new ArrayList<>();
                stateVOList.add(stateVO);
                executeStateVOMap.put(stateVO.getMaId(), stateVOList);
            }
        }
        List<String> mahineList = new ArrayList<>();
        for (Map.Entry<Integer, List<ExecuteStateVO>> entry : executeStateVOMap.entrySet()){
            mahineList.add(entry.getValue().get(0).getMachineName());
        }
        return mahineList;
    }

    @Override
    public List<String> getDistinceSdId(Date classStartTime, Date classEndTime, Integer mtId) {
        return stateMapper.getDistinceSdId(classStartTime,classEndTime,mtId);
    }

    @Override
    public List<ExecuteStateVO> getSumCountByMtId(Date classStartTime, Date classEndTime, Integer mtId) {
        return stateMapper.getSumCountByMtId(classStartTime,classEndTime,mtId);
    }
}
