package com.yb.workbatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.vo.TakeStockVO;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import com.yb.workbatch.vo.WorkbatchShiftDataVO;
import com.yb.workbatch.vo.WorkbatchShiftVO;

import java.util.List;

public interface WorkbatchShiftService extends IService<WorkbatchShift> {

    /**
     * 锁定解锁顺序
     * @param ids
     * @param wfsortIslock
     * @return
     */
    Boolean shiftLockSort(List<Integer> ids, Integer wfsortIslock);

    List<WorkbatchShiftVO> todyShift(Integer shiftStatus);

    List<WorkbatchShift> workbatchShiftBysdDate(String sdDate);

    int setFinishNum(Integer wfid,Integer finishnum);

    List<TakeStockVO> selectByIds(IPage<TakeStockVO> page, List<Integer> wfIds, String wbNo, Integer maId, Integer prId, Integer exStatus);

   WorkbatchShiftDataVO getShiftData(Integer wfId);
}
