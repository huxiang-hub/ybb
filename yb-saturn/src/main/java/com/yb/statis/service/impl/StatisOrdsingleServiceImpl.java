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
import com.yb.execute.vo.ParpreVo;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.entity.StatisOrdinfo;
import com.yb.statis.entity.StatisOrdsingle;
import com.yb.statis.mapper.StatisOrdsingleMapper;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.statis.service.IStatisOrdinfoService;
import com.yb.statis.service.IStatisOrdsingleService;
import com.yb.statis.vo.StatisOrdsingleVO;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class StatisOrdsingleServiceImpl extends ServiceImpl<StatisOrdsingleMapper, StatisOrdsingle> implements IStatisOrdsingleService {
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
    private IStatisOeesetService iStatisOeesetService;
    @Autowired
    private IOrderWorkbatchService iOrderWorkbatchService;
    @Autowired
    private IProcessWorkinfoService iProcessWorkinfoService;
    @Autowired
    private IStatisOrdinfoService iStatisOrdinfoService;
    @Autowired
    private IStatisOrdsingleService iStatisOrdsingleService;
    @Autowired
    private StatisOrdsingleMapper statisOrdsingleMapper;
    @Autowired
    private IWorkbatchShiftsetService workbatchShiftsetService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private IProcessMachlinkService ProcessMachlinkService;
    @Autowired
    private ISuperviseIntervalService superviseIntervalService;
    @Autowired
    private IExecuteInfoService iExecuteInfoService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;

    @Override
    public IPage<StatisOrdsingleVO> selectStatisOrdsinglePage(IPage<StatisOrdsingleVO> page, StatisOrdsingleVO statisOrdsingle) {
        return page.setRecords(baseMapper.selectStatisOrdsinglePage(page, statisOrdsingle));
    }

    @Override
    public IPage<StatisOrdsingleVO> selectOEEStatisOrdsinglePage(IPage<StatisOrdsingleVO> page, StatisOrdsingleVO statisOrdsingle) {
        return page.setRecords(baseMapper.selectOEEStatisOrdsinglePage(page, statisOrdsingle));
    }

    @Override
    //排产单
    public void generateOrderOEEReport(Integer userId, Integer mtId, Integer sdId, Double dutyNumber, String osId, Integer exId) throws ParseException {
        Date startTime = new Date();//订单的开始时间
        Date endTime = new Date();//订单的结束时间
        //这里是默认为null 但是呢 exid 可以用作使用
        if (osId != null) {
            StatisOrdinfo statisordinfo = iStatisOrdinfoService.getBaseMapper().selectById(osId);
            exId = statisordinfo.getExId();
        }
        // 通过传入过来的exId 来获取 我们实际需要统计的时间
        ExecuteInfo executeInfo = iExecuteInfoService.getBaseMapper().selectById(exId);
        if (executeInfo != null) {
            startTime = executeInfo.getStartTime();
            endTime = executeInfo.getEndTime();
        }

        //A 列 设备名称  查询条件名称
        String machineName = getMachineName(mtId);
        //B 列 oee统计时间2020-04-15 ！
        String calculateTime = DateUtil.refNowDay();
        //C 列 人员姓名！
        String OpreationPersonName = getPersonName(userId);
        //D 列 班次名称！ 通过exId 去查询真实的班次信息
        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getById(executeInfo.getWsId());
        String sfName = workbatchShiftset.getCkName();
        Date classStartTime = new Date();
        Date classEndTime = new Date();
        if (workbatchShiftset != null) {
            classStartTime = workbatchShiftset.getStartTime();
            classEndTime = workbatchShiftset.getEndTime();
        }
        //班次时间
        Integer sfTime = (int) ((classEndTime.getTime() - classStartTime.getTime()) / (60 * 1000));

        //E 列 设备保养 ！
        String maintainType = "B2";
        Integer maintainTime = 0;
        Integer maintainMin = 0;
        ParpreVo parpreVo = getMaintain(startTime, endTime, maintainType, mtId);
        if (parpreVo != null) {
            maintainTime = parpreVo.getMaintainTime();
            maintainMin = parpreVo.getMaintainMin();
        }

        //F列 换膜时间 ！
        String exchangeType = "B3";
        Integer exchangeTime = 0;
        Integer exchangeMin = 0;
        ParpreVo exchangeParpre = getExchange(startTime, endTime, exchangeType, mtId);
        if (exchangeParpre != null) {
            exchangeTime = exchangeParpre.getExchangeTime();
            exchangeMin = exchangeParpre.getExchangeMin();
        }

        //K 列印联盒采集数量!
        Integer sumCollectNumber = 1;
        //M 良品数量!
        Integer sumGoodAccept = 0;
        //N 废品数量!
        Integer sumWasteNumber = 0;
        List<ExecuteStateVO> executeStats = iExecuteStateService.getAcceptedGoodsByTime(exId, mtId);
        if (executeStats.size() != 0 && executeStats != null && executeStats.get(0) != null) {
            sumWasteNumber = executeStats.get(0).getWasteNum();
            sumCollectNumber = executeStats.get(0).getBoxNum();
            sumGoodAccept = executeStats.get(0).getCountNum();
        }

        //O 作业数！
        Integer workNumber = sumGoodAccept + sumWasteNumber;

        //L 质检次数!
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
        List<ExecuteStateVO> wearErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, mtId, wearErr);
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
        Double SumTime = equipmentErrTime + equalityErrTime + planErrTime + manageErrTime + wearErrTime + restTime;

        //实际出勤时间
        Long miniSecond = endTime.getTime() - startTime.getTime();
        Double min = (double)Math.round((double) miniSecond / (60 * 1000));

        WorkbatchOrdoee ordoee = null;
        WorkbatchOrdlink workbatchOrdlink = iWorkbatchOrdlinkService.getById(sdId);
        Map<String, Object> condiMap = new HashMap<>();
        if (workbatchOrdlink != null) {
            condiMap.put("wk_id", workbatchOrdlink.getId());
            List<WorkbatchOrdoee> ordoees = iWorkbatchOrdoeeService.getBaseMapper().selectByMap(condiMap);
            if (ordoees.size() != 0 && ordoees != null && ordoees.get(0) != null) {
                ordoee = ordoees.get(0);
            }
        }

        //按照作业数来设置除以排产的计划数量。来预估这个标准指标
        Double finishRate = workNumber.doubleValue() / workbatchOrdlink.getPlanNum();

        //这里用来算机器的工艺名称和转速
        StringBuffer prName = new StringBuffer();
        //AE 标准指标  理论生产性能 机器的转的转速
        Integer acdemicProperties = 1;
        if (ordoee.getSpeed() != null) {
            acdemicProperties = ordoee.getSpeed();
        }
        if (acdemicProperties==0){
            acdemicProperties =1;
        }

        Integer StandMouldNumTime = ordoee.getMouldStay();
        if (StandMouldNumTime == null) {
            StandMouldNumTime = 0;
        }
        //AC 标准指标   这个排产需要的换膜时长
        Integer StandMouldMin = ordoee.getMouldStay();
        if (StandMouldMin == null) {
            StandMouldMin = 0;
        }
        //AD 标准指标   质量检测总次数!
//        Integer StandQualityNumber = ordoee.getQualityNum();
//        if (StandQualityNumber == null) {
//            StandQualityNumber = 0;
//        }

        Double StandWasterTotal = workbatchOrdlink.getExtraNum() * finishRate;
        //其他指标  难易程度
        Double difficultNumber = ordoee.getDifficultNum();
        if (difficultNumber == null) {
            difficultNumber = 0.0;
        }
        //换膜时间 其他指标  生产准备总时长 标准的保养+ 标准的换膜
        Integer totalPre = StandMouldMin;
        //任务数量 加工数-任务数（计划订单要求总数+损耗数）
        Integer standWorkNumber = workbatchOrdlink.getPlanNum() + workbatchOrdlink.getExtraNum();
         //标准   计划时间
        Integer standTime = 0;
        if (workbatchOrdlink.getPlanNum() != null) {
            if ((acdemicProperties / 60)==0){
                acdemicProperties=60;
            }
            standTime = (workbatchOrdlink.getPlanNum()) / (acdemicProperties / 60);

            if ((acdemicProperties / 60)==0){
                acdemicProperties=1;
            }
        }
        //实际生产准备时间！
        //H列 实际稼动时间(计划稼动-总停机时间)
        Integer sunCount = superviseIntervalService.getSumTimeByMaId(2, mtId, startTime, endTime);
        //O列  停机（停机、等待）
        Integer sunCountMin = sunCount / 60;
        //实际生产准备时间
        Integer realParpareMin =1;
        try {
             realParpareMin = getRealParaTime(exId);
        }catch (Exception e){
            realParpareMin=20;
        }
        //H 标准生产时间
        //生产准备总时间
        Integer standPareTime = StandMouldNumTime;
        Double standProductTime = min;
        //I 金世纪计划稼动时间（标准生产时间-标准保养、和换模时间） H-G
        Double jsjplanProductTime = standProductTime - realParpareMin;
        //I 计划稼动时间（标准生产时间-标准保养、和换模时间） H-G
        Double planProductTime = standProductTime - StandMouldNumTime;
        //J 实际稼动时间（标准生产时间-实际生产时间）
        Double realActivationTIme = standProductTime - sunCountMin;
        //P 实际生产性能（速度值）（作业数/实际稼动时间） O/I
        Double realProducePower = (workNumber / (realActivationTIme)) * 60;
        // 时间稼动率（实际时间/标准时间）
        BigDecimal TimeMonement = new BigDecimal(realActivationTIme / standProductTime).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (TimeMonement.compareTo(new BigDecimal(0.0100)) == -1) {
            TimeMonement = new BigDecimal(0.0100).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        if (sumGoodAccept==0){
            sumGoodAccept=1;
        }
        //良品率（良品数/盒子数）
        BigDecimal goodRates = new BigDecimal(((float) sumGoodAccept / (sumGoodAccept + sumWasteNumber))).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (goodRates.compareTo(new BigDecimal(0.0100)) == -1) {
            goodRates = new BigDecimal(0.0100).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //性能稼动率
        BigDecimal qualityRate = new BigDecimal((realProducePower / (acdemicProperties))).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (qualityRate.compareTo(new BigDecimal(0.0100)) == -1) {
            qualityRate = new BigDecimal(0.0100).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //OEE（时间稼动率x良品率x性能稼动率）
        BigDecimal OEErate = (TimeMonement.multiply(goodRates).multiply(qualityRate)).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (OEErate.compareTo(new BigDecimal(0.0100)) == -1) {
            OEErate = new BigDecimal(0.0100).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //在这里进行插入
        StatisOrdinfo statisOrdinfo = null;
        // recaculate false 的时候。我们new 对象判断为插入，如果不为null 的时候，那么我们是直接update。
        if (osId == null) {
            statisOrdinfo = new StatisOrdinfo();
        } else {
            statisOrdinfo = iStatisOrdinfoService.getById(osId);
        }
        //订单ID 通过订单编号查询订单ID
        Map<String, Object> orderInfoMap = new HashMap<>();
        orderInfoMap.put("od_no", workbatchOrdlink.getOdNo());
        List<WorkbatchOrdlink> workbatchOrdlinks = iWorkbatchOrdlinkService.getBaseMapper().selectByMap(orderInfoMap);
        if (workbatchOrdlinks.size() != 0 && workbatchOrdlinks != null && workbatchOrdlinks.get(0) != null) {
            WorkbatchOrdlink workbatchOrd = workbatchOrdlinks.get(0);
            statisOrdinfo.setOdId(workbatchOrd.getId());
            statisOrdinfo.setOdNum(workbatchOrd.getOdNo());
            statisOrdinfo.setWbId(workbatchOrd.getWbId());
            OrderWorkbatch orderWorkbatch = iOrderWorkbatchService.getById(workbatchOrd.getWbId());
            statisOrdinfo.setWbNum(orderWorkbatch.getBatchNo());
            statisOrdinfo.setMaId(mtId);
            statisOrdinfo.setExId(exId);
            statisOrdinfo.setMaName(machineName);
            statisOrdinfo.setPrName(workbatchOrdlink.getPrName());
        }
        //通过prname 去查询 pr的Id
        Map<String, Object> processWorkInfoMap = new HashMap<>();
        processWorkInfoMap.put("pr_name", workbatchOrdlink.getPrName());
        List<ProcessWorkinfo> processWorkinfos = iProcessWorkinfoService.getBaseMapper().selectByMap(processWorkInfoMap);
        if (processWorkinfos.size() != 0 && processWorkinfos != null && processWorkinfos.get(0) != null) {
            statisOrdinfo.setPrId(processWorkinfos.get(0).getId());
        }
        statisOrdinfo.setUsId(userId);
        statisOrdinfo.setUsName(OpreationPersonName);
        statisOrdinfo.setSfName(sfName);
        statisOrdinfo.setOeDate(calculateTime);
        statisOrdinfo.setSdId(sdId);
        statisOrdinfo.setDutyNum(dutyNumber);
        statisOrdinfo.setOrdStart(startTime);
        statisOrdinfo.setOrdEnd(endTime);
        if (osId == null) {
            iStatisOrdinfoService.saveOrUpdate(statisOrdinfo);
        } else {
            iStatisOrdinfoService.updateById(statisOrdinfo);
        }
        //OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
        StatisOrdsingle statisOrdsingle = null;
        if (osId == null) {
            statisOrdsingle = new StatisOrdsingle();
        } else {
            Map<String, Object> statisOrdsingleMap = new HashMap<>();
            statisOrdsingleMap.put("so_id", osId);
            List<StatisOrdsingle> statisOrdsingleList = iStatisOrdsingleService.getBaseMapper().selectByMap(statisOrdsingleMap);
            if (statisOrdsingleList.size() != 0 && statisOrdsingleList != null) {
                statisOrdsingle = statisOrdsingleList.get(0);
            }
        }
        statisOrdsingle.setSoId(statisOrdinfo.getId());
        statisOrdsingle.setStartTime(startTime);
        statisOrdsingle.setEndTime(endTime);
        //出勤时间A -间隔停留时间（分）真实出勤时间
        //   statisOrdsingle.setWorkStay(classDuration);
        statisOrdsingle.setMaintainStay(maintainMin);
        statisOrdsingle.setMaintainNum(maintainTime);
        statisOrdsingle.setMouldStay(exchangeMin);
        statisOrdsingle.setMouldNum(exchangeTime);
        statisOrdsingle.setPrepareStay(realParpareMin.intValue());
        //O标准生产时间分钟（标准出勤-休息开会吃饭）
        statisOrdsingle.setStandardRuntime(standProductTime.intValue());
        statisOrdsingle.setStandTime(standTime);
        statisOrdsingle.setPlanutilizeStay(planProductTime.intValue());
        statisOrdsingle.setJsjPlanutilizeStay(jsjplanProductTime.intValue());
        statisOrdsingle.setFactutilizeStay(realActivationTIme.intValue());
        statisOrdsingle.setNodefectCount(sumGoodAccept);
        statisOrdsingle.setWatesCount(sumWasteNumber);
        //任务数量 加工数-任务数（计划订单要求总数+损耗数）
        statisOrdsingle.setTaskCount(standWorkNumber);
        statisOrdsingle.setBoxNum(sumCollectNumber);
        statisOrdsingle.setQualityNum(qualityTime);
        statisOrdsingle.setFactSpeed(realProducePower.intValue());
        statisOrdsingle.setFaultStay(equipmentErrTime.intValue());
        statisOrdsingle.setQualityStay(equalityErrTime.intValue());
        statisOrdsingle.setPlanStay(planErrTime.intValue());
        statisOrdsingle.setManageStay(manageErrTime.intValue());
        statisOrdsingle.setAbrasionStay(wearErrTime.intValue());
        statisOrdsingle.setRestStay(restTime.intValue());
        statisOrdsingle.setStayTotal(sunCountMin.intValue());
        statisOrdsingle.setWorkCount(workNumber);
        statisOrdsingle.setUtilizeRate(TimeMonement.multiply(new BigDecimal(100)));
        statisOrdsingle.setYieldRate(goodRates.multiply(new BigDecimal(100)));
        statisOrdsingle.setPerformRate(qualityRate.multiply(new BigDecimal(100)));
        statisOrdsingle.setGatherRate(OEErate.multiply(new BigDecimal(100)));
        if (osId == null) {
            iStatisOrdsingleService.saveOrUpdate(statisOrdsingle);
        } else {
            iStatisOrdsingleService.updateById(statisOrdsingle);
        }
        StatisOeeset statisOeeset = null;
        if (osId == null) {
            statisOeeset = new StatisOeeset();
        } else {
            Map<String, Object> oeesetMap = new HashMap<>();
            oeesetMap.put("db_id", osId);
            List<StatisOeeset> statisOeesets = iStatisOeesetService.getBaseMapper().selectByMap(oeesetMap);
            if (statisOeesets.size() != 0 && statisOeesets != null) {
                statisOeeset = statisOeesets.get(0);
            }
        }
        statisOeeset.setStType(2);
        statisOeeset.setSfStaytime(sfTime);
        statisOeeset.setDbId(statisOrdinfo.getId());
        statisOeeset.setMaintainNum(StandMouldNumTime);
        statisOeeset.setMaintainTime(StandMouldMin);
//        statisOeeset.setQualityNum(StandQualityNumber);
        statisOeeset.setNormalSpeed(acdemicProperties.intValue());
        statisOeeset.setShiftsTotalnum(standWorkNumber);
        statisOeeset.setPrSpeed(prName.toString());
        statisOeeset.setWasteNum(StandWasterTotal.intValue());
        statisOeeset.setDiffNum(new BigDecimal(difficultNumber));
        statisOeeset.setPrepareStay(totalPre);
        // 当machOeeID 为null 的时候。我们new 对象判断为插入，如果不为null 的时候，那么我们是直接update。
        if (osId == null) {
            iStatisOeesetService.saveOrUpdate(statisOeeset);
        } else {
            iStatisOeesetService.updateById(statisOeeset);
        }
    }

    public String getMachineName(Integer mtId) {
        //A 列 设备名称  查询条件名称
        MachineMainfo machineMainfo = iMachineMainfoService.getBaseMapper().selectById(mtId);
        String machineName = "无";
        if (machineMainfo != null) {
            machineName = machineMainfo.getName();
        }
        return machineName;
    }

    public String getPersonName(Integer userId) {
        Map<String, Object> OpreatConditionMap = new HashMap<>();
        OpreatConditionMap.put("user_id", userId);
        List<BaseStaffinfo> baseStaffinfos = iBaseStaffinfoService.getBaseMapper().selectByMap(OpreatConditionMap);
        String OpreationPersonName = "无";
        if (baseStaffinfos.size() != 0 && baseStaffinfos != null && baseStaffinfos.get(0) != null) {
            OpreationPersonName = baseStaffinfos.get(0).getName();
        }
        return OpreationPersonName;
    }

    public ParpreVo getMaintain(Date startTime, Date endTime, String type, Integer mtId) {
        ExecuteStateVO executeState = iExecuteStateService.getExecuteVoListBy(startTime, endTime, type, mtId);
        Integer maintainMin = 0;
        Integer maintainTime = 0;
        if (executeState != null && executeState.getSumDuraction() != null && executeState.getCountExecuteNumber() != null) {
            maintainMin = executeState.getSumDuraction() / 60;
            maintainTime = executeState.getCountExecuteNumber();
        }
        ParpreVo ParpreVo = new ParpreVo();
        ParpreVo.setMaintainMin(maintainMin);
        ParpreVo.setMaintainTime(maintainTime);
        return ParpreVo;
    }

    public ParpreVo getExchange(Date startTime, Date endTime, String type, Integer mtId) {
        ExecuteStateVO executeState = iExecuteStateService.getExecuteVoListBy(startTime, endTime, type, mtId);
        Integer exchangeMin = 0;
        Integer exchangeTime = 0;
        if (executeState != null && executeState.getSumDuraction() != null && executeState.getCountExecuteNumber() != null) {
            exchangeMin = executeState.getSumDuraction() / 60;
            exchangeTime = executeState.getCountExecuteNumber();
        }
        ParpreVo ParpreVo = new ParpreVo();
        ParpreVo.setExchangeMin(exchangeMin);
        ParpreVo.setExchangeTime(exchangeTime);
        return ParpreVo;
    }

    public Integer getRealParaTime(Integer exId) {
        ExecuteInfo executeInfo = iExecuteInfoService.getById(exId);
        Long pareSecond =executeInfo.getExeTime().getTime()-executeInfo.getStartTime().getTime();
        Integer pareMin = (int) (pareSecond / (60 * 1000));
        return pareMin;
    }

    @Override
    public List<StatisOrdsingleVO> getStatisInfoBySdId(Integer sdId) {
        return statisOrdsingleMapper.getStatisInfoBySdId(sdId);
    }
}
