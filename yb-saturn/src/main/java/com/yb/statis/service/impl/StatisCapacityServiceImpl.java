package com.yb.statis.service.impl;

import com.yb.statis.mapper.StatisMachreachMapper;
import com.yb.statis.request.DeviceCapacityProgressRequest;
import com.yb.statis.request.DeviceCurrentOrderRequest;
import com.yb.statis.service.StatisCapacityService;
import com.yb.statis.vo.*;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/22 19:56
 */
@Service
public class StatisCapacityServiceImpl implements StatisCapacityService {
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private StatisMachreachMapper statisMachreachMapper;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;

    @Override
    public List<DeviceCurrentOrderVO> deviceCurrentOrder(DeviceCurrentOrderRequest request) {

        List<DeviceCurrentOrderVO> vos = workbatchShiftMapper.getDeviceCurrentOrder(request);
        return vos;
    }

    @Override
    public DeviceCapacityProgressVO deviceCapacityProgress(DeviceCapacityProgressRequest request) throws ParseException {
        Date date = new Date();
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
        List<Integer> wsIds = workbatchShiftsetMapper.findNowWsIds();
        request.setTargetDay(targetDay);
        request.setWsIds(wsIds);
        DeviceCapacityProgressVO deviceCapacityProgressVO = new DeviceCapacityProgressVO();
        List<DeviceCapacityProgressListVO> deviceCapacityProgressListVOS = statisMachreachMapper.deviceCapacityProgress(request);
        List<Integer> maIds = new ArrayList<>();
        if (!deviceCapacityProgressListVOS.isEmpty()) {
            //设置当前订单信息
            deviceCapacityProgressListVOS.forEach(o -> {
                maIds.add(o.getMaId());
                WorkbatchShift nowOrder = workbatchShiftMapper.getNowOrder(o.getMaId());
                if (nowOrder != null) {
                    o.setCurrentFinishNum(nowOrder.getFinishNum());
                    o.setCurrentRate(nowOrder.getRate());
                } else {
                    o.setCurrentFinishNum(0);
                    o.setCurrentRate(BigDecimal.ZERO);
                }
            });
            //总数信息
            Integer finishNum = deviceCapacityProgressListVOS.stream().filter(o -> o.getFinishNum() != null).mapToInt(DeviceCapacityProgressListVO::getFinishNum).sum();
            Integer planNum = deviceCapacityProgressListVOS.stream().filter(o -> o.getPlanNum() != null).mapToInt(DeviceCapacityProgressListVO::getPlanNum).sum();
            deviceCapacityProgressVO.setTotalFinishNum(finishNum);
            deviceCapacityProgressVO.setTotalPlanNum(planNum);
            //获取正在生产数
            Integer proNum = superviseExecuteMapper.getNum(request,maIds);
            deviceCapacityProgressVO.setTotalCurrentNum(proNum);
            Integer totalWaitNum = planNum - finishNum - proNum;
            if (totalWaitNum < 0) {
                totalWaitNum = 0;
            }
            //待生产数
            deviceCapacityProgressVO.setTotalWaitNum(totalWaitNum);
            //暂停未下发数
            int stopNum = workbatchShiftMapper.getStopNum(request.getWsIds(), request.getTargetDay(), "4");
            deviceCapacityProgressVO.setStopNum(stopNum);
            deviceCapacityProgressVO.setDeviceCapacityProgressListVOS(deviceCapacityProgressListVOS);

        }
        return deviceCapacityProgressVO;
    }

    @Override
    public DeviceOrderNumProgressVO deviceOrderNumProgress(DeviceCapacityProgressRequest request) throws ParseException {
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);//当天时间
        List<Integer> wsIds = workbatchShiftsetMapper.findNowWsIds();
        request.setTargetDay(targetDay);

        request.setWsIds(wsIds);
        DeviceOrderNumProgressVO deviceOrderNumProgressVO = new DeviceOrderNumProgressVO();
        List<DeviceOrderNumProgressListVO> vos = statisMachreachMapper.deviceOrderNumProgress(request);
        if (!vos.isEmpty()) {
            vos.forEach(o -> {
                if (o.getFinishNum() == 0) {
                    o.setRate(BigDecimal.ZERO);
                } else {
                    BigDecimal rate = BigDecimal.valueOf(o.getFinishNum() / Double.valueOf(o.getWfNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    o.setRate(rate);
                }
            });
        }
        //已完成订单数
        int totalFinishNum = workbatchShiftMapper.getOrdeNum(request.getMaId(), request.getWsIds(), request.getTargetDay(), 2, null);
        //获取所有订单数
        int totalNum = workbatchShiftMapper.getOrdeNum(request.getMaId(), request.getWsIds(), request.getTargetDay(), null, null);
        //获取正在生产中的订单数
        int proNum = workbatchShiftMapper.getOrdeNum(request.getMaId(), request.getWsIds(), request.getTargetDay(), 1, null);
        //获取待生产订单数
        int waitNum = workbatchShiftMapper.getOrdeNum(request.getMaId(), request.getWsIds(), request.getTargetDay(), 0, null);
        deviceOrderNumProgressVO.setWaitProduceNum(waitNum);
        deviceOrderNumProgressVO.setCurrentNum(proNum);
        deviceOrderNumProgressVO.setTotalNum(totalNum);
        deviceOrderNumProgressVO.setTotalFinishNum(totalFinishNum);
        deviceOrderNumProgressVO.setDeviceOrderNumProgressListVOS(vos);
        //查询暂停未生产单数
        int stopNum = workbatchShiftMapper.getOrdeNum(request.getMaId(), request.getWsIds(), request.getTargetDay(), null, "4");
        deviceOrderNumProgressVO.setStopNum(stopNum);

        return deviceOrderNumProgressVO;
    }
}
