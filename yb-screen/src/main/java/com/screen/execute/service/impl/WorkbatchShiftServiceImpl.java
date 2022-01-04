package com.screen.execute.service.impl;

import com.screen.common.DateUtil;
import com.screen.execute.mapper.WorkbatchShiftMapper;
import com.screen.execute.service.WorkbatchShiftService;
import com.screen.execute.vo.WorkbatchOrdlinkVO;
import com.screen.execute.vo.WorkbatchShiftProcessVO;
import com.screen.execute.vo.WorkbatchShiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorkbatchShiftServiceImpl implements WorkbatchShiftService {

    @Autowired
    WorkbatchShiftMapper workbatchshift;

    @Override
    public List<WorkbatchShiftVO> getWorkShiftList(Integer maId) {
        //默认前后三天的数据信息
        String today = DateUtil.refNowDay();
        String startDate = DateUtil.refNowDay(DateUtil.addDayForDate(new Date(), -1));
        String endDate = today;
        List<WorkbatchShiftVO> wklist = workbatchshift.getWorkShiftList(maId, startDate, endDate);
        return wklist;
    }

    @Override
    public WorkbatchShiftProcessVO getShiftDetail(Integer wfId) {
        WorkbatchOrdlinkVO upProcess = workbatchshift.getUpProcess(wfId);

        WorkbatchShiftProcessVO wkprocess = workbatchshift.getProcessDetail(wfId);
        if (wkprocess == null || wkprocess.getWfId() == null) {
            wkprocess = new WorkbatchShiftProcessVO();
        }
        //判断能够获得上一道工序的内容信息
        if (upProcess != null && upProcess.getId() != null) {
            wkprocess.setUpPrId(upProcess.getPrId());//上一道工序id
            wkprocess.setUpProceName(upProcess.getPrName());//上一道工序名称
            wkprocess.setUpPrFinishNum(upProcess.getCompleteNum());//上一道完成的数量
        }

        return wkprocess;
    }
}
