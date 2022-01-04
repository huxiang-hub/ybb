package com.yb.machine.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.mapper.ExecuteInfoMapper;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.machine.mapper.ProcessFlowMapper;
import com.yb.execute.vo.TraycardMaterialsVO;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.request.OrderDetailRequest;
import com.yb.machine.request.OrderProcessScheduleRequest;
import com.yb.machine.request.ProcessFlowPageRequest;
import com.yb.machine.response.*;
import com.yb.machine.service.MachineReportService;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.supervise.mapper.SuperviseIntervalMapper;
import com.yb.supervise.mapper.SuperviseIntervalalgMapper;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author my
 */
@Service
@Slf4j
public class MachineReportServiceImpl implements MachineReportService {
    @Autowired
    private SuperviseIntervalalgMapper superviseIntervalalgMapper;

    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;

    @Autowired
    private SuperviseIntervalMapper superviseIntervalMapper;

    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;

    @Autowired
    private ProcessFlowMapper processFlowMapper;

    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;

    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;

    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;

    @Autowired
    private ExecuteInfoMapper executeInfoMapper;

    @Override
    public MachineReportVO machineReport(MachineReportRequest request) {
        MachineReportVO repotVO = new MachineReportVO();
        //获取设备状态变化数据
        statusChangeReport(request, repotVO);

        List<MachineSpeedChangePortVO> speedReport = superviseRegularMapper.getSpeedReport(request);

        //查询小时达成率
        List<MachineOrdreachPortVO> ordreachPort = statisOrdreachMapper.ordreachPort(request);
        repotVO.setMachineOrdreachPort(ordreachPort);
        repotVO.setMachineSpeedChangePort(speedReport);

        return repotVO;
    }

    @Override
    public IPage<ProcessFlowVO> page(IPage<ProcessFlowVO> page, ProcessFlowPageRequest request) {
        List<ProcessFlowVO> vos = processFlowMapper.page(page, request);
        if (!vos.isEmpty()) {
            vos.forEach(o -> {
                String planAndArrangeNum = o.getPlanAndArrangeNum();
                planAndArrangeNum = (planAndArrangeNum!=null)?planAndArrangeNum:"";
                String[] split = planAndArrangeNum.split("\\|");
                List<String> strings = Arrays.asList(split);
                List b = new ArrayList();
                strings.forEach(a -> {
                    int index = a.indexOf("-") + 1;
                    a = a.substring(index, a.length());
                    b.add(a);
                });
                planAndArrangeNum = StringUtils.join(b, "|");
                o.setPlanAndArrangeNum(planAndArrangeNum);
                //处理废品数和良品数
                String countNumAndWasteNum = o.getCountNumAndWasteNum();
                countNumAndWasteNum = (countNumAndWasteNum!=null)?countNumAndWasteNum:"";
                String[] countNumAndWasteNums = countNumAndWasteNum.split("\\|");
                List<String> countNumAndWasteNumList = Arrays.asList(countNumAndWasteNums);
                List c = new ArrayList();
                countNumAndWasteNumList.forEach(a -> {
                    int index = a.indexOf("-") + 1;
                    a = a.substring(index, a.length());
                    c.add(a);
                });
                countNumAndWasteNum = StringUtils.join(c, "|");
                o.setCountNumAndWasteNum(countNumAndWasteNum);
            });
        }
        return page.setRecords(vos);
    }

    @Override
    public List<OrderDetailVO> orderDetail(OrderDetailRequest request) {
        //wbNo 批次单：工单编号信息
        List<OrderDetailVO> vos = workbatchOrdlinkMapper.orderDetail(request);

        return vos;
    }

    @Override
    public List<OrderProcessScheduleVO> orderProcessSchedule(OrderProcessScheduleRequest request) {

        List<OrderProcessScheduleVO> vos = executeBrieferMapper.orderProcessSchedule(request);

        return vos;
    }

    private void statusChangeReport(MachineReportRequest request, MachineReportVO repotVO) {
        List<MachineStatusChangePortVO> statusPortVOS = superviseIntervalMapper.getStatusChangePort(request);
        if (!statusPortVOS.isEmpty()) {
            int runTime = statusPortVOS.stream().filter(o -> o.getStatus().equals("1")).mapToInt(MachineStatusChangePortVO::getDiffTime).sum();
            int stopTime = statusPortVOS.stream().filter(o -> o.getStatus().equals("2")).mapToInt(MachineStatusChangePortVO::getDiffTime).sum();
            int offTime = statusPortVOS.stream().filter(o -> o.getStatus().equals("4")).mapToInt(MachineStatusChangePortVO::getDiffTime).sum();
            repotVO.setRunTime(runTime);
            repotVO.setStopTime(stopTime);
            repotVO.setOffTime(offTime);
        }
    }


    @Override
    public List<TraycardMaterialsVO> getTraycardMaterials(Integer wfId) {
        return executeTraycardMapper.getTraycardMaterials(wfId);
    }

    @Override
    public List<ExecuteInfoVO> getExecutinfoList(Integer wfId){
        return executeInfoMapper.getExecutinfoList(wfId);
    }
}
