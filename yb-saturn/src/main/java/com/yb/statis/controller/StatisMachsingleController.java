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
package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.statis.entity.*;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.statis.excelUtils.Page;
import com.yb.statis.service.*;
import com.yb.statis.vo.*;
import com.yb.statis.wrapper.StatisMachsingleWrapper;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 控制器
 *
 * @author Blade
 * @since 2020-04-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statismachsingle")
@Api(value = "", tags = "接口")
public class StatisMachsingleController extends BladeController {
    private IStatisMachsingleService statisMachsingleService;
    //OEE需要导入的service
    private IWorkbatchOrdlinkService iWorkbatchOrdlinkService;
    private IStatisMachoeeService iStatisMachoeeService;
    private IStatisOeesetService iStatisOeesetService;
    private IStatisMachsingleService iStatisMachsingleService;
    private IBaseDeptinfoService iBaseDeptinfoService;
    private IExecuteInfoService iExecuteInfoService;
    private IStatisOrdinfoService iStatisOrdinfoService;
    private IStatisOrdsingleService iStatisOrdsingleService;
    @Autowired
    private StatisLeanOeeController statisLeanOeeController;
    @Autowired
    private IExecuteBrieferService iExecuteBrieferService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private IMachineMainfoService machineMainfoService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachsingle")
    public R<StatisMachsingleVO> detail(StatisMachsingle statisMachsingle) {
        StatisMachsingle detail = statisMachsingleService.getOne(Condition.getQueryWrapper(statisMachsingle));
        return R.data(StatisMachsingleWrapper.build().entityVO(detail));
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statisMachsingle")
    public R<IPage<StatisMachsingleVO>> list(StatisMachsingle statisMachsingle, Query query) {
        IPage<StatisMachsingle> pages = statisMachsingleService.page(Condition.getPage(query), Condition.getQueryWrapper(statisMachsingle));
        return R.data(StatisMachsingleWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisMachsingle")
    public R<IPage<StatisMachsingleVO>> page(StatisMachsingleVO statisMachsingle, Query query) {
        IPage<StatisMachsingleVO> pages = statisMachsingleService.selectStatisMachsinglePage(Condition.getPage(query), statisMachsingle);
        return R.data(pages);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisMachsingle")
    public R save(@Valid @RequestBody StatisMachsingle statisMachsingle) {
        return R.status(statisMachsingleService.save(statisMachsingle));
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisMachsingle")
    public R update(@Valid @RequestBody StatisMachsingle statisMachsingle) {
        return R.status(statisMachsingleService.updateById(statisMachsingle));
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisMachsingle")
    public R submit(@Valid @RequestBody StatisMachsingle statisMachsingle) {
        return R.status(statisMachsingleService.saveOrUpdate(statisMachsingle));
    }


    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        Map<String, Object> statisMachsingleMap = new HashMap<>();
        statisMachsingleMap.put("sm_id", ids);
        statisMachsingleService.getBaseMapper().deleteByMap(statisMachsingleMap);
        Map<String, Object> iStatisOeesetMap = new HashMap<>();
        iStatisOeesetMap.put("db_id", ids);
        iStatisOeesetMap.put("st_type", 1);
        iStatisOeesetService.getBaseMapper().deleteByMap(iStatisOeesetMap);
        return R.data(iStatisMachoeeService.getBaseMapper().deleteById(ids));
    }

    /**
     * OEE设备排查
     */
    @PostMapping("/getMainInfoById")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "OEE排产展示")
    public StatisMachOrderVo getMainInfoById(@ApiParam(value = "主键", required = true) @RequestParam String id, @RequestParam String dpId) {
        StatisMachOrderVo statisMachOrderVo = getOeeMianInfoById(Integer.parseInt(id), Integer.parseInt(dpId));
        return statisMachOrderVo;
    }

    public StatisMachOrderVo getOeeMianInfoById(Integer id, Integer dpId) {
        StatisMachOrderVo statisMachOrderVo = new StatisMachOrderVo();
        StatisMachoee statisMachoee = iStatisMachoeeService.getById(id);
        //时间
        statisMachOrderVo.setExchangeTime(DateUtil.changeDay(statisMachoee.getOeDate()));
        //车间名称
        BaseDeptinfo baseDeptinfo = iBaseDeptinfoService.getById(dpId);
        statisMachOrderVo.setDpName(baseDeptinfo.getDpName());
        //班次
        statisMachOrderVo.setSfName(statisMachoee.getSfName());//班次名称
        Map<String, Object> machsigleMap = new HashMap<>();
        machsigleMap.put("sm_id", statisMachoee.getId());
        List<StatisMachsingle> statisMachsingles = iStatisMachsingleService.getBaseMapper().selectByMap(machsigleMap);
        StatisMachsingle statisMachsingle = statisMachsingles.get(0);
        // 当班OEE
        statisMachOrderVo.setGatherRate(statisMachsingle.getGatherRate().doubleValue());
        //性能稼动率
        statisMachOrderVo.setPerformRate(statisMachsingle.getPerformRate().doubleValue());
        //良品率
        statisMachOrderVo.setYieldRate(statisMachsingle.getYieldRate().doubleValue());
        //设备故障率
        if (statisMachsingle.getFaultStay() == null) {
            statisMachsingle.setFaultStay(0);
        }
        statisMachOrderVo.setEquFailRate((double) statisMachsingle.getFaultStay() / statisMachsingle.getPlanutilizeStay());
        //时间稼动率
        statisMachOrderVo.setUtilizeRate(statisMachsingle.getUtilizeRate().doubleValue());
        //计划稼动时间
        statisMachOrderVo.setPlanutilizeStay(statisMachsingle.getPlanutilizeStay());
        //生产准备时间 statisMachsingle。prepare_stay
        statisMachOrderVo.setPrepareStay(statisMachsingle.getPrepareStay());
        //设备故障时间   statisMachsingle。fault_stay
        statisMachOrderVo.setFaultStay(statisMachsingle.getFaultStay());
        //品质损失时间 statisMachsingle。quality_stay 品质故障C2-M
        statisMachOrderVo.setQualityStay(statisMachsingle.getQualityStay());
        //品种切换时间 statisMachsingle。mould_stay 换膜时长
        statisMachOrderVo.setMouldStay(statisMachsingle.getMouldStay());
        //实验时间 对应statisMachsingle.plan_stay 计划停机C2-O
        statisMachOrderVo.setRearchStay(statisMachsingle.getPlanStay());
        //管理停止时间
        statisMachOrderVo.setManageStay(statisMachsingle.getManageStay());
        // 其他时间 statisMachsingle 磨损更换时间
        statisMachOrderVo.setOtherStay(statisMachsingle.getAbrasionStay());
        //实际稼动时间
        statisMachOrderVo.setFactutilizeStay(statisMachsingle.getFactutilizeStay());
        //TODO 当班主要问题点
        statisMachOrderVo.setMainProblem("暂时无当班主要问题点");
        //TODO 告知下班注意事项
        statisMachOrderVo.setClassTranscationInfo("暂时无告知下班注意事项");
        //通过这里去寻找对应的工单
        Integer wsId = statisMachoee.getWsId();//班次id
        String oeDate = statisMachoee.getOeDate();//oee统计时间
        Integer maId = statisMachoee.getMaId();
        List<ExecuteInfo> executeInfoList =
                iExecuteInfoService.getBaseMapper().selectList(new QueryWrapper<ExecuteInfo>()
                        .eq("ws_id", wsId).eq("target_day", oeDate).eq("ma_id", maId).groupBy("wf_id"));
        //在执行表中找到对应的数据
        List<workBatchVo> workBatchLsit = new ArrayList<>();
        Integer index = 0;
        for (ExecuteInfo info : executeInfoList) {
            Date endTime = info.getEndTime();
            if (endTime != null) {
                Integer SdId = info.getSdId();
                Integer wfId = info.getWfId();
                workBatchVo workBatchVo = new workBatchVo();
                index++;
                workBatchVo.setIndex(index);
                //这里通过SDID把产品到排查 一并查询完毕
                WorkbatchOrdlinkVO workbatchOrdlinkVO = iWorkbatchOrdlinkService.getWorkBatchInfoBySdId(SdId);
                if(workbatchOrdlinkVO != null){
                    //批次编号
                    workBatchVo.setBatchNo(workbatchOrdlinkVO.getWbNo());
                    //产品名称
                    workBatchVo.setPdName(workbatchOrdlinkVO.getPdName());
                    //工序名称
                    workBatchVo.setPrName(workbatchOrdlinkVO.getPrName());
                    //应出勤
                    workBatchVo.setDutyNum(workbatchOrdlinkVO.getDutyNum());
                    //部件名称
                    workBatchVo.setPtName(workbatchOrdlinkVO.getPartName());
                    /*根据排产单查询上报信息*/
                    List<ExecuteBriefer> executeBrieferList = iExecuteBrieferService.getBaseMapper().selectList(new QueryWrapper<ExecuteBriefer>()
                            .eq("wf_id", wfId));
                    Integer productNum = 0;
                    Integer countNum = 0;
                    Integer wasteNum = 0;
                    StringBuffer actualDateTime = new StringBuffer();
                    if(!executeBrieferList.isEmpty()){
                        for(ExecuteBriefer executeBriefer : executeBrieferList){
                            StringBuffer bfStartTime = new StringBuffer(DateUtil.format(executeBriefer.getStartTime(), "HH:mm"));
                            String bfEndTime = DateUtil.format(executeBriefer.getEndTime(), "HH:mm");
                            Integer executeBrieferProductNum = executeBriefer.getProductNum() == null ? 0 :executeBriefer.getProductNum();
                            productNum += executeBrieferProductNum;
                            Integer executeBrieferCountNum = executeBriefer.getCountNum() == null ? 0 :executeBriefer.getCountNum();
                            countNum += executeBrieferCountNum;
                            Integer executeBrieferWasteNum = executeBriefer.getWasteNum() == null ? 0 :executeBriefer.getWasteNum();
                            wasteNum += executeBrieferWasteNum;
                            bfStartTime.append("～");
                            bfStartTime.append(bfEndTime);
                            actualDateTime.append(bfStartTime + ";");
                        }
                    }
                    workBatchVo.setActualDateTime(actualDateTime.toString());
                    //投入数  来自yb_statis_ordSingle的work_count作业数F（实际作业总数）
                    workBatchVo.setWorkCount(productNum);
                    //良品数
                    workBatchVo.setNodefectCount(countNum);
                    //废品数
                    workBatchVo.setWatesCount(wasteNum);
                    /*查询班次信息*/
                    WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
                    if(workbatchShift != null){
                        Date proBeginTime = workbatchShift.getProBeginTime();//计划开始时间
                        Date proFinishTime = workbatchShift.getProFinishTime();//计划结束时间
                        StringBuffer planDateTime = new StringBuffer(DateUtil.toDatestr(proBeginTime, "HH:mm"));
                        planDateTime.append("～" + DateUtil.toDatestr(proFinishTime, "HH:mm"));
                        workBatchVo.setPlanDateTime(planDateTime.toString());
                        //标准产能 机器的转速
                        workBatchVo.setNormalSpeed(workbatchShift.getSpeed());
                        //计划数
                        workBatchVo.setPlanNum(workbatchShift.getPlanNum());
                    }
                    //机台
                    workBatchVo.setMachineName(statisMachoee.getMaName());
                    //达成率  良品/计划数
                    if(workBatchVo.getNodefectCount() == null || workBatchVo.getPlanNum() == null){
                        workBatchVo.setReachRate(null);
                    }else {
                        workBatchVo.setReachRate((double) workBatchVo.getNodefectCount() / workBatchVo.getPlanNum());
                    }
                    //良品率  良品/投入数
                    if(workBatchVo.getNodefectCount() == null ||  workBatchVo.getWorkCount() == null){
                        workBatchVo.setGoodRate(null);
                    }else {
                        workBatchVo.setGoodRate((double) workBatchVo.getNodefectCount() / workBatchVo.getWorkCount());
                    }
                    //实际产能
                    Integer hour = 0;
                    List<ExecuteInfo> infoList =
                            iExecuteInfoService.getBaseMapper().selectList(new QueryWrapper<ExecuteInfo>().eq("wf_id", wfId));
                    for(ExecuteInfo ecuteInfo : infoList){
                        Date infoEndTime = ecuteInfo.getEndTime();
                        Date infoStartTime = ecuteInfo.getStartTime();
                        if (infoEndTime != null) {
                            hour = (int)((infoEndTime.getTime() - infoStartTime.getTime()) / 1000 );//生产时间(秒)
                        }
                    }
                    Integer normalSpeed = null;
                    if(hour != 0){
                        Integer workCount = workBatchVo.getWorkCount();
                        normalSpeed = workCount * 3600 / hour;//实际产能(/小时)
                    }
                    workBatchVo.setFactSpeed(normalSpeed);
                    //性能稼动率
                    if(workBatchVo.getFactSpeed() == null || workBatchVo.getNormalSpeed() == null){
                        workBatchVo.setSpeendRate(null);
                    }else {
                        workBatchVo.setSpeendRate((double) workBatchVo.getFactSpeed() / workBatchVo.getNormalSpeed());
                    }

                    //废品率
                    if(workBatchVo.getWatesCount() == null || workBatchVo.getWorkCount() == null){
                        workBatchVo.setWatesRate(null);
                    }else {
                        workBatchVo.setWatesRate((double) workBatchVo.getWatesCount() / workBatchVo.getWorkCount());
                    }
                    //机长
                    workBatchVo.setUserName(statisMachoee.getUsName());
                    workBatchLsit.add(workBatchVo);
                }
            }
        }
        Integer dutyNumCount = 0;//应出勤总人数
        Double realDutyNumCount = 0.0;//实际出勤总人数
        Double poRateCount = 0.0;//P/O不足总数
        Integer planNumCount = 0;//计划数总数
        Integer worksCount = 0;//投入数总数
        Integer nodefectsCount = 0;//良品数总数
        Integer watesCountsCount = 0;//废品数总数
        Integer normalSpeedAvg = 0;//实际产能平均数
        Integer factSpeedAvg = 0;//标准产能平均数
        if (!workBatchLsit.isEmpty()) {
            for (workBatchVo batchVo : workBatchLsit) {
                Integer dutyNum = batchVo.getDutyNum();
                dutyNum = dutyNum == null ? 0 : dutyNum;
                dutyNumCount += dutyNum;
                Double realDutyNum = batchVo.getRealDutyNum() == null ? 0 : batchVo.getRealDutyNum();;
                realDutyNumCount += realDutyNum;//TODO:实际出勤总人数
                Double poRate = batchVo.getPoRate() == null ? 0 : batchVo.getPoRate();;
                poRateCount += poRate;
                Integer planNum = batchVo.getPlanNum() == null ? 0 : batchVo.getPlanNum();
                planNumCount += planNum;
                Integer workCount = batchVo.getWorkCount() == null ? 0 : batchVo.getWorkCount();
                worksCount += workCount;
                Integer nodefectCount = batchVo.getNodefectCount() == null ? 0 : batchVo.getNodefectCount();
                nodefectsCount += nodefectCount;
                Integer watesCount = batchVo.getWatesCount() == null ? 0 : batchVo.getWatesCount();
                watesCountsCount += watesCount;
                Integer normalSpeed = batchVo.getNormalSpeed() == null ? 0 : batchVo.getNormalSpeed();
                normalSpeedAvg += normalSpeed;//实际产能总数
                Integer factSpeed = batchVo.getFactSpeed() == null ? 0 : batchVo.getFactSpeed();
                factSpeedAvg += factSpeed;//标准产能总数
            }
            statisMachOrderVo.setDutyNumCount(dutyNumCount);
            statisMachOrderVo.setRealDutyNumCount(realDutyNumCount);
            statisMachOrderVo.setPoRateCount(poRateCount / workBatchLsit.size());
            statisMachOrderVo.setPlanNumCount(planNumCount);
            statisMachOrderVo.setWorksCount(worksCount);
            statisMachOrderVo.setNodefectsCount(nodefectsCount);
            statisMachOrderVo.setWatesCountsCount(watesCountsCount);
            if (planNumCount != null && planNumCount != 0) {
                Double reachRateAvg = nodefectsCount / (double) planNumCount;//达成率平均数(良品数总数/计划数总数)
                statisMachOrderVo.setReachRateAvg(reachRateAvg);
            }
            if (worksCount != null && worksCount != 0) {
                Double goodRateAvg = nodefectsCount / (double) worksCount;//良品率平均数(良品数总数/投入数总数)
                Double watesRateAvg = watesCountsCount / (double) worksCount;//废品率平均数(废品数总数/投入数总数)
                statisMachOrderVo.setGoodRateAvg(goodRateAvg);
                statisMachOrderVo.setWatesRateAvg(watesRateAvg);
            }
            normalSpeedAvg = (int) Math.round((double) normalSpeedAvg / workBatchLsit.size());//实际产能平均数(实际产能总数/总条数)
            statisMachOrderVo.setNormalSpeedAvg(normalSpeedAvg);
            factSpeedAvg = (int) Math.round((double) factSpeedAvg / workBatchLsit.size());//标准产能平均数(标准产能总数/总条数)
            statisMachOrderVo.setFactSpeedAvg(factSpeedAvg);
            Double speendRateAvg = factSpeedAvg / (double) normalSpeedAvg;//性能稼动率平均数(实际产能平均数/标准产能平均数)
            statisMachOrderVo.setSpeendRateAvg(speendRateAvg);
        }

        statisMachOrderVo.setWorkBatchLsit(workBatchLsit);
        return statisMachOrderVo;
    }


    /**
     * 排产页面导出机长交接班
     * @param maId
     * @param wsId
     * @param targeDay
     * @param response
     * @return
     */
    @RequestMapping("newWfExcel")
    public R newWfExcel(Integer maId, Integer wsId, String targeDay,  HttpServletResponse response){
        List<StatisMachoee> statisMachoeeList = iStatisMachoeeService.list(new QueryWrapper<StatisMachoee>()
                .eq("ma_id", maId).eq("ws_id", wsId).eq("oe_date", targeDay) .eq("oe_type", 1));
        StatisMachoee statisMachoee;
        if(statisMachoeeList.isEmpty()){
            return R.fail("数据不存在");
        }else {
            statisMachoee = statisMachoeeList.get(0);
        }
        if(statisMachoee == null){
            return R.fail("数据不存在");
        }
        MachineMainfo machineMainfo = machineMainfoService.getById(maId);
        StatisMachOrderVo oeeMianInfoById = null;
        if(machineMainfo != null){
            oeeMianInfoById = getOeeMianInfoById(statisMachoee.getId(), machineMainfo.getDpId());
        }
        if(oeeMianInfoById == null){
            return R.fail("数据不存在");
        }
        // 模板位置，输出流
        String templatePath = "model/handoverSheetJSJ.xls";
        BufferedOutputStream bos = null;//导出到网页
        //插入到excel 的数据
        List<StatisMachOrderVo> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        oeeMianInfoById.setChangeTime(sdf.format(oeeMianInfoById.getExchangeTime()));//设置导出数据的日期
        oeeMianInfoById.setToWeek(dateFm.format(oeeMianInfoById.getExchangeTime()));//设置导出数据的星期几
        list.add(oeeMianInfoById);
        List<Page> page = individual(list); // 一张表一个对象数据
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("机长交接班.xls", response);//返回前端处理
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


    public static void main(String[] args) {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        System.out.println(dateFm.format(new Date()));
    }
    @PostMapping("excelExport")
    public R excelExport(@RequestBody List<ExcelParam> excelParams, HttpServletResponse response) {
        /*String path = "C:/Users/Administrator/Desktop/excelExport/demo/";
        File file = new File(path);
        if(!file.exists()){//判断文件路径是否存在
            file.mkdirs();//不存在创建新的文件
        }
        String filePath = path + "机长交接班.xls";
        OutputStream os = null;*/
        // 模板位置，输出流
        String templatePath = "model/handoverSheetJSJ.xls";
        BufferedOutputStream bos = null;//导出到网页
        //插入到excel 的数据
        List<StatisMachOrderVo> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        for (ExcelParam excel : excelParams) {
            StatisMachOrderVo statisMachOrderVo = getOeeMianInfoById(Integer.parseInt(excel.getId()), Integer.parseInt(excel.getDpId()));
            statisMachOrderVo.setChangeTime(sdf.format(statisMachOrderVo.getExchangeTime()));//设置导出数据的日期
            statisMachOrderVo.setToWeek(dateFm.format(statisMachOrderVo.getExchangeTime()));//设置导出数据的星期几
            list.add(statisMachOrderVo);
        }
//        list =getDateForTest();//测试数据
//        List<Page> page = DataByPage.byPage(list);//一张表导多个对象数据
        List<Page> page = individual(list); // 一张表一个对象数据
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        //model.put("className", "六年三班");   //所有页面都相同数据的数据写在这里
        try {
//            os = new FileOutputStream(filePath);
            bos = ExportlUtil.getBufferedOutputStream("机长交接班.xls", response);//返回前端处理
//            JxlsUtils.exportExcel(templatePath, os, model);//导出到对应路径
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端处理
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

               /* if(os != null){
                    os.close();
                }*/
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 删除多出来的sheet  如果是返回给前端需要干掉 不需要
//        DelSheet.deleteSheet(filePath, "template");
        //return R.success("完成");
        return null;
    }

    /**
     * 将数据获取的数据封装成一页一个人的List
     */
    public static List<Page> individual(List<StatisMachOrderVo> list) {
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
//            p.setSheetName(list.get(i).getDpName()+"-"+list.get(i).getSfName()+"-"+(i+1));
            //设置sheet名称
            List<workBatchVo> workBatchLsit = list.get(i).getWorkBatchLsit();
            if (!workBatchLsit.isEmpty()) {
                String userName = workBatchLsit.get(0).getUserName();
                String machineName = workBatchLsit.get(0).getMachineName();
                String s = machineName.replace("*", "");
                p.setSheetName(s + "-" + userName + "-" + (i + 1));
            } else {
                p.setSheetName("班次oee" + i);
            }
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
     * PageOfMachsingle
     */
    @GetMapping("/PageOfMachsingle")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入PageOfMachsingle")
    public R<IPage<StatisMachsingleVO>> PageOfMachsingle(StatisMachsingleVO statisMachsingle, Query query) {
        IPage<StatisMachsingleVO> pages = statisMachsingleService.selectStatisMachsingleoEEPage(Condition.getPage(query), statisMachsingle);
        return R.data(pages);
    }

    /**
     * PageOfMachsingle
     */
    @GetMapping("/PageOfMachsinglexy")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入PageOfMachsingle")
    public R<IPage<StatisMachsingleVO>> PageOfMachsinglexy(StatisMachsingleVO statisMachsingle, Query query) {
        IPage<StatisMachsingleVO> pages = statisMachsingleService.PageOfMachsinglexy(Condition.getPage(query), statisMachsingle);
        return R.data(pages);
    }

    /**
     * 重新算
     */
    @GetMapping("/recaculate")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisMachsingle")
    public R recaculate(@ApiParam(value = "主键", required = true) @RequestParam String id) {
        try {
            String[] list = id.split(",");
            if (list.length > 0)
                for (String str : list) {
                    StatisMachoee statisMachoee = iStatisMachoeeService.getById(str);
                    Integer maId = statisMachoee.getMaId();
                    String oeDate = statisMachoee.getOeDate();//oee日期
                    Integer wsId = statisMachoee.getWsId();//班次id
                    Integer userId = statisMachoee.getUsId();
                    Date systemDate = statisMachoee.getCreateAt();
                    WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(wsId, maId);

                    statisLeanOeeController.setMachoee(workbatchShiftset,maId, oeDate, wsId);//oee重新算
                    //statisMachsingleService.generateMachineOeeReport(userId, maId, systemDate, Integer.parseInt(str), statisMachoee.getExId());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
