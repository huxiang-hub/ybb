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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.entity.StatisMachsingle;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.mapper.StatisMachsingleMapper;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.service.IStatisMachsingleService;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.statis.vo.StatisMachsingleVO;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchShiftVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Blade
 * @since 2020-04-17
 */
@Service
public class StatisMachsingleServiceImpl extends ServiceImpl<StatisMachsingleMapper, StatisMachsingle> implements IStatisMachsingleService {

    //OEE需要导入的service
    @Autowired
    private IExecuteStateService iExecuteStateService;
    @Autowired
    private IExecuteWasteService iExecuteWasteService;
    @Autowired
    private IMachineMainfoService iMachineMainfoService;
    @Autowired
    private IBaseStaffinfoService iBaseStaffinfoService;
    @Autowired
    private IWorkbatchOrdoeeService iWorkbatchOrdoeeService;
    @Autowired
    private IWorkbatchOrdlinkService iWorkbatchOrdlinkService;
    @Autowired
    private IStatisMachoeeService iStatisMachoeeService;
    @Autowired
    private IStatisOeesetService iStatisOeesetService;
    @Autowired
    private IStatisMachsingleService iStatisMachsingleService;
    @Autowired
    private IWorkbatchShiftsetService workbatchShiftsetService;
    @Autowired
    private ISuperviseIntervalService superviseIntervalService;
    @Autowired
    private IExecuteInfoService iExecuteInfoService;


    @Override
    public IPage<StatisMachsingleVO> selectStatisMachsinglePage(IPage<StatisMachsingleVO> page, StatisMachsingleVO statisMachsingle) {
        return page.setRecords(baseMapper.selectStatisMachsinglePage(page, statisMachsingle));
    }

    @Override
    public IPage<StatisMachsingleVO> selectStatisMachsingleoEEPage(IPage<StatisMachsingleVO> page, StatisMachsingleVO statisMachsingle) {
        return page.setRecords(baseMapper.selectStatisMachsingleoEEPage(page, statisMachsingle));
    }

    @Override
    public IPage<StatisMachsingleVO> PageOfMachsinglexy(IPage<StatisMachsingleVO> page, StatisMachsingleVO statisMachsingle) {
        return page.setRecords(baseMapper.PageOfMachsinglexy(page, statisMachsingle));
    }



  // 这里是用于生成班次设备的OEE
    @Override
    public void generateMachineOeeReport(Integer userId, Integer mtId, Date systemDate, Integer machOeeID, Integer exId) throws ParseException {
         //machOeeID 是yb_statis_machine 表的主键 然后用于重新算。
        if (machOeeID!=null){
          StatisMachoee statisMachoee =iStatisMachoeeService.getById(machOeeID);
          exId = statisMachoee.getExId();
        }
        // 先查询ExecuteInfo 执行表的数据， 然后在通过ex_id 来查询所有的数据，
        // 我们第一次把ex_id 存到yb_statis_machine 里面 然后在Controller时候通过
        //重新算在拿出来
        ExecuteInfo executeInfo =  iExecuteInfoService.getById(exId);
        Date startTime = new Date();
        Date endTime = new Date();
        //通过executeInfo,获取到班次ID,然后通过班次ID。获取班次的各种信息。
        //然后通过班次获取这个班次的实际时间，实际的开始时间和实际的结束时间。
        Integer wsId;
        //班次名称
        String sfName ="";
        //标准 保养时长
        Integer standMaintainMin=0;
        //标准 吃饭时间
        Integer standMealTime =0;
        if (executeInfo!=null){
            wsId= executeInfo.getWsId();
//            WorkbatchShiftset workbatchShiftset= workbatchShiftsetService.getBaseMapper().selectById(wsId);
            WorkbatchShiftset workbatchShiftset = workbatchShiftsetService.selectByMaid(wsId,null);//需要有设备id，暂时没有
//            startTime =executeInfo.getSfStartTime();
//            endTime=executeInfo.getSfEndTime();
            sfName = workbatchShiftset.getCkName();
            //这里是标准是吃饭时间
            standMealTime=workbatchShiftset.getMealStay();
            //这里是标准的保养时间
//          standMaintainMin=workbatchShiftset.getMaintainStay();
        }
        //这里是因为数据库里面可能是null 如果数据库里面是null的话。
        //我们可以把数据重新置为0
        if(standMealTime==null){
            standMealTime=0;
        }
        if(standMaintainMin==null){
            standMaintainMin=0;
        }

        //B 列 oee统计时间2020-04-15 ！
        //这里通过现在时间获得班次日期。
        String calculateTime = DateUtil.refNowDay();

        //A 列 设备名称 查询条件名称
        MachineMainfo machineMainfo = iMachineMainfoService.getBaseMapper().selectById(mtId);
        String machineName = null;
        if (machineMainfo != null) {
            machineName = machineMainfo.getName();
        }

        //C 列 人员姓名！
        Map<String, Object> OpreatConditionMap = new HashMap<>();
        OpreatConditionMap.put("user_id", userId);
        List<BaseStaffinfo> baseStaffinfos = iBaseStaffinfoService.getBaseMapper().selectByMap(OpreatConditionMap);
        String OpreationPersonName = null;
        if (baseStaffinfos.size() != 0 && baseStaffinfos != null && baseStaffinfos.get(0) != null) {
            OpreationPersonName = baseStaffinfos.get(0).getName();
        }

        //E 列 设备保养 ！
        String maintain = "B2";
        //根据设备Id 和时间段查询设备保养的时间
        ExecuteStateVO executeStateVOS = iExecuteStateService.getExecuteVoListBy(startTime, endTime, maintain, mtId);
        Integer maintainMin = 0;
        Integer maintainTime = 0;
        if (executeStateVOS != null && executeStateVOS.getSumDuraction() != null && executeStateVOS.getCountExecuteNumber() != null) {
            maintainMin = executeStateVOS.getSumDuraction() / 60;
            maintainTime = executeStateVOS.getCountExecuteNumber();
        }

        //F 列换膜时间 ！
        String exchange = "B3";
        //根据设备Id 和时间段查询设备保养的时间
        ExecuteStateVO executeState = iExecuteStateService.getExecuteVoListBy(startTime, endTime, exchange, mtId);
        Integer moudleMin = 0;
        Integer moudleTime = 0;
        if (executeState != null && executeState.getSumDuraction() != null && executeState.getCountExecuteNumber() != null) {
            moudleMin = executeState.getSumDuraction() / 60;
            moudleTime = executeState.getCountExecuteNumber();
        }

        //K 列印联盒采集数量!
        Integer sumCollectNumber = 0;
        //M 列良品数量! 良品数是修正后的数量。
        Integer sumGoodAccept = 0;
        //N 列废品数量!
        Integer sumWasteNumber = 0;
        //TODO 金世纪独有的字段
        Integer timeSet=0;
        List<ExecuteStateVO> executeStats = iExecuteStateService.getAcceptedGoodsByTimeAndMa(startTime, endTime, mtId);
        if (executeStats.size() != 0 && executeStats != null && executeStats.get(0) != null) {
            sumWasteNumber = executeStats.get(0).getWasteNum();
            sumCollectNumber = executeStats.get(0).getBoxNum();
            sumGoodAccept = executeStats.get(0).getCountNum();
            timeSet= executeStats.get(0).getTimeSet();
        }

        //O 列作业数！
        Integer workNumber = sumGoodAccept + sumWasteNumber;

        //L 列质检次数!
        List<ExecuteWasteVO> executeWasteVOS = iExecuteWasteService.getWateByTime(startTime, endTime);
        Integer qualityTime = 0;
        if (executeWasteVOS.size() != 0 && executeWasteVOS != null) {
            qualityTime = executeWasteVOS.size();
        }

        //停机（停机、等待）
        //Q 设备故障C2-N！
        String equipmentErr = "设备故障C2-N";
        Double equipmentErrTime = 0.0;
        List<ExecuteStateVO> failList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, mtId, equipmentErr);
        if (failList.size() != 0 && failList != null && failList.get(0) != null) {
            equipmentErrTime = failList.get(0).getDuration() / 60;
        }

        //R 品质故障C2-M
        String equalityErr = "品质故障C2-M";
        Double equalityErrTime = 0.0;
        List<ExecuteStateVO> qualityErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, mtId, equalityErr);
        if (qualityErrList.size() != 0 && qualityErrList != null && qualityErrList.get(0) != null) {
            equalityErrTime = qualityErrList.get(0).getDuration() / 60;
        }

        //S 计划停机C2-O
        String planErr = "计划停机C2-O";
        Double planErrTime = 0.0;
        List<ExecuteStateVO> planErrErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, mtId, planErr);
        if (planErrErrList.size() != 0 && planErrErrList != null && planErrErrList.get(0) != null) {
            planErrTime = planErrErrList.get(0).getDuration() / 60;
        }

        //T 管理停止C2-P
        String manageErr = "管理停止C2-P";
        Double manageErrTime = 0.0;
        List<ExecuteStateVO> manageErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, mtId, manageErr);
        if (manageErrList.size() != 0 && manageErrList != null && manageErrList.get(0) != null) {
            manageErrTime = manageErrList.get(0).getDuration() / 60;
        }

        //U 磨损更换C2-Q
        String wearErr = "磨损更换C2-Q";
        Double wearErrTime = 0.0;
        List<ExecuteStateVO> wearErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime,mtId, wearErr);
        if (wearErrList.size() != 0 && wearErrList != null && wearErrList.get(0) != null) {
            wearErrTime = wearErrList.get(0).getDuration() / 60;
        }

        //V 休息吃饭C2-R
        String rest = "休息吃饭C2-R";
        Double restTime = 0.0;
        List<ExecuteStateVO> restList = iExecuteStateService.getExecuteFailclassify(startTime, endTime, mtId, rest);
        if (restList.size() != 0 && restList != null && restList.get(0) != null) {
            restTime = restList.get(0).getDuration() / 60;
        }

        //W 停机等待总计时间！
        Integer stop = superviseIntervalService.getSumTimeByMaId(2, mtId, startTime, endTime);
        Integer stopMin = stop / 60;
        //W列 停机的合计时间
        Double  sumStopMin = qualityTime+equipmentErrTime+equalityErrTime+planErrTime+manageErrTime+wearErrTime+restTime;

        //F列 实际生产准备时间。 获取班次时间内的ExecuteInfo,
        long pareSecond = 1;
        try {
           List<ExecuteInfo> executeInfos = iExecuteInfoService.getSdIdsbyTime(startTime,endTime,mtId);
            for (ExecuteInfo info : executeInfos) {
                pareSecond =pareSecond+ (info.getExeTime().getTime()-info.getStartTime().getTime());
            }
        }catch (Exception e){
             pareSecond = 41*1000*60;
        }

        //X 标准出勤 实际准备时间！ 实际的保养时间和实际的换膜时间 生产准备时间L（分计算）实际时间
        Integer realPreTime = (int)(pareSecond/(1000*60));

        //班次时间
        Long miniSecond = endTime.getTime() - startTime.getTime();
        Long min = millisecondToMinute(miniSecond);

        //一个班次可能会有的多个排产。
        List<WorkbatchOrdoee> resultList = new ArrayList<>();
        String eventId = "D1";//上班A1 下班A2 接单B1保养B2换模B3正式生产C1停机C2质检C3结束生产D1生产上报D2入库D3
        //这里是通过start的时间在班次之中，然后呢 用户id 然后呢 生产结束等标识，拿到了这个班次可能有的排产计划
        List<String> ids = iExecuteStateService.getSdIdbyConditon(startTime, endTime, mtId, userId.toString(), eventId);
        //其他指标   班次时间
        Integer planClassNumber = 0;
        //其他指标  损耗数总
        Integer wasterNumber = 0;
        //当班的计划生产总数
        if (ids.size() != 0) {
            for (String id : ids) {
                if (id != null) {
                    WorkbatchOrdlink workbatchOrdlink = iWorkbatchOrdlinkService.getById(id);
                    if (workbatchOrdlink != null) {
                        planClassNumber = planClassNumber + workbatchOrdlink.getPlanNum() + workbatchOrdlink.getExtraNum();
                        wasterNumber = workbatchOrdlink.getExtraNum() + wasterNumber;
                    }
                }
            }

            for (String id : ids) {
                Map<String, Object> condiMap = new HashMap<>();
                condiMap.put("wk_id", id);
                List<WorkbatchOrdoee> workbatchOrdoees = iWorkbatchOrdoeeService.getBaseMapper().selectByMap(condiMap);
                if (workbatchOrdoees.size() != 0 && workbatchOrdoees != null && workbatchOrdoees.get(0) != null) {
                    resultList.add(workbatchOrdoees.get(0));
                }
            }
        }
        //标准指标   换模总次数!
        Integer standMouldNum = resultList.size();
        //标准指标   换模总时长!
        Integer standMouldStay = 0;
        //标准指标   质量检测总次数!
        Integer totalQualityNumber = 0;
        //这里用来算机器的工艺名称和转速
        StringBuffer prName = new StringBuffer();
        //标准指标   难易程度!
        Double standDifficultSum  = 0.0;
        //标准指标  机器速度
        Integer machineSpeed = 1;
        Integer totalSpeedPlan =1;
        Integer speed =1;
        if (resultList.size() != 0) {
            for (WorkbatchOrdoee ordoee : resultList) {
                standDifficultSum =standDifficultSum + ordoee.getDifficultNum();
                //在这里获得每一个设备的标准时速度
                totalQualityNumber = totalQualityNumber ;//+ ordoee.getQualityNum();
                WorkbatchOrdlink workbatchOrdlink = iWorkbatchOrdlinkService.getById(ordoee.getWkId());
                if (workbatchOrdlink != null) {
                    List<MachineMainfoVO> machineMainfoVOS = iMachineMainfoService.getRateByMachineId(mtId,workbatchOrdlink.getPrName());
                    machineSpeed  =machineSpeed+ordoee.getSpeed()*(workbatchOrdlink.getPlanNum());
                    totalSpeedPlan  =totalSpeedPlan+ workbatchOrdlink.getPlanNum();
                }
                // 这里去拼接名称和转的速度
                speed = machineSpeed/(totalSpeedPlan);
            }
        }

        Double balanceDifficult  =standDifficultSum/standMouldNum;

        //标准生产时间（标准出勤-吃饭、休息）【休息吃饭C2-R：休息R1、吃饭R2、开会R3】!!
        Integer standProduceTime =min.intValue()-standMealTime-timeSet;
        //计划稼动时间（标准生产时间-标准保养、和换模时间）
        //TODO 这里是金世纪独有的
        Integer jsjPlanActivationTime =  (int)(standProduceTime-realPreTime);
        Integer planActivationTime = (int)(standProduceTime -standMouldStay-standMaintainMin);
        //实际稼动时间实际稼动时间（标准生产时间-实际生产时间）
        //（计划稼动时间-故障停机-生产准备-设备故障）
        //减去时间：设备故障C2-N、品质故障C2-M、计划停机C2-O、管理停止C2-P
        Integer RealActivationTime =min.intValue()-stopMin;
        // 时间稼动率（实际时间/标准时间）
        BigDecimal TimeMonement = new BigDecimal((float)RealActivationTime / standProduceTime).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (TimeMonement.compareTo(new BigDecimal(0.0100)) == -1) {
            TimeMonement = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal goodRates ;
        //良品率（良品数/盒子数）
        try {
             goodRates = new BigDecimal(((float) sumGoodAccept / (sumGoodAccept + sumWasteNumber))).setScale(4, BigDecimal.ROUND_HALF_UP);
        }catch (Exception e){
            goodRates = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        if (goodRates.compareTo(new BigDecimal(0.0100)) == -1) {
            goodRates = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //实际生产性能（速度值）（作业数/实际稼动时间）
        BigDecimal productionPerformance = new BigDecimal(60*(((float) workNumber / RealActivationTime))).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (productionPerformance.compareTo(new BigDecimal(0.0100)) ==-1) {
            productionPerformance = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //性能稼动率
        BigDecimal qualityRate = productionPerformance.divide(new BigDecimal((float) speed), 4, BigDecimal.ROUND_HALF_UP);
        if (qualityRate.compareTo(new BigDecimal(0.0100)) ==-1) {
            qualityRate = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //OEE（时间稼动率x良品率x性能稼动率）
        BigDecimal OEErate = (TimeMonement.multiply(goodRates).multiply(qualityRate)).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (OEErate.compareTo(new BigDecimal(0.0100)) == -1) {
            OEErate = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }

        //OEE数据信息表_yb_statis_machoee
        StatisMachoee statisMachoee = null;
        // 当machOeeID 为null 的时候。我们new 对象判断为插入，如果不为null 的时候，那么我们是直接update。
        if (machOeeID == null) {
            statisMachoee = new StatisMachoee();
        } else {
            statisMachoee = iStatisMachoeeService.getById(machOeeID);
        }
        statisMachoee.setOeDate(calculateTime);
        statisMachoee.setMaId(mtId);
        statisMachoee.setMaName(machineName);
        statisMachoee.setUsId(userId);
        statisMachoee.setUsName(OpreationPersonName);
        statisMachoee.setSfName(sfName);
        statisMachoee.setExId(exId);
        statisMachoee.setOeType(1);
        if (machOeeID == null) {
            iStatisMachoeeService.saveOrUpdate(statisMachoee);
        } else {
            iStatisMachoeeService.updateById(statisMachoee);
        }
        StatisMachsingle statisMachsingle = null;
        if (machOeeID == null) {
            statisMachsingle = new StatisMachsingle();
        } else {
            Map<String, Object> machSigleMap = new HashMap<>();
            machSigleMap.put("sm_id", machOeeID);
            List<StatisMachsingle> statisMachsingles = iStatisMachsingleService.getBaseMapper().selectByMap(machSigleMap);
            if (statisMachsingles.size() != 0 && statisMachsingles != null) {
                statisMachsingle = statisMachsingles.get(0);
            }
        }
        statisMachsingle.setSmId(statisMachoee.getId());
        statisMachsingle.setStartTime(startTime);
        statisMachsingle.setEndTime(endTime);
        //设备包养的时间和次数
        statisMachsingle.setMaintainStay(maintainMin);
        statisMachsingle.setMaintainNum(maintainTime);
        //换膜的时间和次数
        statisMachsingle.setMouldNum(moudleTime);
        statisMachsingle.setMouldStay(moudleMin);
        statisMachsingle.setPrepareStay(realPreTime);
        statisMachsingle.setStandardRuntime(standProduceTime.intValue());
        statisMachsingle.setPlanutilizeStay(planActivationTime.intValue());
        //TODO 金世纪独有
        statisMachsingle.setJsjPlanutilizeStay(jsjPlanActivationTime);
        statisMachsingle.setFactutilizeStay(RealActivationTime.intValue());
        statisMachsingle.setNodefectCount(sumGoodAccept);
        statisMachsingle.setWatesCount(sumWasteNumber);
        statisMachsingle.setTaskCount(planClassNumber);
        statisMachsingle.setWorkCount(workNumber);
        statisMachsingle.setBoxNum(sumCollectNumber);
        statisMachsingle.setQualityNum(qualityTime);
        statisMachsingle.setFactSpeed(productionPerformance.intValue());
        statisMachsingle.setFaultStay(equipmentErrTime.intValue());
        statisMachsingle.setQualityStay(equalityErrTime.intValue());
        statisMachsingle.setPlanStay(planErrTime.intValue());
        statisMachsingle.setManageStay(manageErrTime.intValue());
        statisMachsingle.setAbrasionStay(wearErrTime.intValue());
        statisMachsingle.setRestStay(restTime.intValue());
        statisMachsingle.setStayTotal(stopMin);
        statisMachsingle.setUtilizeRate(TimeMonement.multiply(new BigDecimal(100)));
        statisMachsingle.setYieldRate(goodRates.multiply(new BigDecimal(100)));
        statisMachsingle.setPerformRate(qualityRate.multiply(new BigDecimal(100)));
        statisMachsingle.setGatherRate(OEErate.multiply(new BigDecimal(100)));
        if (machOeeID == null) {
            iStatisMachsingleService.saveOrUpdate(statisMachsingle);
        } else {
            iStatisMachsingleService.updateById(statisMachsingle);
        }
        StatisOeeset statisOeeset = null;
        if (machOeeID == null) {
            statisOeeset = new StatisOeeset();
        } else {
            Map<String, Object> oeesetMap = new HashMap<>();
            oeesetMap.put("db_id", machOeeID);
            List<StatisOeeset> statisOeesets = iStatisOeesetService.getBaseMapper().selectByMap(oeesetMap);
            if (statisOeesets.size() != 0 && statisOeesets != null) {
                statisOeeset = statisOeesets.get(0);
            }
        }
        //统计分析设置表_yb_statis_oeeset
        statisOeeset.setStType(1);
        statisOeeset.setDbId(statisMachoee.getId());
        statisOeeset.setSfStarttime(startTime);
        statisOeeset.setSfEndtime(endTime);
        statisOeeset.setSfStaytime(min.intValue());
        statisOeeset.setResetNum(1);
        statisOeeset.setResetTime(standMealTime);
        //标准的包养时间
        statisOeeset.setMaintainNum(1);
        statisOeeset.setMaintainTime(standMaintainMin);
        //标准的换膜时间和次数
        statisOeeset.setMouldNum(standMouldNum);
        statisOeeset.setMouldTime(standMouldStay);
        //标准难易程度
        statisOeeset.setDiffNum(new BigDecimal(balanceDifficult));
        statisOeeset.setQualityNum(totalQualityNumber);
        statisOeeset.setNormalSpeed(speed);
        statisOeeset.setShiftsTotalnum(planClassNumber);
        statisOeeset.setPrSpeed(prName.toString());
        statisOeeset.setWasteNum(wasterNumber);
        statisOeeset.setPrepareStay(realPreTime);
        // 当machOeeID 为null 的时候。我们new 对象判断为插入，如果不为null 的时候，那么我们是直接update。
        if (machOeeID == null) {
            iStatisOeesetService.saveOrUpdate(statisOeeset);
        } else {
            iStatisOeesetService.updateById(statisOeeset);
        }
    }



    public long millisecondToMinute(Long millisecond) {
        return millisecond / (60 * 1000);
    }
}
