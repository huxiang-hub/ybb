package com.yb.statis.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.SuperviseRegular;
import com.yb.execute.service.*;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.entity.StatisMachregular;
import com.yb.statis.entity.StatisMachsingle;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.service.IStatisMachRegularService;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.service.IStatisMachsingleService;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 精益生产的OEE
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisLeanOee")
@Api(value = "", tags = "生成oee接口")
public class StatisLeanOeeController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StatisMachRegularController.class);


    @Autowired
    private IExecuteStateService iExecuteStateService;
    @Autowired
    private IExecuteWasteService iExecuteWasteService;
    @Autowired
    private IMachineMainfoService iMachineMainfoService;
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
    private ISuperviseIntervalService superviseIntervalService;
    @Autowired
    private IExecuteInfoService iExecuteInfoService;
    @Autowired
    private IProcessMachlinkService iProcessMachlinkService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;
    @Autowired
    private SuperviseRegularService superviseRegularService;
    @Autowired
    private IExecuteBrieferService executeBrieferService;
    @Autowired
    private IBaseStaffinfoService baseStaffinfoService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private IStatisMachRegularService statisMachRegularService;


    /*-------------------------------------------------*/


    /**
     * 获取设备id,当前班次id,当前日期
     */
    public void  getMaIdWsIdTargetDay() {
        List<String> stringList = new ArrayList<>();
        stringList.add("xingyi");
        stringList.add("nxhr");
        for(String s : stringList){
            DBIdentifier.setProjectCode(s);
            getMaIdWsIdTargetDay("");
            getMaIdTargetDay("");
        }

    }


//    /**
//     * 自动生成河北何瑞oee数据
//     */
//    @Scheduled(cron = "0 2 8,20 * * ?")
//    public void  nxhrOeeSent() {
//        DBIdentifier.setProjectCode("nxhr");
//        getMaIdWsIdTargetDay("");
//        getMaIdTargetDay("");
//    }
    @GetMapping("OeeSent")
    @ApiOperation(value = "重新生成oee")
    public void  OeeSent(String projectCode, String targetDay) {
        DBIdentifier.setProjectCode(projectCode);
        getMaIdWsIdTargetDay(targetDay);
        getMaIdTargetDay(targetDay);
    }

    public void getMaIdWsIdTargetDay (String targetDay){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String wsFormat = sdf.format(new Date(date.getTime() - 1000 * 60 * 30));//减半小时
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        Integer h = Integer.valueOf(simpleDateFormat.format(date));
        if(targetDay.isEmpty()){
            if(h < 12){//如果小时数小于12则认为是白班,新增昨天数据
                Date toDay = DateUtil.addDayForDate(date, -1);
                targetDay = DateUtil.refNowDay(toDay);
            }else {//否则是晚上八点,新增今天oee
                targetDay = DateUtil.refNowDay();
            }
        }else {//如果传入日期,则使用传入日期
            if(h < 12){//如果小时数小于12则认为是白班,新增昨天数据
                Date toDay = DateUtil.addDayForDate(DateUtil.changeDay(targetDay), -1);
                targetDay = DateUtil.refNowDay(toDay);
            }
        }
        System.err.println("targetDay:"+targetDay);
//        List<Integer> maIdList = superviseIntervalService.getMaIdsByTargetDay(targetDay);
        List<Integer> maIdList = iWorkbatchOrdlinkService.getBySdDate(targetDay, null);
        Set<Integer> wsIdSet = new HashSet<>();
        List<Integer> ordlinkMaIdList;
        if(!maIdList.isEmpty()){
            for(Integer maId : maIdList){
                WorkbatchMachShiftVO workbatchMachShiftVO = workbatchShiftMapper.findByMaIdWsTime(maId, wsFormat);
                if(workbatchMachShiftVO != null){
                    wsIdSet.add(workbatchMachShiftVO.getId());
                }
            }
        }
        /*获取当前班次的所有排产信息*/
        if(!wsIdSet.isEmpty()){
            for(Integer wsId : wsIdSet){
                log.info("-------------------------wsId: " + wsId);
                ordlinkMaIdList = iWorkbatchOrdlinkService.getBySdDate(targetDay, wsId);
                for(Integer maId1 : ordlinkMaIdList){
                    log.info("-------------------------maId1:" + maId1);
                    WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(wsId, maId1);
                    setMachoee(workbatchShiftset,maId1, targetDay, wsId);
                }
            }
        }
    }
    /**
     * 根据执行单id集合查询上报表的作业数,良品数,废品数
     * @param exIds
     * @return
     */
    @RequestMapping("/getNumberByExId")
    public  List<ExecuteBriefer> getNumberByExId(@RequestBody List<Integer> exIds){
        List<ExecuteBriefer> executeBrieferList = executeBrieferService.getNumberByExId(exIds);
        return executeBrieferList;
    }

    /**
     * 班次oee
     * @param maId
     * @param targetDay
     * @param wsId
     */
    @Transactional(rollbackFor = Exception.class)
    public void setMachoee(WorkbatchShiftset workbatchShiftset,Integer maId,String targetDay,Integer wsId){

        /*---------------------------*/
        Integer abrasionStay = 0;//磨损更换C2-Q
        Integer maintainNum = 0;//设备保养次数
        Integer maintainStay = 0;//设备保养B2（分计算）
        Integer manageStay = 0;//管理停止C2-P
        Integer planStay = 0;//计划停机C2-O
        Integer qualityStay = 0;//品质故障C2-M
        Integer taskCount;//加工数-任务数（计划订单要求总数+损耗数）
        Integer workStay;//出勤时间A -间隔停留时间（分）真实出勤时间
        Integer faultStay = 0;//设备故障C2-N（生产设备故障，公共设备故障）
        Integer prepareStay;//生产准备时间L（分计算）实际时间
        /*-------------------------*/
        Integer mouldStay = 0;//换膜时间
        Integer mouldNum = 0;//换膜次数
        Integer restStay;//吃饭时长
        Integer stayTotal;//停机时长(分)
        Integer nodefectCount = 0;//良品数
        Integer watesCount = 0;//废品数
        Integer workCount = 0;//作业数
        Integer factutilizeStay;//实际稼动时间(分)
        BigDecimal utilizeRate;//时间稼动率
        BigDecimal yieldRate;//性能稼动率
        BigDecimal performRate;//良品率
        Double oeeRate;//OEE综合效率
        Integer sdId = null;//得到最后一个排产id
        Integer exId = null;//得到最后一个排产id
        Integer qualityNum = 0;//质检次数
        /*查询班次信息*/
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getById(wsId);

        Date startTime = workbatchShiftset.getStartTime();//开始时间
        Date endTime = workbatchShiftset.getEndTime();//结束时间
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, targetDay);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        String ckName = workbatchShiftset.getCkName();//班次名称
        restStay = workbatchShiftset.getMealStay();
        /*查询执行单信息,获取sdId*/
        List<ExecuteInfo> executeInfoList =
                iExecuteInfoService.getBaseMapper().selectList(new QueryWrapper<ExecuteInfo>()
                        .eq("ma_id", maId).eq("ws_id", wsId).eq("target_day", targetDay));
        List<Integer> exIds = new ArrayList<>();
        for(ExecuteInfo executeInfo : executeInfoList){
            exId = executeInfo.getId();
            sdId = executeInfo.getSdId();
            Integer wfId = executeInfo.getWfId();
//            WorkbatchOrdoee workbatchOrdoee =
//                    iWorkbatchOrdoeeService.getOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", sdId));
//            Integer ldStay = workbatchOrdoee.getMouldStay() == null ? 0 : workbatchOrdoee.getMouldStay();
//            Integer ityNum = workbatchOrdoee.getQualityNum();
            WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
            if(workbatchShift != null){
                Integer ldStay = workbatchShift.getMouldStay() == null ? 0 : workbatchShift.getMouldStay();
                mouldStay += ldStay;//换膜总时长
                mouldNum++;
            }
//            qualityNum += ityNum;//质检次数
            exIds.add(executeInfo.getId());
        }
        prepareStay = mouldStay;//先用换膜时间

        /*获取上报表良品,废品,作业数*/
        List<ExecuteBriefer> executeBrieferList = new ArrayList<>();
        if(!exIds.isEmpty()) {
            executeBrieferList = getNumberByExId(exIds);
        }
        Integer usId = null;
        String usName = null;
        if(!executeBrieferList.isEmpty()){
            for(ExecuteBriefer executeBriefer : executeBrieferList){
                usId = executeBriefer.getHandleUsid();//处理上报人员
                Integer productNum = executeBriefer.getProductNum();//作业数
                Integer countNum = executeBriefer.getCountNum();//良品数
                Integer wasteNum = executeBriefer.getWasteNum();//废品数
                if(productNum == null){
                    productNum = 0;
                }
                if(countNum == null){
                    countNum = 0;
                }
                if(wasteNum == null){
                    wasteNum = 0;
                }
                workCount += productNum;
                nodefectCount += countNum;
                watesCount += wasteNum;
            }
        }
        taskCount = watesCount + nodefectCount;
        if(usId != null){
            BaseStaffinfo baseStaffinfo = baseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("user_id", usId));
            if(baseStaffinfo != null){
                usName = baseStaffinfo.getName();
            }
        }
        /*查询设备信息*/
        MachineMainfo machineMainfo = iMachineMainfoService.getById(maId);
        String maName = machineMainfo.getName();//设备名称
        Integer prId = machineMainfo.getProId();
        ProcessMachlink processMachlink = null;
        if(prId != null){
            processMachlink = iProcessMachlinkService.getOne(new QueryWrapper<ProcessMachlink>().eq("pr_id", prId).eq("ma_id", maId));
        }else {
            log.info("-----------------------设备没有主工序------------------------");
        }
        Integer speed = null;
        if(processMachlink != null){
            speed = processMachlink.getSpeed();//理论速度
        }else {
            log.info("------------------------主工序绑定设备------------------------");
        }
        Integer ckTime = Math.abs((int)(endTime.getTime() - startTime.getTime())/1000/60);//班次时间(分)
        workStay = ckTime;
        Integer standardRuntime = ckTime - restStay;//标准生产时间
        Integer planutilizeStay = standardRuntime;//计划稼动时间
        /*查询设备停机时长*/
        Integer sumTimeBy = superviseIntervalService.getSumTimeByMaId(2, maId, classStartTime, classEndTime);//设备停机时间(秒)
        stayTotal = sumTimeBy / 60;//(分)
        /*查询运行时间*/
        List<SuperviseRegular> superviseRegularList = superviseRegularService.getBaseMapper().selectList(new QueryWrapper<SuperviseRegular>()
                .eq("status", 1).eq("ma_id", maId).le("start_time", classEndTime).ge("start_time", classStartTime));
        Integer boxNum = 0;
        Integer timeByMaId = 0;
        for(SuperviseRegular superviseRegular : superviseRegularList){
            Integer diffNum = superviseRegular.getDiffNum();//间隔时间（秒）
            diffNum = diffNum == null ? 0 : diffNum;
            Integer pcout = superviseRegular.getPcout();//间隔计数
            pcout = pcout == null ? 0 : pcout;
            boxNum += pcout;//盒子计数
            timeByMaId += diffNum;//设备运行时长(秒)
        }
        // Integer boxNum = superviseIntervalService.SumBoxNumber(maId, classStartTime, classEndTime);//盒子计数
        /*查询设备运行时长(秒)*/
        //Integer timeByMaId = superviseIntervalService.getSumTimeByMaId(1, maId, classStartTime, classEndTime);
        timeByMaId = timeByMaId == null ? 0 : timeByMaId;
        factutilizeStay = timeByMaId / 60;//实际稼动时间(转化为分)
        Integer factSpeed = 0;
        sumTimeBy = sumTimeBy == null ? 0 : sumTimeBy;
        if(factutilizeStay == 0){
            factSpeed = 0;
        }else {
            if(boxNum != null && boxNum != 0){
                factSpeed = boxNum/(factutilizeStay + (sumTimeBy/60));//实际速度(/分钟)
            }
        }
        factSpeed= factSpeed*60;//实际速度(/小时)
        double utilize = factutilizeStay / (double) planutilizeStay;
        utilizeRate = new BigDecimal(utilize * 100); //时间稼动率(实际稼动时间/计划稼动时间)

        double perform;
        if(speed != null && speed != 0){
            perform = (factSpeed / (double)speed);
        }else {
            perform = 1;//如果理论速度为空或0,则性能稼动率默认为100%
        }
        performRate = new BigDecimal(perform * 100);

        double yield;
        if(workCount != null && workCount != 0){
            yield = (nodefectCount / (double)workCount);//良品率
        }else {
            yield = 1.0;//如果未上报,作业数,良品数均为空,则良品率默认为100%
        }
        yieldRate = new BigDecimal(yield * 100);//性能稼动率(实际速度/理论速度)

        oeeRate = utilize * yield * perform;//OEE综合效率
        BigDecimal gatherRate = new BigDecimal(oeeRate * 100);//OEE综合效率
        /*-------------------OEE数据信息表--------------------*/

        StatisMachoee statisMachoee =
                iStatisMachoeeService.getOne(new QueryWrapper<StatisMachoee>()
                        .eq("oe_type", 1).eq("oe_date", targetDay).eq("ws_id", wsId).eq("ma_id", maId));
        if(statisMachoee == null){
            statisMachoee = new StatisMachoee();//OEE数据信息表
        }
        statisMachoee.setMaId(maId);//设备id
        statisMachoee.setMaName(maName);//设备名称
        statisMachoee.setCreateAt(new Date());
        statisMachoee.setExId(exId);//执行单id(现在取的最后一个执行单id)
        statisMachoee.setOeDate(targetDay);//oee统计时间
        statisMachoee.setOeType(1);//1、设备排产单 2、设备定时任务
        statisMachoee.setSdId(sdId);//最后一个排产单id
        statisMachoee.setSfName(ckName);//班次名称
        statisMachoee.setWsId(wsId);//班次id
        statisMachoee.setSfStartTime(classStartTime);//开始时间
        statisMachoee.setSfEndTime(classEndTime);//结束时间
        statisMachoee.setUsId(usId);
        statisMachoee.setUsName(usName);
        iStatisMachoeeService.saveOrUpdate(statisMachoee);

        /*----------------班次设备OEE数据表--------------------*/

        StatisMachsingle statisMachsingle = iStatisMachsingleService.getOne(new QueryWrapper<StatisMachsingle>().eq("sm_id", statisMachoee.getId()));
        if(statisMachsingle == null){
            statisMachsingle = new StatisMachsingle();
        }
        statisMachsingle.setBoxNum(boxNum);//盒子计数
        statisMachsingle.setFactSpeed(factSpeed);//实际能力生产性H*每小时生产速度取整（班次总数和总时长）
        statisMachsingle.setFactutilizeStay(factutilizeStay);//实际稼动时间
        statisMachsingle.setFaultStay(faultStay);//设备故障C2-N（生产设备故障,公共设备故障）
        statisMachsingle.setGatherRate(gatherRate);//OEE综合效率
        statisMachsingle.setNodefectCount(nodefectCount);//良品数
        statisMachsingle.setPerformRate(performRate);//良品率
        statisMachsingle.setPlanutilizeStay(planutilizeStay);//计划稼动时间C
        statisMachsingle.setPrepareStay(prepareStay);//生产准备时间L（分计算）实际时间
        statisMachsingle.setQualityNum(qualityNum);//质检次数
        statisMachsingle.setSmId(statisMachoee.getId());//设备统计唯一主键id（yb_statis_machoee）
        statisMachsingle.setStandardRuntime(standardRuntime);//标准生产时间分钟（标准出勤-休息开会吃饭）
        statisMachsingle.setStayTotal(stayTotal);//停机合计分钟
        statisMachsingle.setUtilizeRate(utilizeRate);//时间稼动率
        statisMachsingle.setWatesCount(watesCount);//废品数
        statisMachsingle.setWorkCount(workCount);//作业数F（实际作业总数）
        statisMachsingle.setYieldRate(yieldRate);//良品率G
        statisMachsingle.setNormalSpeed(speed);//理论速度
        statisMachsingle.setAbrasionStay(abrasionStay);//磨损更换C2-Q
        statisMachsingle.setEndTime(classEndTime);//结束时间
        statisMachsingle.setStartTime(classStartTime);//开始时间
        statisMachsingle.setJsjPlanutilizeStay(0);//金世纪计划稼动时间
        statisMachsingle.setMaintainNum(maintainNum);//设备保养次数
        statisMachsingle.setMaintainStay(maintainStay);//设备保养B2（分计算）
        statisMachsingle.setManageStay(manageStay);//管理停止C2-P
        statisMachsingle.setMouldNum(mouldNum);//换膜次数
        statisMachsingle.setMouldStay(mouldStay);//换膜时长
        statisMachsingle.setPlanStay(planStay);//计划停机C2-O
        statisMachsingle.setQualityStay(qualityStay);//品质故障C2-M
        statisMachsingle.setRestStay(restStay);//休息吃饭C2-R
        statisMachsingle.setTaskCount(taskCount);//加工数-任务数（计划订单要求总数+损耗数）
        statisMachsingle.setWorkStay(workStay);//出勤时间A -间隔停留时间（分）真实出勤时间
        statisMachsingle.setCreateAt(new Date());
        statisMachsingle.setOeId(null);//
        iStatisMachsingleService.saveOrUpdate(statisMachsingle);
    }


//    public void getMaIdTargetDay(){
//        getMaIdTargetDay("");
//    }
//    public void  getMaIdTargetDay(String targetDay){
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        String wsFormat = sdf.format(new Date(date.getTime() - 1000 * 60 * 60));//减1小时
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
//        Integer h = Integer.valueOf(simpleDateFormat.format(date));
//        if(targetDay.isEmpty()){
//            if(h < 12){//如果小时数小于12则认为是白班,新增昨天数据
//                Date toDay = DateUtil.addDayForDate(date, -1);
//                targetDay = DateUtil.refNowDay(toDay);
//            }else {//否则是晚上八点,新增今天oee
//                targetDay = DateUtil.refNowDay();
//            }
//        }else {//如果传入日期,则使用传入日期
//            if(h < 12){//如果小时数小于12则认为是白班,新增昨天数据
//                Date toDay = DateUtil.addDayForDate(DateUtil.changeDay(targetDay), -1);
//                targetDay = DateUtil.refNowDay(toDay);
//            }
//        }
//
//        System.err.println("targetDay:" + targetDay);
//        List<Integer> maIdList = superviseIntervalService.getMaIdsByTargetDay(targetDay);
//        if(!maIdList.isEmpty()){
//            for(Integer maId : maIdList){
//                WorkbatchMachShiftVO workbatchMachShiftVO = workbatchShiftMapper.findByMaIdWsTime(maId, wsFormat);
//                if(workbatchMachShiftVO != null){
//                    setDayOee(maId, targetDay, workbatchMachShiftVO.getId());
//                }
//            }
//        }
//
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void setDayOee(Integer maId,String targetDay,Integer wsId){
//        /*---------------------------*/
//        Integer abrasionStay = 0;//磨损更换C2-Q
//        Integer maintainNum = 0;//设备保养次数
//        Integer maintainStay = 0;//设备保养B2（分计算）
//        Integer manageStay = 0;//管理停止C2-P
//        Integer planStay = 0;//计划停机C2-O
//        Integer qualityStay = 0;//品质故障C2-M
//        Integer taskCount = 0;//加工数-任务数（计划订单要求总数+损耗数）
//        Integer workStay;//出勤时间A -间隔停留时间（分）真实出勤时间
//        Integer faultStay = 0;//设备故障C2-N（生产设备故障，公共设备故障）
//        Integer prepareStay = 0;//生产准备时间L（分计算）实际时间
//        /*-------------------------*/
//        Integer mouldStay = 0;//换膜时间
//        Integer mouldNum = 0;//换膜次数
//        Integer restStay;//吃饭时长
//        Integer stayTotal;//停机时长(分)
//        Integer nodefectCount = null;//良品数
//        Integer watesCount = 0;//废品数
//        Integer workCount = 0;//作业数
//        Integer factutilizeStay;//实际稼动时间(分)
//        BigDecimal utilizeRate;//时间稼动率
//        BigDecimal yieldRate;//性能稼动率
//        BigDecimal performRate;//良品率
//        Double oeeRate;//OEE综合效率
//        Integer sdId = null;//得到最后一个排产id
//        Integer exId = null;//得到最后一个排产id
//        Integer qualityNum = 0;//质检次数
//        /*查询班次信息*/
////        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getById(wsId);
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(wsId, maId);
//
//        Date startTime = workbatchShiftset.getStartTime();//开始时间
//        Date endTime = workbatchShiftset.getEndTime();//结束时间
//        Date classStartTime = workbatchShiftset.getStartTime();
//        Date classEndTime = workbatchShiftset.getEndTime();
//        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, targetDay);//判断大小后赋值给开始时间和结束时间
//        classStartTime = wsDatetime[0];
//        classEndTime = wsDatetime[1];
//        String ckName = workbatchShiftset.getCkName();//班次名称
//        restStay = workbatchShiftset.getMealStay();
//
//
//        /*查询设备信息*/
//        MachineMainfo machineMainfo = iMachineMainfoService.getById(maId);
//        String maName = machineMainfo.getName();//设备名称
//        Integer prId = machineMainfo.getProId();
//        ProcessMachlink processMachlink = null;
//        if(prId != null){
//            processMachlink = iProcessMachlinkService.getOne(new QueryWrapper<ProcessMachlink>().eq("pr_id", prId).eq("ma_id", maId));
//        }else {
//            log.error("----------------设备没有主工序--------------");
//        }
//        Integer speed = null;
//        if(processMachlink != null){
//            speed = processMachlink.getSpeed();//理论速度
//        }else {
//            log.error("----------------主工序绑定设备----------------");
//        }
//        Integer ckTime = Math.abs((int)(endTime.getTime() - startTime.getTime())/1000/60);//班次时间(分)
//        workStay = ckTime;
//        Integer standardRuntime = ckTime - restStay;//标准生产时间
//        Integer planutilizeStay = standardRuntime;//计划稼动时间
//        /*查询设备停机时长*/
//        Integer sumTimeBy = superviseIntervalService.getSumTimeByMaId(2, maId, classStartTime, classEndTime);//设备停机时间(秒)
//        stayTotal = sumTimeBy / 60;//(分)
//        /*查询运行时间*/
//        List<SuperviseRegular> superviseRegularList = superviseRegularService.getBaseMapper().selectList(new QueryWrapper<SuperviseRegular>()
//                .eq("status", 1).eq("ma_id", maId).le("start_time", classEndTime).ge("start_time", classStartTime));
//        Integer boxNum = 0;
//        Integer timeByMaId = 0;
//        for(SuperviseRegular superviseRegular : superviseRegularList){
//            Integer diffNum = superviseRegular.getDiffNum();//间隔时间（秒）
//            diffNum = diffNum == null ? 0 : diffNum;
//            Integer pcout = superviseRegular.getPcout();//间隔计数
//            pcout = pcout == null ? 0 : pcout;
//            boxNum += pcout;//盒子计数
//            timeByMaId += diffNum;//设备运行时长(秒)
//        }
//        // Integer boxNum = superviseIntervalService.SumBoxNumber(maId, classStartTime, classEndTime);//盒子计数
//        /*查询设备运行时长(秒)*/
//        timeByMaId = superviseIntervalService.getSumTimeByMaId(1, maId, classStartTime, classEndTime);
//        timeByMaId = timeByMaId == null ? 0 : timeByMaId;
//        factutilizeStay = timeByMaId / 60;//实际稼动时间(转化为分)
//        Integer factSpeed = 0;
//        sumTimeBy = sumTimeBy == null ? 0 : sumTimeBy;
//        if(factutilizeStay == 0){
//            factSpeed = 0;
//        }else {
//            if(boxNum != null && boxNum != 0){
//                factSpeed = boxNum/(factutilizeStay + (sumTimeBy/60));//实际速度(/分钟)
//            }
//        }
//        factSpeed= factSpeed*60;//实际速度(/小时)
//        double utilize = factutilizeStay / (double) planutilizeStay;
//        utilizeRate = new BigDecimal(utilize * 100); //时间稼动率(实际稼动时间/计划稼动时间)
//
//        double perform;
//        if(speed != null && speed != 0){
//            perform = (factSpeed / (double)speed);
//        }else {
//            perform = 1;//如果理论速度为空或0,则性能稼动率默认为100%
//        }
//        performRate = new BigDecimal(perform * 100);
//
//        double yield;
//        if(workCount != null && workCount != 0){
//            yield = (nodefectCount / (double)workCount);//良品率
//        }else {
//            yield = 1.0;//如果未上报,作业数,良品数均为空,则良品率默认为100%
//        }
//        yieldRate = new BigDecimal(yield * 100);//性能稼动率(实际速度/理论速度)
//
//        oeeRate = utilize * yield * perform;//OEE综合效率
//        BigDecimal gatherRate = new BigDecimal(oeeRate * 100);//OEE综合效率
//        /*-------------------OEE数据信息表--------------------*/
//
//        StatisMachoee statisMachoee =
//                iStatisMachoeeService.getOne(new QueryWrapper<StatisMachoee>()
//                        .eq("oe_type", 2).eq("oe_date", targetDay).eq("ws_id", wsId).eq("ma_id", maId));
//        if(statisMachoee == null){
//            statisMachoee = new StatisMachoee();//OEE数据信息表
//        }
//        statisMachoee.setMaId(maId);//设备id
//        statisMachoee.setMaName(maName);//设备名称
//        statisMachoee.setCreateAt(new Date());
//        statisMachoee.setExId(exId);//执行单id(现在取的最后一个执行单id)
//        statisMachoee.setOeDate(targetDay);//oee统计时间
//        statisMachoee.setOeType(2);//1、设备排产单 2、设备定时任务
//        statisMachoee.setSdId(sdId);//最后一个排产单id
//        statisMachoee.setSfName(ckName);//班次名称
//        statisMachoee.setWsId(wsId);//班次id
//        statisMachoee.setSfStartTime(classStartTime);//开始时间
//        statisMachoee.setSfEndTime(classEndTime);//结束时间
//        statisMachoee.setUsId(null);
//        statisMachoee.setUsName(null);
//        iStatisMachoeeService.saveOrUpdate(statisMachoee);
//
//        /*----------------班次设备OEE数据表--------------------*/
//
//        StatisMachregular statisMachregular = statisMachRegularService.getOne(new QueryWrapper<StatisMachregular>()
//                .eq("sm_id", statisMachoee.getId()));
//        if(statisMachregular == null){
//            statisMachregular = new StatisMachregular();
//        }
//        statisMachregular.setBoxNum(boxNum);//盒子计数
//        statisMachregular.setFactSpeed(factSpeed);//实际能力生产性H*每小时生产速度取整（班次总数和总时长）
//        statisMachregular.setFactutilizeStay(factutilizeStay);//实际稼动时间
//        statisMachregular.setFaultStay(faultStay);//设备故障C2-N（生产设备故障,公共设备故障）
//        statisMachregular.setGatherRate(gatherRate);//OEE综合效率
//        statisMachregular.setNodefectCount(nodefectCount);//良品数-无缺陷
//        statisMachregular.setPerformRate(performRate);//良品率
//        statisMachregular.setPlanutilizeStay(planutilizeStay);//计划稼动时间C
//        statisMachregular.setPrepareStay(prepareStay);//生产准备时间L（分计算）实际时间
//        statisMachregular.setQualityNum(qualityNum);//质检次数
//        statisMachregular.setSmId(statisMachoee.getId());//设备统计唯一主键id（yb_statis_machoee）
//        statisMachregular.setStandardRuntime(standardRuntime);//标准生产时间分钟（标准出勤-休息开会吃饭）
//        statisMachregular.setStayTotal(stayTotal);//停机合计分钟
//        statisMachregular.setUtilizeRate(utilizeRate);//时间稼动率
//        statisMachregular.setWatesCount(watesCount);//废品数
//        statisMachregular.setWorkCount(workCount);//作业数F（实际作业总数）
//        statisMachregular.setYieldRate(yieldRate);//良品率G
//        statisMachregular.setNormalSpeed(speed);//理论速度
//        statisMachregular.setAbrasionStay(abrasionStay);//磨损更换C2-Q
//        statisMachregular.setMaintainNum(maintainNum);//设备保养次数
//        statisMachregular.setMaintainStay(maintainStay);//设备保养B2（分计算）
//        statisMachregular.setManageStay(manageStay);//管理停止C2-P
//        statisMachregular.setMouldNum(mouldNum);//换膜次数
//        statisMachregular.setMouldStay(mouldStay);//换膜时长
//        statisMachregular.setPlanStay(planStay);//计划停机C2-O
//        statisMachregular.setQualityStay(qualityStay);//品质故障C2-M
//        statisMachregular.setRestStay(restStay);//休息吃饭C2-R
//        statisMachregular.setTaskCount(taskCount);//加工数-任务数（计划订单要求总数+损耗数）
//        statisMachregular.setWorkStay(workStay);//出勤时间A -间隔停留时间（分）真实出勤时间
//        statisMachregular.setCreateAt(new Date());
//        statisMachRegularService.saveOrUpdate(statisMachregular);
//    }

    /**
     * 设备oee
     * @param targetDay
     */
    public void  getMaIdTargetDay(String targetDay){
        Date date = new Date();
        if(StringUtil.isEmpty(targetDay)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            Integer h = Integer.valueOf(simpleDateFormat.format(date));
            if(h <= 12){
                Date toDay = DateUtil.addDayForDate(date, -1);
                targetDay = DateUtil.refNowDay(toDay);
            }else {
                targetDay = DateUtil.refNowDay();
            }
        }
        List<Integer> maIdList = superviseIntervalService.getMaIdsByTargetDay(targetDay);
        for(Integer maId : maIdList){
            setDayOee(maId, targetDay);
        }
    }


    /**
     * 新增二十四小时oee
     * @param maId
     * @param targetDay
     */
    @RequestMapping("/setDayOee")
    public void setDayOee(Integer maId,String targetDay){

        /*---------------------------*/
        Integer abrasionStay = 0;//磨损更换C2-Q
        Integer maintainNum = 0;//设备保养次数
        Integer maintainStay = 0;//设备保养B2（分计算）
        Integer manageStay = 0;//管理停止C2-P
        Integer planStay = 0;//计划停机C2-O
        Integer qualityStay = 0;//品质故障C2-M
        Integer taskCount = 0;//加工数-任务数（计划订单要求总数+损耗数）
        Integer workStay = 0;//出勤时间A -间隔停留时间（分）真实出勤时间
        Integer faultStay = 0;//设备故障C2-N（生产设备故障，公共设备故障）
        Integer prepareStay;//生产准备时间L（分计算）实际时间
        /*-------------------------*/
        Integer mouldStay = 0;//换膜时间
        Integer mouldNum = 0;//换膜次数
        Integer restStay = 0;//吃饭时长
        Integer stayTotal;//停机时长(分)
        Integer nodefectCount = 0;//良品数
        Integer watesCount = 0;//废品数
        Integer workCount = 0;//作业数
        Integer factutilizeStay;//实际稼动时间(分)
        BigDecimal utilizeRate;//时间稼动率
        BigDecimal yieldRate;//性能稼动率
        BigDecimal performRate;//良品率
        Double oeeRate;//OEE综合效率
        Integer sdId = null;//得到最后一个排产id
        Integer exId = null;//得到最后一个排产id
        Integer qualityNum = 0;//质检次数
        /*查询班次信息*/
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getById(wsId);
//        Date startTime = workbatchShiftset.getStartTime();//开始时间
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startTime = cal.getTime();//08:00

        Date classStartTime = DateUtil.toDate(targetDay + " " + DateUtil.format(startTime, "HH:mm:ss"), null);;
        Date classEndTime = DateUtil.toDate(targetDay + " " + DateUtil.format(classStartTime, "HH:mm:ss"), null);;
        classEndTime = DateUtil.addDayForDate(classEndTime,1);



        /*查询执行单信息,获取sdId*/
        List<ExecuteInfo> executeInfoList =
                iExecuteInfoService.getBaseMapper().selectList(new QueryWrapper<ExecuteInfo>()
                        .eq("ma_id", maId).eq("target_day", targetDay));
        List<Integer> exIds = new ArrayList<>();
        for(ExecuteInfo executeInfo : executeInfoList){
            exId = executeInfo.getId();
            Integer wfId = executeInfo.getWfId();
            WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
            if(workbatchShift != null){
                Integer ldStay = workbatchShift.getMouldStay() == null ? 0 : workbatchShift.getMouldStay();
                mouldStay += ldStay;//换膜总时长
                mouldNum++;
            }
//            qualityNum += ityNum;//质检次数
            exIds.add(executeInfo.getId());
        }
        prepareStay = mouldStay;//先用换膜时间

        /*获取上报表良品,废品,作业数*/
        List<ExecuteBriefer> executeBrieferList = new ArrayList<>();
        if(!exIds.isEmpty()) {
            executeBrieferList = getNumberByExId(exIds);
        }
        Integer usId = null;
        String usName = null;
        if(!executeBrieferList.isEmpty()){
            for(ExecuteBriefer executeBriefer : executeBrieferList){
                usId = executeBriefer.getHandleUsid();//处理上报人员
                Integer productNum = executeBriefer.getProductNum();//作业数
                Integer countNum = executeBriefer.getCountNum();//良品数
                Integer wasteNum = executeBriefer.getWasteNum();//废品数
                if(productNum == null){
                    productNum = 0;
                }
                if(countNum == null){
                    countNum = 0;
                }
                if(wasteNum == null){
                    wasteNum = 0;
                }
                workCount += productNum;
                nodefectCount += countNum;
                watesCount += wasteNum;
            }
        }
        if(usId != null){
            BaseStaffinfo baseStaffinfo = baseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("user_id", usId));
            if(baseStaffinfo != null){
                usName = baseStaffinfo.getName();
            }
        }

        prepareStay = mouldStay;//先用换膜时间
        List<WorkbatchMachShiftVO> workbatchMachShiftVOList = workbatchShiftMapper.findByMaId(maId);
        List<Integer> ids = new ArrayList<>();
        for(WorkbatchMachShiftVO workbatchMachShiftVO : workbatchMachShiftVOList){
            Integer id = workbatchMachShiftVO.getId();
            ids.add(id);
        }
        /*班次信息*/
        List<WorkbatchShiftset> workbatchShiftsetList = iWorkbatchShiftsetService.getBaseMapper().selectBatchIds(ids);
        for(WorkbatchShiftset batchShiftset : workbatchShiftsetList){
            Integer mealStay = batchShiftset.getMealStay();
            mealStay = mealStay == null ? 0 : mealStay;
            restStay += mealStay;
        }
        if(usId != null){
            BaseStaffinfo baseStaffinfo = baseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("user_id", usId));
            if(baseStaffinfo != null){
                usName = baseStaffinfo.getName();
            }
        }
        /*查询设备信息*/
        MachineMainfo machineMainfo = iMachineMainfoService.getById(maId);
        String maName = machineMainfo.getName();//设备名称
        Integer prId = machineMainfo.getProId();
        ProcessMachlink processMachlink = null;
        if(prId != null){
            processMachlink = iProcessMachlinkService.getOne(new QueryWrapper<ProcessMachlink>().eq("pr_id", prId).eq("ma_id", maId));
        }else {
            log.info("---------------------设备没有主工序------------------------");
        }
        Integer speed = null;
        if(processMachlink != null){
            speed = processMachlink.getSpeed();//理论速度
        }else {
            log.info("----------------------主工序绑定设备----------------------");
        }
        Integer ckTime = Math.abs(24*60);//班次时间(分)
        Integer standardRuntime = ckTime - restStay;//标准生产时间
        Integer planutilizeStay = standardRuntime;//计划稼动时间
        /*查询设备停机时长*/
        Integer sumTimeBy = superviseIntervalService.getSumTimeByMaId(2, maId, classStartTime, classEndTime);//设备停机时间(秒)
        stayTotal = sumTimeBy / 60;//(分)
        List<SuperviseRegular> superviseRegularList = superviseRegularService.getBaseMapper().selectList(new QueryWrapper<SuperviseRegular>()
                .eq("status", 1).eq("ma_id", maId).le("start_time", classEndTime).ge("start_time", classStartTime));
        Integer boxNum = 0;
        Integer timeByMaId = 0;
        for(SuperviseRegular superviseRegular : superviseRegularList){
            Integer diffNum = superviseRegular.getDiffNum();//间隔时间（秒）
            diffNum = diffNum == null ? 0 : diffNum;
            Integer pcout = superviseRegular.getPcout();//间隔计数
            pcout = pcout == null ? 0 : pcout;
            boxNum += pcout;//盒子计数
            timeByMaId += diffNum;//设备运行时长(秒)
        }
        // Integer boxNum = superviseIntervalService.SumBoxNumber(maId, classStartTime, classEndTime);//盒子计数
        /*查询设备运行时长(秒)*/
        //Integer timeByMaId = superviseIntervalService.getSumTimeByMaId(1, maId, classStartTime, classEndTime);
        timeByMaId = timeByMaId == null ? 0 : timeByMaId;
        factutilizeStay = timeByMaId / 60;//实际稼动时间(转化为分)
        Integer factSpeed = 0;
        sumTimeBy = sumTimeBy == null ? 0 : sumTimeBy;
        if(factutilizeStay == 0){
            factSpeed = 0;
        }else {
            if(boxNum != null && boxNum != 0){
                factSpeed = boxNum/(factutilizeStay + (sumTimeBy/60));//实际速度(/分钟)
            }
        }
        factSpeed= factSpeed*60;//实际速度(/小时)
        double utilize = factutilizeStay / (double) planutilizeStay;
        utilizeRate = new BigDecimal(utilize * 100); //时间稼动率(实际稼动时间/计划稼动时间)

        double perform = 0.0;
        if(speed != null && speed != 0){
            perform = (factSpeed / (double)speed);
        }else {
            perform = 1;//如果理论速度为空或0,则性能稼动率默认为100%
        }
        performRate = new BigDecimal(perform * 100);

        double yield;
        if(workCount != null && workCount != 0){
            yield = (nodefectCount / (double)workCount);//良品率
        }else {
            yield = 1.0;//如果未上报,作业数,良品数均为空,则良品率默认为100%
        }
        yieldRate = new BigDecimal(yield * 100);//性能稼动率(实际速度/理论速度)

        oeeRate = utilize * yield * perform;//OEE综合效率
        BigDecimal gatherRate = new BigDecimal(oeeRate * 100);//OEE综合效率
        /*-------------------OEE数据信息表--------------------*/

        StatisMachoee statisMachoee =
                iStatisMachoeeService.getOne(new QueryWrapper<StatisMachoee>()
                        .eq("oe_date", targetDay).eq("oe_type", 2).eq("ma_id", maId));
        if(statisMachoee == null){
            statisMachoee = new StatisMachoee();//OEE数据信息表
        }
        statisMachoee.setMaId(maId);//设备id
        statisMachoee.setMaName(maName);//设备名称
        statisMachoee.setCreateAt(new Date());
        statisMachoee.setExId(exId);//执行单id(现在取的最后一个执行单id)
        statisMachoee.setOeDate(targetDay);//oee统计时间
        statisMachoee.setOeType(2);//1、设备排产单 2、设备定时任务
        statisMachoee.setSdId(sdId);//最后一个排产单id
        statisMachoee.setUsId(usId);
        statisMachoee.setUsName(usName);
        iStatisMachoeeService.saveOrUpdate(statisMachoee);

        /*----------------班次设备OEE数据表--------------------*/

        StatisMachregular statisMachregular = statisMachRegularService.getOne(new QueryWrapper<StatisMachregular>()
                .eq("sm_id", statisMachoee.getId()));
        if(statisMachregular == null){
            statisMachregular = new StatisMachregular();
        }
        statisMachregular.setBoxNum(boxNum);//盒子计数
        statisMachregular.setFactSpeed(factSpeed);//实际能力生产性H*每小时生产速度取整（班次总数和总时长）
        statisMachregular.setFactutilizeStay(factutilizeStay);//实际稼动时间
        statisMachregular.setFaultStay(faultStay);//设备故障C2-N（生产设备故障,公共设备故障）
        statisMachregular.setGatherRate(gatherRate);//OEE综合效率
        statisMachregular.setNodefectCount(nodefectCount);//良品数-无缺陷
        statisMachregular.setPerformRate(performRate);//良品率
        statisMachregular.setPlanutilizeStay(planutilizeStay);//计划稼动时间C
        statisMachregular.setPrepareStay(prepareStay);//生产准备时间L（分计算）实际时间
        statisMachregular.setQualityNum(qualityNum);//质检次数
        statisMachregular.setSmId(statisMachoee.getId());//设备统计唯一主键id（yb_statis_machoee）
        statisMachregular.setStandardRuntime(standardRuntime);//标准生产时间分钟（标准出勤-休息开会吃饭）
        statisMachregular.setStayTotal(stayTotal);//停机合计分钟
        statisMachregular.setUtilizeRate(utilizeRate);//时间稼动率
        statisMachregular.setWatesCount(watesCount);//废品数
        statisMachregular.setWorkCount(workCount);//作业数F（实际作业总数）
        statisMachregular.setYieldRate(yieldRate);//良品率G
        statisMachregular.setNormalSpeed(speed);//理论速度
        statisMachregular.setAbrasionStay(abrasionStay);//磨损更换C2-Q
        statisMachregular.setMaintainNum(maintainNum);//设备保养次数
        statisMachregular.setMaintainStay(maintainStay);//设备保养B2（分计算）
        statisMachregular.setManageStay(manageStay);//管理停止C2-P
        statisMachregular.setMouldNum(mouldNum);//换膜次数
        statisMachregular.setMouldStay(mouldStay);//换膜时长
        statisMachregular.setPlanStay(planStay);//计划停机C2-O
        statisMachregular.setQualityStay(qualityStay);//品质故障C2-M
        statisMachregular.setRestStay(restStay);//休息吃饭C2-R
        statisMachregular.setTaskCount(taskCount);//加工数-任务数（计划订单要求总数+损耗数）
        statisMachregular.setWorkStay(workStay);//出勤时间A -间隔停留时间（分）真实出勤时间
        statisMachregular.setCreateAt(new Date());
        statisMachRegularService.saveOrUpdate(statisMachregular);
    }


    /*--------------------------------------------------*/


    //每天早上8点晚上8点
//    @Scheduled(cron = "0 2 8,20 * * ?")
    public void task() {
        try {
            DBIdentifier.setProjectCode("xingyi");
            statisLeanOee(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/generateStatisLeanOee")
    @ApiOperation(value = "生成OEEReport")
    public void statisLeanOee(String machineOeeId) {
        DBIdentifier.setProjectCode("xingyi");
        String sdDate = DateUtil.format(DateUtil.addDayForDate(new Date(System.currentTimeMillis()),-1)); //获取当日的排产单信息

//        Integer whiteStartTime = 8;
//        Integer minutes = 0;//班次分钟数
//        Integer whiteEndTime = 20;
//        Integer blackStartTime = 20;
//        Integer blackEndTime = 8;
        //这里循环机器 并且写死了班次
        List<MachineMainfo> machineMainfos = iMachineMainfoService.list();
        WorkbatchShiftset nowshiftset = iWorkbatchShiftsetService.getShiftsetByNow();
        //判断当时间是否为某生产排产时间
        if (nowshiftset != null) {
            Date classStartTime = nowshiftset.getStartTime();
            Date classEndTime = nowshiftset.getEndTime();
            Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, sdDate);
            classStartTime = wsDatetime[0];
            classEndTime = wsDatetime[1];

            //设备清单循环
            for (MachineMainfo machineMainfo : machineMainfos) {
                if(machineMainfo.getId() == 50208)
                    GenerateOEEReport(machineMainfo.getId(),sdDate,nowshiftset.getId());
            }
        }
    }



    @PostMapping("/generateLeanOee")
    @ApiOperation(value = "根据日期和班次Id进行生成OEE")
    public void generateLeanOee(String sdDate,Integer wsId) {
        //根据班次id查询班次信息
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getBaseMapper().selectById(wsId);
//
//        Date classStartTime = workbatchShiftset.getStartTime();
//        Date classEndTime = workbatchShiftset.getEndTime();
//        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, sdDate);
//        classStartTime = wsDatetime[0];
//        classEndTime = wsDatetime[1];
        //todo wyn ********************

        List<MachineMainfo> machineMainfos = iMachineMainfoService.list();
        for (MachineMainfo machineMainfo : machineMainfos) {
            GenerateMachineOEE( machineMainfo.getId(),sdDate,wsId);
        }
    }

    /****
     * 生产OEE的报表内容信息
     * @param maId
     * @param sdDate
     * @param wsId
     * @desc wyn 2020-07-22
     */
    public void GenerateMachineOEE(Integer maId,String sdDate,Integer wsId) {
        //OEE数据信息表_yb_statis_machoee 主表信息--扩展表信息
        StatisMachoee statisMachoee = new StatisMachoee();
        StatisMachsingle statisMachsingle = new StatisMachsingle();//oee对象插入

        //获取班次信息管理
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getBaseMapper().selectById(wsId);
        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(wsId,maId);
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, sdDate);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        //获取班次的时间 标准出勤时间
        Double classCont = DateUtil.calLastedTime(classEndTime.getTime(),classStartTime)/60;
        statisMachsingle.setWorkStay(classCont.intValue());//

        //A 列 设备名称  查询条件名称
        MachineMainfo machineMainfo = iMachineMainfoService.getBaseMapper().selectById(maId);
        String machineName = null;
        if (machineMainfo != null) {
            machineName = machineMainfo.getName();
        }
        statisMachoee.setMaId(maId);
        statisMachoee.setMaName(machineName);
        statisMachoee.setOeDate(sdDate);
        statisMachoee.setWsId(wsId);
        statisMachoee.setSfName(workbatchShiftset.getCkName());


        //吃饭间距时间：从吃饭的停留时间
        Integer mealstay = workbatchShiftset.getMealStay();//按照分钟来
        //计划操作时间
//        statisMachsingle.setPlanutilizeStay();// 计划稼动时间
//        statisMachsingle.setFactutilizeStay();//实际稼动时间
//        statisMachsingle.setFactSpeed();//实际运行速度
//
//        statisMachsingle.setBoxNum();
//        statisMachsingle.setStayTotal();//停机的合计统计时间
//
//        statisMachsingle.setRestStay(mealstay); //停机吃饭
//
//        /*++++++++++++++++++++++++++++++++++统计OEE数量++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
//        statisMachsingle.setUtilizeRate();//时间稼动率E*
//        statisMachsingle.setYieldRate();//良品率G*
//        statisMachsingle.setPerformRate();//性能稼动率
//        statisMachsingle.setGatherRate();//OEE的综合效率K*

        //获取今日班次排产单信息 暂不用
        // List<WorkbatchOrdlink> ordLinkBysdate = iWorkbatchOrdlinkService.getOrdLinkBysdate(sdDate,maId,wsId);

        //B 列 oee统计时间2020-04-15 ！
        String calculateTime = DateUtil.refNowDay();

    }

    private void setBrieferOEEBy(){

        StatisMachsingle statisMachsingle = new StatisMachsingle();//oee对象插入
        //良品数  //作业数 //废品数
        statisMachsingle.getNodefectCount();//无缺陷
        statisMachsingle.getWatesCount();//废品        wates_count
        statisMachsingle.getWorkCount();//作业总数量

    }


    /****
     * 生产OEE的报表内容信息
     * @param maId
     */
    public void GenerateOEEReport(Integer maId,String sdDate,Integer wsId) {
        //获取班次信息管理
//        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getBaseMapper().selectById(wsId);
        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(wsId,maId);
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, sdDate);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];

        //A 列 设备名称  查询条件名称
        MachineMainfo machineMainfo = iMachineMainfoService.getBaseMapper().selectById(maId);
        String machineName = null;
        if (machineMainfo != null) {
            machineName = machineMainfo.getName();
        }

        //B 列 oee统计时间2020-04-15 ！
        String calculateTime = DateUtil.refNowDay();
        //K 列印联盒采集数量!
        Integer sumCollectNumber = 0;
        //M 列良品数量! 良品数是修正后的数量。
        Integer sumGoodAccept = 0;
        //N 列废品数量!
        Integer sumWasteNumber = 0;
        //状态信息内容
        List<ExecuteStateVO> executeStats = iExecuteStateService.getSumCountByMtId(classStartTime, classEndTime, maId);
        if (executeStats.size() != 0 && executeStats != null && executeStats.get(0) != null) {
            sumWasteNumber = executeStats.get(0).getWasteNum();
            sumCollectNumber = executeStats.get(0).getBoxNum();
            sumGoodAccept = executeStats.get(0).getCountNum();
        }
        if (sumCollectNumber == null) {
            sumCollectNumber = 0;
        }
        if (sumWasteNumber == null) {
            sumWasteNumber = 0;
        }
        if (sumGoodAccept == null) {
            sumGoodAccept = 0;
        }
        //一个班次可能会有的多个排产。
        List<WorkbatchOrdoee> resultList = new ArrayList<>();
        //标准速度
        List<String> ids = iExecuteStateService.getDistinceSdId(classStartTime, classEndTime, maId);
        //计划班次数量
        Integer planClassNumber = 1;
        //班次总损耗数
        Integer wasterNumber = 1;
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
        Double standDifficultSum = 0.0;
        //标准指标  机器速度
        Integer machineSpeed = 1;
        Integer totalSpeedPlan = 1;
        if (resultList.size() != 0) {
            for (WorkbatchOrdoee ordoee : resultList) {
                //换模时间
                standMouldStay = standMouldStay + ordoee.getMouldStay();
                standDifficultSum = standDifficultSum + ordoee.getDifficultNum();//难易程度
                //在这里获得每一个设备的标准时速度
                totalQualityNumber = totalQualityNumber ;//+ ordoee.getQualityNum();
                WorkbatchOrdlink workbatchOrdlink = iWorkbatchOrdlinkService.getById(ordoee.getWkId());
                if (workbatchOrdlink != null) {
                    List<MachineMainfoVO> machineMainfoVOS = iMachineMainfoService.getRateByMachineId(maId, workbatchOrdlink.getPrName());
                    machineSpeed = machineSpeed + ordoee.getSpeed() * (workbatchOrdlink.getPlanNum());
                    totalSpeedPlan = totalSpeedPlan + workbatchOrdlink.getPlanNum();
                }
            }
        }

        Integer speed = 1;
        MachineMainfo machine = iMachineMainfoService.getById(maId);
        ProcessMachlink processMachlink = iProcessMachlinkService.getEntityByPrMt(machine.getId(), machine.getProId());
        speed = processMachlink.getSpeed();
        //O 列作业数！
        Integer workNumber = sumGoodAccept + sumWasteNumber;
        //L 列质检次数!
        List<ExecuteWasteVO> executeWasteVOS = iExecuteWasteService.getWateByTime(classStartTime, classEndTime);
        Integer qualityTime = 0;
        if (executeWasteVOS.size() != 0 && executeWasteVOS != null) {
            qualityTime = executeWasteVOS.size();
        }
        //W 停机等待总计时间！
        Integer stop = superviseIntervalService.getSumTimeByMaId(2, maId, classStartTime, classEndTime);
        Integer stopMin = stop / 60;
        //F 列 实际生产准备时间
        long pareSecond = 1;
        try {
            List<ExecuteInfo> executeInfos = iExecuteInfoService.getSdIdsbyTime(classStartTime, classEndTime, maId);
            for (ExecuteInfo info : executeInfos) {
                pareSecond = pareSecond + (info.getExeTime().getTime() - info.getStartTime().getTime());
            }
        } catch (Exception e) {
            pareSecond = 41 * 1000 * 60;
        }
        //X 标准出勤 实际准备时间！ 实际的保养时间和实际的换膜时间 生产准备时间L（分计算）实际时间
        Integer realPreTime = (int) (pareSecond / (1000 * 60));
        //班次时间
        Long miniSecond = classEndTime.getTime() - classStartTime.getTime();
        Long min = millisecondToMinute(miniSecond);
        Integer standMealTime = 35;
        //标准生产时间（标准出勤-吃饭、休息）【休息吃饭C2-R：休息R1、吃饭R2、开会R3】!!
        Integer standProduceTime = min.intValue();
        //计划稼动时间（标准生产时间-标准保养、和换模时间）
        Integer planActivationTime = (int) (standProduceTime - standMealTime);
        //实际稼动时间实际稼动时间（标准生产时间-实际生产时间）
        //（计划稼动时间-故障停机-生产准备-设备故障）
        //减去时间：设备故障C2-N、品质故障C2-M、计划停机C2-O、管理停止C2-P
        Integer RealActivationTime = min.intValue() - stopMin;
        // 时间稼动率（实际时间/标准时间）
        BigDecimal TimeMonement = new BigDecimal((float) RealActivationTime / standProduceTime).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (TimeMonement.compareTo(new BigDecimal(0.0100)) == -1) {
            TimeMonement = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal goodRates;
        //良品率（良品数/盒子数）
        try {
            goodRates = new BigDecimal(((float) sumCollectNumber / (sumCollectNumber))).setScale(4, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            goodRates = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        if (goodRates.compareTo(new BigDecimal(0.0100)) == -1) {
            goodRates = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //实际生产性能（速度值）（作业数/实际稼动时间）
        BigDecimal productionPerformance = new BigDecimal(60 * (((float) sumCollectNumber / RealActivationTime))).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (productionPerformance.compareTo(new BigDecimal(0.0100)) == -1) {
            productionPerformance = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //性能稼动率
        BigDecimal qualityRate = productionPerformance.divide(new BigDecimal((float) speed), 4, BigDecimal.ROUND_HALF_UP);
        if (qualityRate.compareTo(new BigDecimal(0.0100)) == -1) {
            qualityRate = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //OEE（时间稼动率x良品率x性能稼动率）
        BigDecimal OEErate = (TimeMonement.multiply(goodRates).multiply(qualityRate)).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (OEErate.compareTo(new BigDecimal(0.0100)) == -1) {
            OEErate = new BigDecimal(0.0001).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        //OEE数据信息表_yb_statis_machoee
        StatisMachoee statisMachoee = new StatisMachoee();
        statisMachoee.setOeDate(calculateTime);
        statisMachoee.setMaId(maId);
        statisMachoee.setMaName(machineName);
        //设备OEE
        statisMachoee.setOeType(1);
        statisMachoee.setSfStartTime(classStartTime);
        statisMachoee.setSfEndTime(classEndTime);

        iStatisMachoeeService.saveOrUpdate(statisMachoee);
        StatisMachsingle statisMachsingle = new StatisMachsingle();//oee对象插入
        Map<String, Object> machSigleMap = new HashMap<>();
        List<StatisMachsingle> statisMachsingles = iStatisMachsingleService.getBaseMapper().selectByMap(machSigleMap);
        statisMachsingle.setSmId(statisMachoee.getId());
        //设备包养的时间和次数
        statisMachsingle.setPrepareStay(realPreTime);
        statisMachsingle.setStandardRuntime(standProduceTime.intValue());
        statisMachsingle.setPlanutilizeStay(planActivationTime.intValue());
        statisMachsingle.setFactutilizeStay(RealActivationTime.intValue());
        statisMachsingle.setNodefectCount(sumGoodAccept);
        statisMachsingle.setWatesCount(sumWasteNumber);
        statisMachsingle.setWorkCount(workNumber);
        statisMachsingle.setBoxNum(sumCollectNumber);
        statisMachsingle.setQualityNum(qualityTime);
        statisMachsingle.setFactSpeed(productionPerformance.intValue());
        statisMachsingle.setStayTotal(stopMin);
        statisMachsingle.setUtilizeRate(TimeMonement.multiply(new BigDecimal(100)));
        statisMachsingle.setYieldRate(goodRates.multiply(new BigDecimal(100)));
        statisMachsingle.setPerformRate(qualityRate.multiply(new BigDecimal(100)));
        statisMachsingle.setGatherRate(OEErate.multiply(new BigDecimal(100)));
        iStatisMachsingleService.saveOrUpdate(statisMachsingle);
        StatisOeeset statisOeeset = new StatisOeeset();
        //统计分析设置表_yb_statis_oeeset
        //精益OEE
        statisOeeset.setStType(3);
        statisOeeset.setDbId(statisMachoee.getId());
        statisOeeset.setSfStaytime(min.intValue());
        statisOeeset.setResetNum(1);//休息次数
        statisOeeset.setResetTime(standMealTime);
        //标准的保养时间
        statisOeeset.setMaintainNum(1);
        //标准难易程度
        statisOeeset.setPrepareStay(realPreTime);
        // 当machOeeID 为null 的时候。我们new 对象判断为插入，如果不为null 的时候，那么我们是直接update。
        iStatisOeesetService.saveOrUpdate(statisOeeset);
        iStatisOeesetService.updateById(statisOeeset);
    }

    public long millisecondToMinute(Long millisecond) {
        return millisecond / (60 * 1000);
    }

    /****
     * 根据开始时间、结束时间、排产日期 返回数组
     * @param classStartTime
     * @param classEndTime
     * @param sdDate
     * @return
     */
    public Date[] setWsDatetime (Date classStartTime, Date classEndTime,String sdDate){
        Date[] wsDatetime = new Date[2];
        //判断开始时间大于结束时间，需要加一天在实际时间
        classStartTime = DateUtil.toDate(sdDate+" "+DateUtil.format(classStartTime,"HH:mm:ss"),null);
        classEndTime = DateUtil.toDate(sdDate+" "+DateUtil.format(classEndTime,"HH:mm:ss"),null);
        if (classStartTime.compareTo(classEndTime) > 0) {
            classEndTime = DateUtil.addDayForDate(classEndTime,1);
        }
        wsDatetime[0] = classStartTime;
        wsDatetime[1] = classEndTime;
        return wsDatetime;
    }


}

