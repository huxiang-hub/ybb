package com.yb.statis.controller;

import com.yb.common.DateUtil;
import com.yb.statis.request.DeviceCapacityProgressRequest;
import com.yb.statis.request.DeviceCurrentOrderRequest;
import com.yb.statis.service.StatisCapacityService;
import com.yb.statis.vo.DeviceCapacityProgressVO;
import com.yb.statis.vo.DeviceCurrentOrderVO;
import com.yb.statis.vo.DeviceOrderNumProgressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/23 9:20
 */
@RestController
@RequestMapping("/statisCapacity")
@Api(value = "设备产能统计", tags = "接口")
@Log4j
@CrossOrigin
public class StatisCapacityController extends BladeController {
    @Autowired
    private StatisCapacityService statisCapacityService;

    /**
     * 设备当前订单进度报表
     */
    @PostMapping("/currentOrder")
    @ApiOperation(value = "设备当前订单进度报表")
    public R<List<DeviceCurrentOrderVO>> currentOrder(@RequestBody @Validated DeviceCurrentOrderRequest request) {
        request.setTargetDay(DateUtil.refNowDay());//日期默认当天,不能选择
        List<DeviceCurrentOrderVO> vos = statisCapacityService.deviceCurrentOrder(request);
        return R.data(vos);
    }

    /**
     * 产能总进度
     */
    @PostMapping("/deviceCapacityProgress")
    @ApiOperation(value = "产能总进度")
    public R<DeviceCapacityProgressVO> deviceCapacityProgress(@RequestBody @Validated DeviceCapacityProgressRequest request) throws ParseException {
        request.setTargetDay(DateUtil.refNowDay());//日期默认当天,不能选择
        DeviceCapacityProgressVO vo = statisCapacityService.deviceCapacityProgress(request);
        return R.data(vo);
    }

    /**
     * 产单总进度
     */
    @PostMapping("/deviceOrderNumProgress")
    @ApiOperation(value = "产能订单数总进度")
    public R<DeviceOrderNumProgressVO> deviceOrderNumProgress(@RequestBody @Validated DeviceCapacityProgressRequest request) throws ParseException {
        request.setTargetDay(DateUtil.refNowDay());//日期默认当天,不能选择
        DeviceOrderNumProgressVO vo = statisCapacityService.deviceOrderNumProgress(request);
        return R.data(vo);
    }
}
