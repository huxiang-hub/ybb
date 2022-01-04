package com.yb.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Configuration
@EnableScheduling
public class AutomaticStop {

    @Autowired
    public MachineMainfoMapper machineMainfoMapper;
    @Autowired
    public SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    public SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    public WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    public IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    public ExecuteBrieferMapper executeBrieferMapper;

    @Scheduled(cron = "0 0 8,20 * * ?")
    public void AutomaticStop(){

        List<String> stringList = new ArrayList<>();
//        stringList.add("xingyi");
        stringList.add("nxhr");
        for(String s : stringList) {
            DBIdentifier.setProjectCode(s);
            /*自动结束*/
            machineAutomaticStop();
        }
    }

    /**
     * 自动结束订单
     */
    public void machineAutomaticStop(){
        /*查询所有设备*/
//        List<MachineMainfo> machineMainfoList = machineMainfoMapper.selectList(new QueryWrapper<>());
        List<SuperviseBoxinfo> superviseBoxinfoList =
                superviseBoxinfoMapper.selectList(new QueryWrapper<SuperviseBoxinfo>()
                        .ne("ma_id", -1)
                        .isNotNull("ma_id"));
        List<String> exeStatusList = new ArrayList<>();
//        exeStatusList.add("B");
        exeStatusList.add("C");
        superviseBoxinfoList.forEach(e -> {
            Integer maId = e.getMaId();
            SuperviseExecute superviseExecute =
                    superviseExecuteMapper.selectOne(new QueryWrapper<SuperviseExecute>()
                            .eq("ma_id", maId)
                            .in("exe_status", exeStatusList));
            if(superviseExecute !=  null){
                Date startTime = superviseExecute.getStartTime();
                Date date = new Date();
                long hour = (date.getTime() - startTime.getTime()) / 1000 / 60;
                if(hour >=  60){
                    Integer wfId = superviseExecute.getWfId();
                    Integer sdId = superviseExecute.getSdId();
                    Integer exId = superviseExecute.getExId();//执行id
                    Integer operator = superviseExecute.getOperator();//机长
//                    SuperviseBoxinfo superviseBoxinfo =
//                            superviseBoxinfoMapper.selectOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));
                    String status = e.getStatus();//状态1运行2停机3故障4离线"
                    String projectCode = DBIdentifier.getProjectCode();
                    if("1".equals(status)){
                        MyThread myThread = new MyThread();
                        myThread.projectCode = projectCode;
                        myThread.exId = exId;
                        myThread.operator = operator;
                        myThread.wfId = wfId;
                        myThread.maId = maId;
                        myThread.sdId = sdId;
                        myThread.start();
                        return;
                    }
                    log.info("--------------maId : "+ maId +", 执行了了finishOrder方法----------------");
                    workbatchOrdlinkService.finishOrder(maId, sdId, operator, exId, wfId, 4);
                }
            }
        });
    }

    class MyThread extends Thread{
        String projectCode;
        Integer wfId;
        Integer sdId;
        Integer maId;
        Integer exId;
        Integer operator;//机长
        @SneakyThrows
        @Override
        public void run() {
            DBIdentifier.setProjectCode(projectCode);
            log.info("--------------maId:"+ maId +", 进入了run方法----------------");
            ExecuteBriefer briefer =
                    executeBrieferMapper.selectOne(new QueryWrapper<ExecuteBriefer>().eq("ex_id", exId));
            if(briefer != null){
                return;
            }
            SuperviseBoxinfo superviseBoxinfo =
                    superviseBoxinfoMapper.selectOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));
            String status = superviseBoxinfo.getStatus();//状态1运行2停机3故障4离线"
            if("1".equals(status)){
                sleep(60 * 1000);//睡眠1分钟
                run();
            }else {
                workbatchOrdlinkService.finishOrder(maId, sdId, operator, exId, wfId, 4);
                log.info("--------------maId:"+ maId +", 结束run方法----------------");
            }
        }
    }

}
