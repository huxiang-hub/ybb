package com.yb.panelapi.order.service;

import com.yb.panelapi.request.MachineStopListRequest;
import com.yb.panelapi.request.WeekHourRateLossRequest;
import com.yb.panelapi.vo.HourRateLossStatisticsVO;
import com.yb.panelapi.vo.MachineStopListVO;
import com.yb.panelapi.vo.MachineStopVO;

import java.util.List;

/**
 * @Description: 机台新接口
 * @Author my
 * @Date Created in 2020/8/12 19:26
 */
public interface MachineNewService {

    /**
     * 获取当前设备最后一个生产上报的
     *
     * @param maId
     * @return
     */
    Integer getShiftLastOrder(Integer maId, Integer wsId);

    /**
     * 小时达成率损耗分析统计
     *
     * @param request
     * @return
     */
    List<HourRateLossStatisticsVO> hourRateLossStatistics(WeekHourRateLossRequest request);


    /**
     * 机台停机列表
     * @param request
     * @return
     */
    MachineStopVO stopList(MachineStopListRequest request);

}
