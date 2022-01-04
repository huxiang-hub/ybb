package com.yb.panelapi.order.controller;

import com.yb.panelapi.order.service.MachineNewService;
import com.yb.panelapi.request.MachineStopListRequest;
import com.yb.panelapi.request.WeekHourRateLossRequest;
import com.yb.panelapi.vo.HourRateLossStatisticsVO;
import com.yb.panelapi.vo.MachineStopListVO;
import com.yb.panelapi.vo.MachineStopVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.exception.CommonException;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description: 机台新接口
 * @Author my
 * @Date Created in 2020/8/12 19:26
 */
@RestController
@RequestMapping("/machine")
@Slf4j
public class MachineNewController {

    @Autowired
    private MachineNewService machineNewService;

    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;

    @GetMapping("/getShiftLastOrder")
    @ApiOperation(value = "获取当前班次最后一个订单")
    public R getShiftLastOrder(Integer maId, Integer wsId) {
        log.debug("获取当前班次最后一个订单：[maId:{}, wsId:{}]", maId, wsId);

        Integer wfId = machineNewService.getShiftLastOrder(maId, wsId);

        log.debug("获取当前班次最后一个订单成功：[maId:{}, wsId:{}, bId:{}]", maId, wsId, wfId);
        return R.data(wfId);
    }

    @PostMapping("/hourRateLossStatistics")
    @ApiOperation(value = "小时达成率备注统计分析")
    public R<List<HourRateLossStatisticsVO>> hourRateLossStatistics(@RequestBody @Validated WeekHourRateLossRequest request) {
        List<HourRateLossStatisticsVO> vos = machineNewService.hourRateLossStatistics(request);
        return R.data(vos);
    }

    @GetMapping("/getBoxUuid")
    @ApiOperation(value = "获取设备绑定的盒子的uuid")
    public R getBoxUuid(Integer maId) {
        SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        if (boxinfo == null) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "请绑定盒子信息");
        }
        return R.data(boxinfo.getUuid());
    }

    @PostMapping("/stopList")
    @ApiOperation(value = "机台停机列表")
    public R<MachineStopVO> stopList(@RequestBody @Validated MachineStopListRequest request) {
        log.debug("开始获取机台停机列表：[request:{}]", request);

        MachineStopVO vo = machineNewService.stopList(request);

        log.debug("获取机台停机列表成功");
        return R.data(vo);
    }
}
