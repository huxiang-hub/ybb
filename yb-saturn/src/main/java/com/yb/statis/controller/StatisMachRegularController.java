package com.yb.statis.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.entity.StatisMachregular;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.service.IStatisMachRegularService;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.statis.vo.StatisMachrRegularVO;
import com.yb.statis.vo.StatisMachsingleVO;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import com.yb.supervise.vo.SuperviseExecuteVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchShiftsetVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequestMapping("/OeeSechedule")
@RestController
@AllArgsConstructor
public class StatisMachRegularController {
    private ISuperviseBoxinfoService iSuperviseBoxinfoService;
    //OEE需要导入的service
    private IExecuteStateService iExecuteStateService;
    private IExecuteWasteService iExecuteWasteService;
    private IMachineMainfoService iMachineMainfoService;
    private IBaseStaffinfoService iBaseStaffinfoService;
    private IWorkbatchOrdoeeService iWorkbatchOrdoeeService;
    private IWorkbatchOrdlinkService iWorkbatchOrdlinkService;
    private IStatisMachoeeService iStatisMachoeeService;
    private IStatisOeesetService iStatisOeesetService;
    private ISuperviseExecuteService iSuperviseExecuteService;
    private IStatisMachRegularService iStatisMachRegularService;
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;

    public long millisecondToMinute(Long millisecond) {
        return millisecond / (60 * 1000);
    }
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StatisMachRegularController.class);

    //每15 分钟一次
    //@Scheduled(cron = "0 0,15,30,45 * * * ?")
    public void task() {
        try {
            DBIdentifier.setProjectCode("000000");
            generateMachineReport();
        } catch (Exception e) {
            log.info("定时任务生成失败--------------------------------------");
        }
    }
    /**
     * 生成OEE定时任务
     */
    @PostMapping("/generateMachineReport")
    @ApiOperation(value = "生成OEEReport")
    public void generateOEEMachineReport() {
        try {
            generateMachineReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generateMachineReport() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // 放入Date类型数据
        int targetHour = calendar.get(Calendar.HOUR_OF_DAY); // 时（24小时制）
        int targetMin = calendar.get(Calendar.MINUTE); // 分
        if (targetMin>55 || targetMin<5){
            targetMin=0;
        }
        if (targetMin>40 && targetMin<50){
            targetMin=45;
        }
        if (targetMin>25 && targetMin<35){
            targetMin=30;
        }
        if (targetMin>10 && targetMin<20){
            targetMin=15;
        }
        //首先查询出来非停机的机器
        List<SuperviseBoxinfoVO> superviseBoxInfoVOList = iSuperviseBoxinfoService.getBoxListNotStop();
        for (SuperviseBoxinfoVO superviseBoxInfo : superviseBoxInfoVOList) {
            String mtId = superviseBoxInfo.getMaId().toString();
            SimpleDateFormat dateSimple = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            //开始时间为当天的0时0分0秒
            //这里只是初始化时间等会在改
            Date dates =  new Date();
            Date startDate = dateSimple.parse(dateSimple.format(dates));
            Date startTime = startDate;
            Date endTime = dates;
            //持续的时间
            //B 列 oee统计时间2020-04-15 ！
            String calculateTime = DateUtil.refNowDay();
            //A 列 设备名称  查询条件名称
            MachineMainfo machineMainfos = iMachineMainfoService.getBaseMapper().selectById(mtId);
            String machineName = null;
            if (machineMainfos!=null) {
                machineName = machineMainfos.getName();
            }
            //C 列 人员姓名!
            //通过uuID 查询人员的姓名
            SuperviseExecuteVO superviseExecuteVO = iSuperviseExecuteService.getBeanByUUID(superviseBoxInfo.getUuid());
            Map<String, Object> OpreatConditionMap = new HashMap<>();
            Integer userId = 0;
            Integer sdId = 0;
            if (superviseExecuteVO != null) {
                userId = superviseExecuteVO.getOperator();
                sdId = superviseExecuteVO.getSdId();
            }
            OpreatConditionMap.put("user_id", superviseExecuteVO.getOperator());
            List<BaseStaffinfo> baseStaffinfos = iBaseStaffinfoService.getBaseMapper().selectByMap(OpreatConditionMap);
            String OpreationPersonName = null;
            if (baseStaffinfos.get(0) != null && baseStaffinfos.size() != 0 && baseStaffinfos != null) {
                OpreationPersonName = baseStaffinfos.get(0).getName();
            }
            //D 列 班次名称！
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String DateStr = sdf.format(new Date());
            String sfName="";
            List<WorkbatchShiftsetVO> workbatchShiftinfoVOList = iWorkbatchShiftsetService.getWorkbatchShiftByUserIdAndCheckDate(DateStr, userId);
            if (workbatchShiftinfoVOList != null && workbatchShiftinfoVOList.size() > 0) {
                sfName = workbatchShiftinfoVOList.get(0).getCkName();
            }
            //E 列 设备保养 ！
            String maintain = "B2";
            //根据设备Id 和时间段查询设备保养的时间
            ExecuteStateVO executeState = iExecuteStateService.getExecuteVoListBy(startTime, endTime, maintain, Integer.parseInt(mtId));
            Integer restultMaintain = 0;
            Integer maintainTime = 0;
            if (executeState != null && executeState.getSumDuraction() != null && executeState.getCountExecuteNumber() != null) {
                restultMaintain = executeState.getSumDuraction()/60;
                maintainTime = executeState.getCountExecuteNumber();
            }
            //F列 换膜时间 ！
            String exchange = "B3";
            //根据设备Id 和时间段查询设备保养的时间
            ExecuteStateVO executeStat = iExecuteStateService.getExecuteVoListBy(startTime, endTime, exchange, Integer.parseInt(mtId));
            Integer resultExchange = 0;
            Integer moudleExchaneTime = 0;
            if (executeStat != null && executeStat.getSumDuraction() != null && executeState.getCountExecuteNumber() != null) {
                resultExchange = executeStat.getSumDuraction()/60;
                moudleExchaneTime = executeStat.getCountExecuteNumber();
            }
            //K 列印联盒采集数量!
            Integer sumCollectNumber = 0;
            //M 良品数量!
            Integer sumGoodAccept = 0;
            //N 废品数量!
            Integer sumWasteNumber = 0;
            List<ExecuteStateVO> executeStats = iExecuteStateService.getAcceptedGoodsByTimeAndMa(startTime, endTime, Integer.parseInt(mtId));
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
            List<ExecuteStateVO> failList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, Integer.parseInt(mtId), equipmentErr);
            if (failList.size() != 0 && failList != null && failList.get(0) != null) {
                equipmentErrTime = failList.get(0).getDuration();
            }
            //R 品质故障C2-M
            String equalityErr = "品质故障C2-M";
            Double equalityErrTime = 0.0;
            List<ExecuteStateVO> qualityErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, Integer.parseInt(mtId), equalityErr);
            if (qualityErrList.size() != 0 && qualityErrList != null && qualityErrList.get(0) != null) {
                equalityErrTime = qualityErrList.get(0).getDuration();
            }
            //S 计划停机C2-O
            String planErr = "计划停机C2-O";
            Double planErrTime = 0.0;
            List<ExecuteStateVO> planErrErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, Integer.parseInt(mtId), planErr);
            if (planErrErrList.size() != 0 && planErrErrList != null && planErrErrList.get(0) != null) {
                planErrTime = planErrErrList.get(0).getDuration();
            }
            //T 管理停止C2-P
            String manageErr = "管理停止C2-P";
            Double manageErrTime = 0.0;
            List<ExecuteStateVO> manageErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, Integer.parseInt(mtId), manageErr);
            if (manageErrList.size() != 0 && manageErrList != null && manageErrList.get(0) != null) {
                manageErrTime = manageErrList.get(0).getDuration();
            }
            //U 磨损更换C2-Q
            String wearErr = "磨损更换C2-Q";
            Double wearErrTime = 0.0;
            List<ExecuteStateVO> wearErrList = iExecuteStateService.getExecuteFailStatus(startTime, endTime, Integer.parseInt(mtId), wearErr);
            if (wearErrList.size() != 0 && wearErrList != null && wearErrList.get(0) != null) {
                wearErrTime = wearErrList.get(0).getDuration();
            }
            //V 休息吃饭C2-R
            String rest = "休息吃饭C2-R";
            Double restTime = 0.0;
            List<ExecuteStateVO> restList = iExecuteStateService.getExecuteFailclassify(startTime, endTime, Integer.parseInt(mtId), rest);
            if (restList.size() != 0 && restList != null && restList.get(0) != null) {
                restTime = restList.get(0).getDuration();
            }
            //W 停机等待总计时间！
            Double SumTime = equipmentErrTime + equalityErrTime + planErrTime + manageErrTime + wearErrTime + restTime;
            //X 标准出勤 实际准备时间！ 实际的保养时间和实际的换膜时间 生产准备时间L（分计算）实际时间
            Integer realPreTime = restultMaintain + resultExchange;
            Long miniSecond = endTime.getTime() - startTime.getTime();
            Long min = millisecondToMinute(miniSecond);
            //一个时间段可能会有的多个排产。
            List<WorkbatchOrdoee> resultList = new ArrayList<>();
            String eventId = "D1";//上班A1 下班A2 接单B1保养B2换模B3正式生产C1停机C2质检C3结束生产D1生产上报D2入库D3
            //这里是通过start的时间在班次之中，然后呢 用户id 然后呢 生产结束等标识，拿到了这个时间段的把排产计划
            List<String> ids = iExecuteStateService.getSdIdbyConditon(startTime, endTime, Integer.parseInt(mtId), userId.toString(), eventId);
            Integer planClassNumber = 0;
            //当班的计划生产总数
            if (ids.size() != 0) {
                for (String id : ids) {
                    if (id != null) {
                        WorkbatchOrdlink workbatchOrdlink = iWorkbatchOrdlinkService.getById(id);
                        if (workbatchOrdlink != null) {
                            planClassNumber = planClassNumber + workbatchOrdlink.getPlanNum() + workbatchOrdlink.getExtraNum();
                        }
                    }
                }
                for (String id : ids) {
                    Map<String, Object> condiMap = new HashMap<>();
                    condiMap.put("wk_id", id);
                    List<WorkbatchOrdoee> workbatchOrdlink = iWorkbatchOrdoeeService.getBaseMapper().selectByMap(condiMap);
                    if (workbatchOrdlink.size() != 0 && workbatchOrdlink != null && workbatchOrdlink.get(0) != null) {
                        resultList.add(workbatchOrdlink.get(0));
                    }
                }
            }
            //这里使用Distince
            //标准指标   保养总次数!
            Integer totalMaintainNum = 0;
            //标准指标   保养总时间!
            Integer totalMaintainStay = 0;
            //标准指标   换模总次数!
            Integer totalMouldNum = 0;
            //标准指标   换模总时长!
            Integer totalMouldStay = 0;
            //标准指标 休息吃饭！
            Integer totalEatAndRest = 0;
            Integer TotalRestNumber = 0;
            //标准指标   质量检测总次数!
            Integer totalQualityNumber = 0;
            //其他指标  损耗数总
            Integer wasterTotal = 0;
            Double difficultNumber = 0.0;
            //计划生产数量TaskNumber
            Integer TotalCompletNumber = 0;
            Integer TotalSpeedNumber = 0;
            //这里用来算机器的工艺名称和转速
            StringBuffer prName = new StringBuffer();
            //理论的浪费数量
            Integer SumWaterNumber = 0;
            if (resultList.size() != 0) {
                for (WorkbatchOrdoee ordoee : resultList) {
                    /*TotalRestNumber = TotalRestNumber + ordoee.getMealNum();
                    totalEatAndRest = totalEatAndRest +  ordoee.getMealStay();

                    totalMaintainNum = totalMaintainNum + ordoee.getMaintainNum();
                    totalMaintainStay = totalMaintainStay + ordoee.getMaintainStay();*/

                    totalMouldNum = totalMouldNum + ordoee.getMouldNum();
                    totalMouldStay = totalMouldStay + ordoee.getMouldStay();

                    //在这里获得每一个设备的标准时速度
                    totalQualityNumber = totalQualityNumber ;//+ ordoee.getQualityNum();
                    Integer speed = 0;
                    WorkbatchOrdlink workbatchOrdoee = iWorkbatchOrdlinkService.getById(ordoee.getWkId());
                    if (workbatchOrdoee != null) {
                        List<MachineMainfoVO> machineMainfoVOS = iMachineMainfoService.getRateByMachineId(Integer.parseInt(mtId), workbatchOrdoee.getPrName());
                        //通过工序名称和设备id 拿到设备的转速
                        if (machineMainfoVOS.size() != 0 && machineMainfoVOS != null && machineMainfoVOS.get(0) != null) {
                            speed = machineMainfoVOS.get(0).getSpeed();
                        }
                    }
                    Integer number = workbatchOrdoee.getCompleteNum();
                    TotalCompletNumber = number + TotalCompletNumber;
                    TotalSpeedNumber = speed * number + TotalSpeedNumber;
                    wasterTotal = wasterTotal + workbatchOrdoee.getExtraNum();
                    //平均难易程度
                    difficultNumber = difficultNumber + ordoee.getDifficultNum() * number;
                    prName.append(workbatchOrdoee.getPrName() + ":" + speed + "/ ");
                    //总消耗数量
                    SumWaterNumber = SumWaterNumber + workbatchOrdoee.getExtraNum();
                }
        }
        //标准指标   理论生产性能！
        Integer AcdemicProduceQuality = 0;
        AcdemicProduceQuality = TotalSpeedNumber / TotalCompletNumber;
        //其他指标  平均难易程度
        Double difficultLevel = difficultNumber / TotalCompletNumber;
        //然后eventId 为A1 来计算endTime
        Map<String, Object> conditionsMap = new HashMap<>();
        conditionsMap.put("event", "A1");
        conditionsMap.put("us_id", userId);
        List<ExecuteState> executeStatesList = iExecuteStateService.getBaseMapper().selectByMap(conditionsMap);
        Date EndTime = null;
        if (executeStatesList.size() != 0 && executeStatesList != null && executeStatesList.get(0) != null) {
            EndTime = executeStatesList.get(0).getEndAt();
        }
        //休息吃饭 前面已经算了
            //标准生产时间（标准出勤-吃饭、休息）【休息吃饭C2-R：休息R1、吃饭R2、开会R3】!!
            Double standProduceTime = min - restTime;
            //计划稼动时间（标准生产时间-标准保养、和换模时间）
            Double planActivationTime = standProduceTime- resultExchange - restultMaintain;
            //实际稼动时间实际稼动时间（标准生产时间-实际生产时间）
            //（计划稼动时间-故障停机-生产准备-设备故障）
            //减去时间：设备故障C2-N、品质故障C2-M、计划停机C2-O、管理停止C2-P
            Double RealActivationTime =planActivationTime- equipmentErrTime - equalityErrTime - planErrTime - manageErrTime;
            // 时间稼动率（实际时间/标准时间）
            BigDecimal TimeMonement = new BigDecimal(planActivationTime / RealActivationTime).setScale(2, BigDecimal.ROUND_HALF_UP);
            //良品率（良品数/盒子数）
            BigDecimal goodRates = new BigDecimal(((float) sumGoodAccept / sumCollectNumber)).setScale(2, BigDecimal.ROUND_HALF_UP);
            //实际生产性能
            Double faceSpeendDouble =(workNumber/RealActivationTime);
            Integer faceSPeed = faceSpeendDouble.intValue();
            //性能稼动率
            BigDecimal productionPerformance = new BigDecimal((float)(faceSPeed) / AcdemicProduceQuality).setScale(2, BigDecimal.ROUND_HALF_UP);
            //OEE（时间稼动率x良品率x性能稼动率）
            BigDecimal OEErate = (TimeMonement.multiply(goodRates).multiply(productionPerformance)).setScale(2,BigDecimal.ROUND_HALF_UP);
            //OEE数据信息表_yb_statis_machoee
            StatisMachoee statisMachoee = new StatisMachoee();
            statisMachoee.setOeDate(calculateTime);
            statisMachoee.setMaId(Integer.parseInt(mtId));
            statisMachoee.setMaName(machineName);
            statisMachoee.setUsId(userId);
            statisMachoee.setUsName(OpreationPersonName);
            statisMachoee.setSfName(sfName);
            statisMachoee.setOeType(2);
            iStatisMachoeeService.saveOrUpdate(statisMachoee);
            // 设备OEE 数据表yb_statis_machregular
            StatisMachregular statisMachRegular = new StatisMachregular();
            statisMachRegular.setSmId(statisMachoee.getId());
            statisMachRegular.setStartTime(startTime);
            statisMachRegular.setEndTime(endTime);
            //保养
            statisMachRegular.setMaintainNum(maintainTime);
            statisMachRegular.setMaintainStay(restultMaintain);
            //换膜
            statisMachRegular.setMouldNum(moudleExchaneTime);
            statisMachRegular.setMouldStay(resultExchange);
            // 日 时 分
//            statisMachRegular.setTargetHour(targetHour);
//            statisMachRegular.setTargetMin(targetMin);

            statisMachRegular.setPrepareStay(realPreTime);
            statisMachRegular.setStandardRuntime(standProduceTime.intValue());
            statisMachRegular.setPlanutilizeStay(planActivationTime.intValue());
            statisMachRegular.setFactutilizeStay(RealActivationTime.intValue());
            statisMachRegular.setNodefectCount(sumGoodAccept);
            statisMachRegular.setWatesCount(sumWasteNumber);
            statisMachRegular.setTaskCount(planClassNumber);
            statisMachRegular.setWorkCount(workNumber);
            statisMachRegular.setBoxNum(sumCollectNumber);
            statisMachRegular.setQualityNum(qualityTime);
            //实际生产性能
            statisMachRegular.setFactSpeed(faceSPeed);
            statisMachRegular.setFaultStay(equipmentErrTime.intValue());
            statisMachRegular.setQualityStay(equalityErrTime.intValue());
            statisMachRegular.setPlanStay(planErrTime.intValue());
            statisMachRegular.setManageStay(manageErrTime.intValue());
            statisMachRegular.setAbrasionStay(wearErrTime.intValue());
            statisMachRegular.setRestStay(restTime.intValue());
            statisMachRegular.setStayTotal(SumTime.intValue());
            statisMachRegular.setUtilizeRate(TimeMonement);
            statisMachRegular.setYieldRate(goodRates);
            statisMachRegular.setPerformRate(productionPerformance);
            statisMachRegular.setGatherRate(OEErate);
            iStatisMachRegularService.saveOrUpdate(statisMachRegular);

            //统计分析设置表_yb_statis_oeeset
            StatisOeeset statisOeeset = new StatisOeeset();
            statisOeeset.setStType(1);
            statisOeeset.setDbId(statisMachoee.getId());
            statisOeeset.setSfStarttime(startTime);
            statisOeeset.setSfEndtime(endTime);
            statisOeeset.setSfStaytime(min.intValue());
            statisOeeset.setResetNum(TotalRestNumber);
            statisOeeset.setResetTime(totalEatAndRest);
            statisOeeset.setMaintainNum(totalMaintainNum);
            statisOeeset.setMaintainTime(totalMaintainStay);
            statisOeeset.setMouldNum(totalMaintainNum);
            statisOeeset.setMouldTime(totalMaintainStay);
            statisOeeset.setMouldTime(totalMouldNum);
            statisOeeset.setMouldTime(totalMouldStay);
            statisOeeset.setQualityNum(qualityTime);
            statisOeeset.setNormalSpeed(AcdemicProduceQuality);
            statisOeeset.setShiftsTotalnum(planClassNumber);
            statisOeeset.setPrSpeed(prName.toString());
            statisOeeset.setWasteNum(SumWaterNumber);
            statisOeeset.setDiffNum(new BigDecimal(difficultLevel));
            statisOeeset.setPrepareStay(realPreTime.intValue());
            iStatisOeesetService.saveOrUpdate(statisOeeset);
        }
    }

    /**
     * PageOfMachsingle
     */
    @GetMapping("/PageOfMachRegular")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入PageOfMachRegular")
    public R<IPage<StatisMachsingleVO>> PageOfMachRegular(StatisMachrRegularVO statisMachrRegularVO, Query query) {
        IPage<StatisMachsingleVO> pages = iStatisMachRegularService.selectStatisMachRegularPage(Condition.getPage(query), statisMachrRegularVO);
        return R.data(pages);
    }
}
