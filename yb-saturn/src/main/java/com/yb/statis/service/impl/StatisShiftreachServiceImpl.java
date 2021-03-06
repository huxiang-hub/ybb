package com.yb.statis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.entity.StatisReachremark;
import com.yb.statis.entity.StatisShiftreach;
import com.yb.statis.entity.StatisShiftreachext;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.statis.excelUtils.Page;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.statis.mapper.StatisReachremarkMapper;
import com.yb.statis.mapper.StatisShiftreachMapper;
import com.yb.statis.mapper.StatisShiftreachextMapper;
import com.yb.statis.service.StatisShiftreachService;
import com.yb.statis.utils.AddImageUtils;
import com.yb.statis.vo.*;
import com.yb.system.dict.mapper.SaDictMapper;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.mapper.SaUserMapper;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchMainshiftService;
import com.yb.workbatch.vo.SquareVO;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisShiftreachServiceImpl extends ServiceImpl<StatisShiftreachMapper, StatisShiftreach> implements StatisShiftreachService {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private StatisShiftreachMapper statisShiftreachMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private StatisReachremarkMapper statisReachremarkMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private SaUserMapper saUserMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private StatisShiftreachextMapper statisShiftreachextMapper;
    @Autowired
    private SaDictMapper saDictMapper;
    @Autowired
    private IWorkbatchMainshiftService workbatchMainshiftService;


    @Override
    public IPage<StatisShiftreachVO> statisShiftreachList(IPage<StatisShiftreachVO> page, StatisShiftreachVO statisShiftreachVO) {
        List<StatisShiftreachVO> statisShiftreachList = statisShiftreachMapper.statisShiftreachList(page, statisShiftreachVO);
        return page.setRecords(statisShiftreachList);
    }


    @Scheduled(cron = "0 2 8,20 * * ?")
    public void timedTaskStatisShiftreach() {
        List<String> stringList = new ArrayList<>();
        stringList.add("xingyi");
        stringList.add("nxhr");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String wsFormat = sdf.format(new Date(date.getTime() - 1000 * 60 * 30));//???30??????
        String targetDay;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        Integer h = Integer.valueOf(simpleDateFormat.format(date));
        if (h < 12) {//?????????????????????12??????????????????,??????????????????
            Date toDay = DateUtil.addDayForDate(date, -1);
            targetDay = DateUtil.refNowDay(toDay);
        } else {//?????????????????????,????????????oee
            targetDay = DateUtil.refNowDay();
        }
        for (String s : stringList) {
            DBIdentifier.setProjectCode(s);
            List<Integer> maIds = statisOrdreachMapper.getMaidsByTargetDay(targetDay);
            for (Integer maId : maIds) {
                /*??????????????????????????????????????????*/
                WorkbatchMachShiftVO workbatchMachShiftVO = workbatchShiftMapper.findByMaIdWsTime(maId, wsFormat);
                if (workbatchMachShiftVO != null) {
                    Integer wsId = workbatchMachShiftVO.getId();//??????id
                    addStatisShiftreach(maId, wsId, targetDay);
                }
            }
        }

    }

    @Override
    public boolean timedTaskStatisShiftreach(String targetDay, Integer wsId) {
        try {
            statisShiftreachMapper.delete(new QueryWrapper<StatisShiftreach>()
                    .eq("target_day", targetDay).eq("ws_id", wsId));
            List<Integer> maIds = statisOrdreachMapper.getMaidsByTargetDay(targetDay);
            for (Integer maId : maIds) {
                addStatisShiftreach(maId, wsId, targetDay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<ShiftreachListVO> getShiftreachList(GetShiftreachVO getShiftreachVO) {
        List<Integer> maIdList = getShiftreachVO.getMaIdList();
        if (maIdList == null || maIdList.isEmpty()) {
            return null;
        }
        String startTime = getShiftreachVO.getStartTime();
        if (StringUtil.isEmpty(startTime)) {
            startTime = DateUtil.refNowDay();
            getShiftreachVO.setStartTime(startTime);
        }
        /*??????????????????*/
//        else{
//            if(startTime.equalsIgnoreCase(getShiftreachVO.getEndTime())){
//                //?????????????????????????????????????????????????????????????????????1???
//                String endTime = DateUtil.toDatestr(DateUtil.addDayForDate(DateUtil.toDate(startTime,"yyyy-MM-dd"),1),"yyyy-MM-dd") ;
//                getShiftreachVO.setEndTime(endTime);
//            }
//        }
        /*????????????????????????(?????????,??????,????????????)*/
        List<ShiftreachListVO> shiftreachList = statisShiftreachMapper.getShiftreachList(getShiftreachVO);
        /*???????????????????????????(?????????,??????,????????????)*/
        List<ShiftreachListVO> planShiftreachList = workbatchShiftMapper.getPlanShiftreachList(getShiftreachVO);
        Iterator<ShiftreachListVO> iterator = planShiftreachList.iterator();
        while (iterator.hasNext()) {
            ShiftreachListVO shiftreachVO = iterator.next();
            Integer voWsId = shiftreachVO.getWsId();
            Integer voMaId = shiftreachVO.getMaId();
            String voTargetDate = shiftreachVO.getTargetDate();
            for (ShiftreachListVO shiftreachListVO : shiftreachList) {
                Integer wsId = shiftreachListVO.getWsId();
                Integer maId = shiftreachListVO.getMaId();
                String targetDate = shiftreachListVO.getTargetDate();
                if (wsId.equals(voWsId) && maId.equals(voMaId) && targetDate.equals(voTargetDate)) {
                    Integer planNum = shiftreachVO.getPlanNum();
                    shiftreachListVO.setPlanNum(planNum);
                    if (planNum != null && planNum != 0) {
                        Integer countNum = shiftreachListVO.getCountNum();
                        shiftreachListVO.setRateNum((double) countNum / planNum);
                        shiftreachListVO.setWsName(shiftreachVO.getWsName());
                    }
                    iterator.remove();
                }
            }
        }
        if (!planShiftreachList.isEmpty()) {
            shiftreachList.addAll(planShiftreachList);
        }
        return shiftreachList;
    }

    @Override
    public Integer shiftreachExcelExport(GetShiftreachVO getShiftreachVO) {
        List<Map<String, Object>> exportData = new ArrayList<>();
        AddImageUtils addImageUtils = new AddImageUtils();
        String targertDate = getShiftreachVO.getEndTime() == null ? getShiftreachVO.getStartTime() : (getShiftreachVO.getStartTime().equals(getShiftreachVO.getEndTime()) ? getShiftreachVO.getStartTime() : getShiftreachVO.getStartTime() + "-" + getShiftreachVO.getEndTime());
        String tableHeard = "????????????????????????????????????" + targertDate + "???";
        List<ShiftreachListVO> shiftreachList = getShiftreachList(getShiftreachVO);
        Map<String, List<ShiftreachListVO>> maNameMap = shiftreachList.stream().collect(Collectors.groupingBy(ShiftreachListVO::getMaName));
        maNameMap.forEach((key, value) -> {
            Map<String, List<ShiftreachListVO>> wsNameMap = value.stream().filter(a -> null != a.getWsName()).collect(Collectors.groupingBy(ShiftreachListVO::getWsName));
            List<ShiftreachListVO> shiftreachListVOS = new ArrayList<>();
            wsNameMap.forEach((k, v) -> {
                int planNumSum = v.stream().filter(a -> a.getPlanNum() != null).mapToInt(ShiftreachListVO::getPlanNum).sum();
                int productNumSum = v.stream().filter(a -> a.getProductNum() != null).mapToInt(ShiftreachListVO::getProductNum).sum();
                int countNumSum = v.stream().filter(a -> a.getCountNum() != null).mapToInt(ShiftreachListVO::getCountNum).sum();
                int wasteNumSum = v.stream().filter(a -> a.getWasteNum() != null).mapToInt(ShiftreachListVO::getWasteNum).sum();
                List<String> usNames = v.stream().filter(a -> a.getUsName() != null).map(ShiftreachListVO::getUsName).collect(Collectors.toList());
                String usNameAll = usNames.stream().distinct().collect(Collectors.joining(","));
                ShiftreachListVO s = new ShiftreachListVO();
                s.setMaName(key);
                s.setTargetDate(getShiftreachVO.getEndTime() == null ? getShiftreachVO.getStartTime() : getShiftreachVO.getStartTime() + "-" + getShiftreachVO.getEndTime());
                s.setWsName(k);
                s.setUsName(usNameAll);
                s.setPlanNum(planNumSum);
                s.setProductNum(productNumSum);
                s.setCountNum(countNumSum);
                s.setWasteNum(wasteNumSum);
                s.setRateNum((double) countNumSum / planNumSum);
                s.setWasteRate((double) wasteNumSum / productNumSum);
                s.setImage(addImageUtils.addImage(s.getRateNum()));
                shiftreachListVOS.add(s);
            });
            Map<String, Object> m = new HashMap<>();
            m.put("machine", key);
            m.put("shift", shiftreachListVOS);
            exportData.add(m);
        });
        Map<String, List<ShiftreachListVO>> wsNameSumMap = shiftreachList.stream().filter(a -> a.getWsName() != null).collect(Collectors.groupingBy(ShiftreachListVO::getWsName));
        List<Map<String, Object>> wsNameSumList = new ArrayList<>();
        wsNameSumMap.forEach((k, v) -> {
            HashMap<String, Object> wsSum = new HashMap<>();
            int planNumWsSum = v.stream().filter(a -> a.getPlanNum() != null).mapToInt(ShiftreachListVO::getPlanNum).sum();
            int productNumWsSum = v.stream().filter(a -> a.getProductNum() != null).mapToInt(ShiftreachListVO::getProductNum).sum();
            int countNumWsSum = v.stream().filter(a -> a.getCountNum() != null).mapToInt(ShiftreachListVO::getCountNum).sum();
            int wasteNumWsSum = v.stream().filter(a -> a.getWasteNum() != null).mapToInt(ShiftreachListVO::getWasteNum).sum();
            double rateNumWs = ((double) countNumWsSum / planNumWsSum);
            double wasteRateWs = ((double) wasteNumWsSum / productNumWsSum);
            wsSum.put("wsName", k + "??????");
            wsSum.put("planNumWsSum", planNumWsSum);
            wsSum.put("productNumWsSum", productNumWsSum);
            wsSum.put("countNumWsSum", countNumWsSum);
            wsSum.put("wasteNumWsSum", wasteNumWsSum);
            wsSum.put("rateNumWs", rateNumWs);
            wsSum.put("wasteRateWs", wasteRateWs);
            wsNameSumList.add(wsSum);
        });
        int planNumAllSum = shiftreachList.stream().filter(a -> a.getPlanNum() != null).mapToInt(ShiftreachListVO::getPlanNum).sum();
        int productNumAllSum = shiftreachList.stream().filter(a -> a.getProductNum() != null).mapToInt(ShiftreachListVO::getProductNum).sum();
        int countNumAllSum = shiftreachList.stream().filter(a -> a.getCountNum() != null).mapToInt(ShiftreachListVO::getCountNum).sum();
        int wasteNumAllSum = shiftreachList.stream().filter(a -> a.getWasteNum() != null).mapToInt(ShiftreachListVO::getWasteNum).sum();
        double rateNumAll = ((double) countNumAllSum / planNumAllSum);
        double wasteRateAll = ((double) wasteNumAllSum / productNumAllSum);
        HashMap<String, Object> sumAll = new HashMap<>();
        sumAll.put("planNumAllSum", planNumAllSum);
        sumAll.put("productNumAllSum", productNumAllSum);
        sumAll.put("countNumAllSum", countNumAllSum);
        sumAll.put("wasteNumAllSum", wasteNumAllSum);
        sumAll.put("rateNumAll", rateNumAll);
        sumAll.put("wasteRateAll", wasteRateAll);

        String templatePath = "model/shiftreach.xls";
        BufferedOutputStream bos = null;//???????????????

        Map<String, Object> model = new HashMap<>();
        model.put("pages", exportData);
        model.put("wsNameSumList", wsNameSumList);
        model.put("sumAll", sumAll);
        model.put("tableHeard", tableHeard);
        model.put("sheetNames", "???????????????");
        model.put("slName", "???????????????");
        try {
            bos = ExportlUtil.getBufferedOutputStream("???????????????.xls", response);//??????????????????
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//?????????????????????
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    @Override
    public List<ShiftreachListVO> getShiftreachTotal(GetShiftreachVO getShiftreachVO) {
        String startTime = getShiftreachVO.getStartTime();
        if (StringUtil.isEmpty(startTime)) {
            startTime = DateUtil.refNowDay();
            getShiftreachVO.setStartTime(startTime);
        }
        /*???????????????, ???????????????*/
        List<ShiftreachListVO> sumPlanNumList = workbatchShiftMapper.getWsSumPlanNum(getShiftreachVO);
        /*???????????????*/
        List<ShiftreachListVO> shiftreachTotal = statisShiftreachMapper.getShiftreachTotal(getShiftreachVO);
        for (ShiftreachListVO shiftreachListVO : shiftreachTotal) {
            shiftreachListVO.setWsName(shiftreachListVO.getWsName() + "??????");
            for (ShiftreachListVO sumPlanNum : sumPlanNumList) {
                if (sumPlanNum.getWsId().equals(shiftreachListVO.getWsId())) {
                    Integer planNum = sumPlanNum.getPlanNum();
                    if (planNum == null || planNum == 0) {
                        shiftreachListVO.setRateNum(0.0);
                    } else {
                        shiftreachListVO.setRateNum((double) shiftreachListVO.getCountNum() / planNum);
                    }
                    shiftreachListVO.setPlanNum(planNum);
                }
            }
        }
        /*???????????????*/
        ShiftreachListVO SumPlanNum = workbatchShiftMapper.getSumPlanNum(getShiftreachVO);
        ShiftreachListVO Total = statisShiftreachMapper.getTotal(getShiftreachVO);
        if (SumPlanNum != null) {
            Integer planNum = SumPlanNum.getPlanNum();
            if (planNum == null || planNum == 0) {
                Total.setRateNum(0.0);
            } else {
                Total.setRateNum((double) Total.getCountNum() / planNum);
            }
        }
        Total.setWsName("?????????");
        shiftreachTotal.add(Total);
        return shiftreachTotal;
    }


    @Transactional(rollbackFor = Exception.class)
    public void addStatisShiftreach(Integer maId, Integer wsId, String targetDay) {
        List<StatisOrdreach> statisOrdreachList = statisOrdreachMapper.statisOrdInitList(targetDay, wsId, maId);
        if (!statisOrdreachList.isEmpty()) {
            /*??????????????????????????????*/
            List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>()
                    .eq("ws_id", wsId)
                    .eq("ma_id", maId)
                    .eq("sd_date", targetDay)
                    .ne("plan_type", "1")
                    .isNotNull("plan_type")
                    .ne("shift_status", -1));

            Integer planstopTime = 0;//??????????????????
            Integer maintainTime = 0;//??????????????????
            StatisShiftreach statisShiftreach = new StatisShiftreach();
            for (WorkbatchShift workbatchShift : workbatchShiftList) {
                Integer planTotalTime = workbatchShift.getPlanTotalTime() == null ? 0 : workbatchShift.getPlanTotalTime();
                String planType = workbatchShift.getPlanType();
                switch (planType) {//2????????????B-B2???3????????????C-C2
                    case "2": {
                        maintainTime += planTotalTime;
                        break;
                    }
                    case "3": {
                        planstopTime += planTotalTime;
                        break;
                    }
                }
            }
            /*?????????????????????*/
            Integer usId = null;
            Integer squareNum = 0;//?????????
            List<SquareVO> workbatchOrdlinkVOList = workbatchOrdlinkMapper.selectSquareList(maId, wsId, targetDay);
            Set<Integer> wfSet = new HashSet();
            for (SquareVO squareVO : workbatchOrdlinkVOList) {
                Integer wfId = squareVO.getWfId();//????????????id
                if (wfId != null) {
                    wfSet.add(wfId);
                }
                usId = squareVO.getHandleUsid();//?????????id
                statisShiftreach.setUsId(usId);
                String operateSize = squareVO.getOperateSize();//????????????
                if (!StringUtil.isEmpty(operateSize)) {
                    Integer productNum = squareVO.getProductNum() == null ? 0 : squareVO.getProductNum();//???????????????
                    String[] split = operateSize.split("\\*");
                    Integer width = null;
                    Integer length = null;
                    if (split.length > 1) {
                        width = Integer.valueOf(split[0]);
                        length = Integer.valueOf(split[1]);
                    }
                    width = width == null ? 0 : width;
                    length = length == null ? 0 : length;
                    Integer square = (int) ((double) width * length * productNum / 1000000);
                    squareNum += square;
                }
            }
            if (usId != null) {
                /*??????????????????*/
                SaUser saUser = saUserMapper.selectById(usId);
                if (saUser != null) {
                    statisShiftreach.setUsName(saUser.getName());
                }
            }
            if (!statisOrdreachList.isEmpty()) {
                StatisOrdreach ordreach = statisOrdreachList.get(0);
                String maName = ordreach.getMaName();//????????????
                String wsName = ordreach.getWsName();//????????????
                statisShiftreach.setMaId(maId);
                statisShiftreach.setMaName(maName);
                statisShiftreach.setWsId(wsId);
                statisShiftreach.setWsCkname(wsName);
                statisShiftreach.setTargetDay(targetDay);//????????????
                Integer planCount = 0;//???????????????
                Integer realCount = 0;//???????????????
                Integer repairTime = 0;//????????????
                for (StatisOrdreach statisOrdreach : statisOrdreachList) {
                    Integer srId = statisOrdreach.getId();
                    Integer planNum = statisOrdreach.getPlanCount();
                    planNum = planNum == null ? 0 : planNum;
                    planCount += planNum;//?????????
                    Integer realNum = statisOrdreach.getRealCount();
                    realNum = realNum == null ? 0 : realNum;
                    realCount += realNum;//?????????
                    /*??????????????????*/
                    StatisReachremark reachremark =
                            statisReachremarkMapper.selectOne(new QueryWrapper<StatisReachremark>().eq("sr_id", srId));
                    if (reachremark == null) {
                        continue;
                    }
//                    Integer manageStopTime = reachremark.getManageStopTime() == null ? 0 : reachremark.getManageStopTime();//??????????????????
//                    Integer otherLossTime = reachremark.getOtherLossTime() == null ? 0 : reachremark.getOtherLossTime();//??????????????????(??????)
//                    Integer proReadyTime = reachremark.getProReadyTime() == null ? 0 : reachremark.getProReadyTime();//??????????????????
//                    Integer qualityTestTime = reachremark.getQualityTestTime() == null ? 0 : reachremark.getQualityTestTime();//??????????????????
//                    Integer typeSwitchTime = reachremark.getTypeSwitchTime() == null ? 0 : reachremark.getTypeSwitchTime();//??????????????????
//                    String otherLossCause = reachremark.getOtherLossCause();
                    Integer deviceFaultTime = reachremark.getDeviceFaultTime() == null ? 0 : reachremark.getDeviceFaultTime();//??????????????????
                    repairTime += deviceFaultTime;
                }
                Double rateNum = 1.0;
                if (planCount != null && planCount != 0) {
                    rateNum = realCount / (double) planCount;
                }/*else {
                    rateNum = 1.0;//?????????????????????0,???????????????100%
                }*/
                Date date = new Date();
                statisShiftreach.setPlanCount(planCount);
                statisShiftreach.setRealCount(realCount);
                statisShiftreach.setRateNum(rateNum);
                statisShiftreach.setCreateAt(date);
                statisShiftreach.setUpdateAt(date);
                statisShiftreach.setSquareNum(squareNum);//???????????????2???
                statisShiftreach.setRepairTime(repairTime);//????????????
                statisShiftreach.setMaintainTime(maintainTime);//????????????????????????
                statisShiftreach.setPlanstopTime(planstopTime);//??????????????????
//            statisShiftreach.setRemark();
                statisShiftreachMapper.insert(statisShiftreach);

                /*??????????????????????????????*/
                StatisShiftreachext statisShiftreachext = new StatisShiftreachext();
                Integer ordNum = wfSet.size();//?????????
                statisShiftreachext.setCreateAt(date);
                statisShiftreachext.setUpdateAt(date);
                MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
                Integer maType = null;
                if (machineMainfo != null) {
                    maType = Integer.valueOf(machineMainfo.getMaType());
                }
                statisShiftreachext.setSfId(statisShiftreach.getId());
                statisShiftreachext.setMaType(maType);//????????????
                statisShiftreachext.setOrdNum(ordNum);//?????????
                statisShiftreachextMapper.insert(statisShiftreachext);
            }
        }

    }

    /**
     * ??????????????????
     *
     * @param statisShiftreachList
     * @return
     */
    private List<StatisShiftreachVO> addImages(List<StatisShiftreachVO> statisShiftreachList) {

        for (StatisShiftreachVO StatisShiftreachVO : statisShiftreachList) {
            /*?????????*/
            Double rateNum = StatisShiftreachVO.getRateNum();
            AddImageUtils addImageUtils = new AddImageUtils();
            byte[] bytes = addImageUtils.addImage(rateNum);
            StatisShiftreachVO.setImage(bytes);
        }
        return statisShiftreachList;
    }

    /**
     * ????????????????????????
     *
     * @param statisShiftreachList
     * @return
     */
    private List<StatisShiftreachExcelExportVO> statisShiftreachExcel(List<StatisShiftreachVO> statisShiftreachList) {
        List<StatisShiftreachExcelExportVO> statisShiftreachExcelExportVOList = new ArrayList<>();
        Map<String, List<StatisShiftreachVO>> statisShiftreachVOMap = new HashMap<>();
        List<StatisShiftreachVO> statisShiftreachVOS;
        String targetDay = null;
        for (StatisShiftreachVO statisShiftreachVO : statisShiftreachList) {
            String maType = statisShiftreachVO.getMaType().toString();//????????????
            targetDay = statisShiftreachVO.getTargetDay();//??????
            if (statisShiftreachVOMap.containsKey(maType)) {//???Map????????????,??????????????????key
                statisShiftreachVOMap.get(maType).add(statisShiftreachVO);
            } else {
                statisShiftreachVOS = new ArrayList<>();
                statisShiftreachVOS.add(statisShiftreachVO);
                statisShiftreachVOMap.put(maType, statisShiftreachVOS);
            }
        }
        AddImageUtils addImageUtils = new AddImageUtils();
        for (String key : statisShiftreachVOMap.keySet()) {//?????????key????????????
            List<StatisShiftreachVO> shiftreachVOList = statisShiftreachVOMap.get(key);
            /*??????????????????*/
            String maTypeValue = saDictMapper.getValue("maType", Integer.valueOf(key));
            StatisShiftreachExcelExportVO statisShiftreachExcelExportVO = new StatisShiftreachExcelExportVO();
            statisShiftreachExcelExportVO.setReachImageVO(addImageUtils.saveImageReach());
            statisShiftreachExcelExportVO.setMaType(key);
            statisShiftreachExcelExportVO.setMaTypeValue(maTypeValue);
            statisShiftreachExcelExportVO.setTargetDay(targetDay);
            statisShiftreachExcelExportVO.setStatisShiftreachVOList(shiftreachVOList);
            statisShiftreachExcelExportVOList.add(statisShiftreachExcelExportVO);
        }
        return statisShiftreachExcelExportVOList;
    }

    /**
     * ??????excel
     */
    @Override
    public Integer statisShiftreachExcelExport(String targetDay, Integer maType, Integer wsId) {
        if (StringUtil.isEmpty(targetDay)) {
            targetDay = DateUtil.refNowDay();
        }
        /*???????????????????????????*/
        List<StatisShiftreachVO> statisShiftreachList = statisShiftreachMapper.selectExcelExport(targetDay, maType, wsId);
        if (statisShiftreachList.isEmpty()) {
            return null;
        }
        List<StatisShiftreachVO> statisShiftreachVOS = addImages(statisShiftreachList);//????????????
        /*????????????????????????*/
        List<StatisShiftreachExcelExportVO> statisShiftreachExcelExportVOList = statisShiftreachExcel(statisShiftreachVOS);
        String templatePath = "model/capacityModel.xls";
        BufferedOutputStream bos = null;//???????????????
        List<Page> page = individual(statisShiftreachExcelExportVOList); // ???????????????????????????
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("??????????????????.xls", response);//??????????????????
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//?????????????????????
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }


    public static List<Page> individual(List<StatisShiftreachExcelExportVO> list) {
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
            p.setSheetName(list.get(i).getMaTypeValue());
            //??????sheet??????
            pages.add(p);
        }

        return pages;
    }

    public static List<Page> individual2(List<ShiftreachListVO> list) {
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
            p.setSheetName("???????????????");
            //??????sheet??????
            pages.add(p);
        }

        return pages;
    }

    /**
     * Excel ?????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????
     */
    public static ArrayList<String> getSheetName(List<Page> page) {
        ArrayList<String> al = new ArrayList<String>();
        for (int i = 0; i < page.size(); i++) {
            al.add(page.get(i).getSheetName());
        }
        return al;
    }
}
