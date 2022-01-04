package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.yb.barcodeUtils.PDFUtils;
import com.yb.common.DateUtil;
import com.yb.execute.vo.TakeStockVO;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkbatchShiftServiceImpl extends ServiceImpl<WorkbatchShiftMapper, WorkbatchShift> implements WorkbatchShiftService {

    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;

    @Override
    public Boolean shiftLockSort(List<Integer> ids, Integer wfsortIslock) {
        Integer lockSort = workbatchShiftMapper.shiftLockSort(ids, wfsortIslock);
        return SqlHelper.retBool(lockSort);
    }

    @Override
    public List<WorkbatchShiftVO> todyShift(Integer shiftStatus) {
        return workbatchShiftMapper.todyShift(shiftStatus);
    }

    @Override
    public List<WorkbatchShift> workbatchShiftBysdDate(String sdDate) {
        String yesSdDate = DateUtil.toDatestr(DateUtil.addDayForDate(DateUtil.toDate(sdDate,"yyyy-MM-dd"), -1), "yyyy-MM-dd");
        System.out.println("yesDate" + yesSdDate);
        String beforSdDate = DateUtil.toDatestr(DateUtil.addDayForDate(DateUtil.toDate(sdDate,"yyyy-MM-dd"), -2), "yyyy-MM-dd");
        System.out.println("beforSdDate" + beforSdDate);
        System.out.println("sdDate" + sdDate);
        return workbatchShiftMapper.workbatchShiftBysdDate(sdDate, yesSdDate, beforSdDate);
    }

    @Override
    public int setFinishNum(Integer wfid,Integer finishnum){
        return workbatchShiftMapper.setFinishNum(wfid, finishnum);
    }

    @Override
    public List<TakeStockVO> selectByIds(IPage<TakeStockVO> page, List<Integer> wfIds, String wbNo, Integer maId, Integer prId, Integer exStatus) {
        return workbatchShiftMapper.selectByIds(page, wfIds, wbNo, maId, prId, exStatus);
    }
    @Override
    public WorkbatchShiftDataVO getShiftData(Integer wfId){
        return workbatchShiftMapper.getShiftData(wfId);
    }
}

