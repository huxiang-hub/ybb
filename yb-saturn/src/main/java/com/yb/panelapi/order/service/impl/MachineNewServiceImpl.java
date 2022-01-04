package com.yb.panelapi.order.service.impl;

import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.panelapi.order.service.MachineNewService;
import com.yb.panelapi.request.MachineStopListRequest;
import com.yb.panelapi.request.WeekHourRateLossRequest;
import com.yb.panelapi.vo.HourRateLossStatisticsVO;
import com.yb.panelapi.vo.MachineStopListVO;
import com.yb.panelapi.vo.MachineStopVO;
import com.yb.statis.mapper.StatisReachremarkMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.supervise.mapper.SuperviseIntervalMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 机台新service
 * @Author my
 * @Date Created in 2020/8/12 20:06
 */
@Service
public class MachineNewServiceImpl implements MachineNewService {

    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;

    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private StatisReachremarkMapper statisReachremarkMapper;
    @Autowired
    private SuperviseIntervalMapper superviseIntervalMapper;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;

    @Override
    public Integer getShiftLastOrder(Integer maId, Integer wsId) {
        return executeBrieferMapper.getShiftLastOrder(maId, wsId);
    }

    @Override
    public List<HourRateLossStatisticsVO> hourRateLossStatistics(WeekHourRateLossRequest request) {
        if (StringUtils.isBlank(request.getStartTime()) && StringUtils.isBlank(request.getEndTime())) {
            //默认设置为本周的时间
            Date beginDayOfWeek = DateTimeUtil.getBeginDayOfWeek();
            Date endDayOfWeek = DateTimeUtil.getEndDayOfWeek();
            request.setStartTime(DateTimeUtil.format(beginDayOfWeek, DateTimeUtil.DEFAULT_DATE_FORMATTER));
            request.setEndTime(DateTimeUtil.format(endDayOfWeek, DateTimeUtil.DEFAULT_DATE_FORMATTER));
        }
        List<HourRateLossStatisticsVO> vos = statisReachremarkMapper.hourRateLossStatistics(request);
        return vos;
    }

    /**
     * 机台停机列表
     *
     * @param request
     * @return
     */
    @Override
    public MachineStopVO stopList(MachineStopListRequest request) {
        //拼接时间条件
        String condition = request.getOperator().getType() + request.getTime();
        List<MachineStopListVO> machineStopListVOS = superviseIntervalMapper.stopList(request, condition);
        //获取工单号和客户名
        MachineStopVO machineStopVO = superviseExecuteMapper.getByMaId(request.getMaId());
        machineStopVO = machineStopVO == null ? new MachineStopVO() : machineStopVO;
        machineStopVO.setMachineStopListVOS(machineStopListVOS);

        return machineStopVO;
    }
}
