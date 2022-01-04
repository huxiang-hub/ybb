package com.yb.statis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.statis.entity.StatisMachreach;
import com.yb.statis.request.StatisMachreachListRequest;
import com.yb.statis.request.HourPlanRateRequest;
import com.yb.statis.vo.*;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;

import java.util.List;

public interface IStatisMachreachService extends IService<StatisMachreach> {

    IPage<StatisMachreach> selectStatisMachreach(IPage<StatisMachreach> page, StatisMachreachVO statisMachreach);

    IPage<StatisMachreachVO> selectOne(Integer id);

    void createStatisMachreachObj(Integer Id);

    void updateStatisMachreachObj(Integer id, WorkbatchOrdlink ordlink, WorkbatchShift workbatchShift);

    IPage<StatisMachreachListVO> planRate(IPage<StatisMachreachListVO> page, StatisMachreachListRequest request);

    List<MonthStatisMachreachListVO> monthPlanRate(String maType, Integer wsId);

    List<DayStatisMachreachListVO> hourPlanRate(HourPlanRateRequest request);


    /**
     * 班次达成率插入
     *
     * @param Id
     * @param wshift
     * @param id
     * @param maId
     */
    void createShiftStatisMachreach(Integer Id, WorkbatchShift wshift, Integer id, Integer maId);
}
