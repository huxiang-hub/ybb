package com.yb.machine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.execute.vo.TraycardMaterialsVO;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.request.OrderDetailRequest;
import com.yb.machine.request.OrderProcessScheduleRequest;
import com.yb.machine.request.ProcessFlowPageRequest;
import com.yb.machine.response.MachineReportVO;
import com.yb.machine.response.OrderDetailVO;
import com.yb.machine.response.OrderProcessScheduleVO;
import com.yb.machine.response.ProcessFlowVO;

import java.util.List;

import java.util.List;

/**
 * @Description: 设备报表servise
 * @Author my
 */
public interface MachineReportService {

    MachineReportVO machineReport(MachineReportRequest request);

    /**
     * 根据wfId查询托盘及上料相关信息
     * @param wfId 排产工单id
     * @return
     */
    List<TraycardMaterialsVO> getTraycardMaterials(Integer wfId);


    /**
     * 自定义分页
     *
     * @param page
     * @param demo
     * @return
     */
    IPage<ProcessFlowVO> page(IPage<ProcessFlowVO> page, ProcessFlowPageRequest demo);

    /**
     * 工单详情
     * @param request
     * @return
     */
    List<OrderDetailVO> orderDetail(OrderDetailRequest request);

    /**
     * 工单工序进度
     * @param request
     * @return
     */
    List<OrderProcessScheduleVO> orderProcessSchedule(OrderProcessScheduleRequest request);


    /**
     * 根据wfId查询托盘及上料相关信息
     * @param wfId 排产工单id
     * @return
     */
    List<ExecuteInfoVO> getExecutinfoList(Integer wfId);
}
