package com.yb.workbatch.task;

import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.service.impl.WorkbatchOrdlinkNewServiceImpl;
import com.yb.workbatch.vo.WorkbatchShiftsetNowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class WorkbatchTask {
    @Autowired
    private WorkbatchShiftService workbatchShiftService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;

    //@Scheduled(cron = "0 0/1 * * * ? ")
    public void workConfigTask() {
        //设置默认数据源
        DBIdentifier.setProjectCode("000000");
        //需要循环租户信息对象内容：
        String sdDate = DateUtil.refNowDay();

        //获取当天的全部排产单信息，进行循环匹配操作
        List<WorkbatchShift> wkshiftls = workbatchShiftService.workbatchShiftBysdDate(sdDate);
        List<WorkbatchMainshift> shiftWsids = iWorkbatchShiftsetService.getShiftWsid();
//        List<Integer> maIds ;
        //这里的业务逻辑没有考虑modle类型的当前仅仅考虑当前时间有的班次信息，并且执行group by方式操作。TODO暂时无需太复杂了。
        log.info("定时时间开始，进行排程单数据回收操作；---------------------------开始---star【" + sdDate + "】" + DateUtil.format(new Date()));
        if (wkshiftls != null && wkshiftls.size() > 0) {
            for (WorkbatchShift wkshift : wkshiftls) {
                //判断班次为包含的班次信息就执行待排产数进行回收。
                for (WorkbatchMainshift shift : shiftWsids) {
                    if (shift.getId() == wkshift.getWsId()) {
                        //提前把finishnum、complatenum数据进行更新修改，然后再进行状态更新
                        workbatchOrdlinkNewService.setOrdShift(wkshift.getId());
                        log.info("定时时间开始，执行修改数据操作；---------------------wsId：【" + wkshift.getId() + "】");
                        workbatchOrdlinkNewService.setWorksNum(wkshift.getSdId(), wkshift.getMaId(), "7");
                    }
                    //shiftWsids.stream().anyMatch(o -> o.getId().equals(wkshift.getWsId()))
                }
            }
        }
        log.info("定时时间开始，进行排程单数据回收操作；---------------------------结束---end【" + sdDate + "】" + ((wkshiftls != null) ? wkshiftls.size() : 0) + "----------------" + DateUtil.format(new Date()));
    }


}
