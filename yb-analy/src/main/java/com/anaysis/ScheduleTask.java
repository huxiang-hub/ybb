package com.anaysis;

import com.anaysis.common.DateTimeUtil;
import com.anaysis.common.DateUtil;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.dynamicData.dbmgr.ProjectDBMgr;
import com.anaysis.executSupervise.entity.SuperviseBoxclean;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.entity.SuperviseRegular;
import com.anaysis.executSupervise.mapper.*;
import com.anaysis.executSupervise.service.SuperviseBoxinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Resource
    private SuperviseBoxinfoService boxInfoService;

    @Resource
    private SuperviseRegularMapper superviseRegularMapper;

    @Resource
    private SuperviseBoxcleanMapper superviseBoxcleanMapper;

    @Resource
    private SuperviseExecuteMapper superviseExecuteMapper;

    @Resource
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;

    @Resource
    private SuperviseShiftcountMapper superviseShiftcountMapper;

    //@Scheduled(cron = "0 0 6 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ? ")
    private void configureTasks() {
        ProjectDBMgr.instance();
        Map<String, String> dbNameMap = ProjectDBMgr.getDbNameMap();
        dbNameMap.forEach((k, v) -> {
            DBIdentifier.setProjectCode(k);
            long curTime = System.currentTimeMillis();
            Date date = new Date();
            List<SuperviseBoxinfo> list = boxInfoService.getList();
            if (!list.isEmpty()) {
                log.info("执行静态定时任务时间--star" + LocalDateTime.now() + ":::清零设备台数:" + ((list != null && list.size() > 0) ? list.size() : 0));
                for (SuperviseBoxinfo boxInfoEntity : list) {
                    //重置当日计数为0
                    //离线状态才进行清零操作。
                    if (boxInfoEntity != null) {
                        //获取盒子实时表信息
                        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecutByBno(boxInfoEntity.getUuid());
                        if (superviseExecute != null) {
                            if (superviseExecute.getStartNum() != null) {
                                superviseExecute.setStartNum(superviseExecute.getStartNum() - boxInfoEntity.getNumberOfDay());
                                superviseExecuteMapper.updateById(superviseExecute);
                            }
                        }
                        boxInfoEntity.setClearNum(boxInfoEntity.getNumberOfDay());
                        boxInfoEntity.setClearTime(date);
                        Integer numberOfDay = boxInfoEntity.getNumberOfDay();
                        //记录清零记录信息
                        clearInsert(boxInfoEntity, date, numberOfDay);
                        superviseBoxinfoMapper.clearZero(boxInfoEntity.getId(), numberOfDay);
                    }
                }
            }
        });
        log.info("执行静态定时任务时间--end:" + LocalDateTime.now());
    }

    private void clearInsert(SuperviseBoxinfo boxInfoEntity, Date curTime, Integer numberOfDay) {
        SuperviseBoxclean superviseBoxclean = new SuperviseBoxclean();
        superviseBoxclean.setCleanTime(curTime);
        superviseBoxclean.setUuid(boxInfoEntity.getUuid());
        superviseBoxclean.setNumber(boxInfoEntity.getNumber());
        superviseBoxclean.setNumberOfDay(numberOfDay);
        superviseBoxclean.setStatus(boxInfoEntity.getStatus());
        superviseBoxcleanMapper.insert(superviseBoxclean);
    }

    /**
     * 每15分钟检测设备状态保存
     */
    //@Scheduled(cron = "0 0,15,30,45 * * * ?")
    //@Scheduled(cron = "*/30 * * * * ?")
    public void task() {
        List<String> db = new ArrayList();
        log.info("开始执行设备状态检测定时器");
        ProjectDBMgr.instance();
        Map<String, String> dbNameMap = ProjectDBMgr.getDbNameMap();
        Date currTime = Date.from(Instant.now());
        dbNameMap.forEach((k, v) -> {
            DBIdentifier.setProjectCode(k);
            saveUpdateRegular(k, currTime);
        });
        log.info("设备状态检测定时器执行完成");
    }

    private void saveUpdateRegular(String k, Date currTime) {
        DBIdentifier.setProjectCode(k);
        List<SuperviseBoxinfo> list = boxInfoService.getList();
        if (!list.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // 放入Date类型数据
            int targetHour = calendar.get(Calendar.HOUR_OF_DAY); // 时（24小时制）
            int targetMin = calendar.get(Calendar.MINUTE); // 分
            if (targetMin > 55 || targetMin < 5) {
                targetMin = 0;
            }
            if (targetMin > 40 && targetMin < 50) {
                targetMin = 45;
            }
            if (targetMin > 25 && targetMin < 35) {
                targetMin = 30;
            }
            if (targetMin > 10 && targetMin < 20) {
                targetMin = 15;
            }
            for (SuperviseBoxinfo superviseBoxinfo : list) {
                //先寻找上一条记录
                SuperviseRegular oldLog = superviseRegularMapper.getPreviousRegularLog(superviseBoxinfo.getUuid());
                //修改上一条记录信息
                if (oldLog != null) {
                    oldLog.setEndTime(currTime);
                    int diffTime = DateUtil.calLastedTime(currTime.getTime(), oldLog.getStartTime());
                    oldLog.setDiffNum(diffTime);
                    //小于上一条
//                    if (startNum < oldLog.getStartNum()) {
//                        startNum = startNum + superviseBoxinfo.getClearNum();
//                    }
//                    oldLog.setEndNum(startNum);
                    // int pcount = startNum - oldLog.getStartNum();
                    //获取15分钟前的综合
                    int pcount = superviseShiftcountMapper.getSumByPcount(oldLog.getCreateAt(), currTime, superviseBoxinfo.getMaId());
                    oldLog.setPcout(pcount);
                    if (diffTime > 0) {
                        Double dspeed = (diffTime > 0) ? pcount / Double.valueOf(diffTime) : 0d;
                        oldLog.setCurrSpeed(dspeed);
                    } else {
                        oldLog.setCurrSpeed(0d);
                    }
                    oldLog.setUpdateAt(currTime);
                    superviseRegularMapper.updateById(oldLog);
                }
                //新增数据
                SuperviseRegular superviseRegular = new SuperviseRegular();
                superviseRegular.setCreateAt(currTime);
                superviseRegular.setUuid(superviseBoxinfo.getUuid());
                superviseRegular.setMaId(superviseBoxinfo.getMaId());
                superviseRegular.setStartNum(superviseBoxinfo.getNumberOfDay() == null ? 0 : superviseBoxinfo.getNumberOfDay());
                superviseRegular.setTargetDay(DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER));
                superviseRegular.setTargetHour(targetHour);
                superviseRegular.setTargetMin(targetMin);
                superviseRegular.setStartTime(currTime);
                superviseRegular.setStatus(Integer.valueOf(superviseBoxinfo.getStatus()));
                superviseRegularMapper.insert(superviseRegular);
            }
        }
    }
}

