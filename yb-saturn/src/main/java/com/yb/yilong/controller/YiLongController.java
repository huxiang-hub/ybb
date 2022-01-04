package com.yb.yilong.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.DateUtil;
import com.yb.system.user.response.SysUserPageVO;
import com.yb.yilong.request.*;
import com.yb.yilong.response.BoxInfoNumberVO;
import com.yb.yilong.response.BoxInfoVO;
import com.yb.yilong.response.MachineDownPageVO;
import com.yb.yilong.response.WbNoInfoVO;
import com.yb.yilong.service.PicapiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/6 14:09
 */
@RestController
@RequestMapping("/picapi")
@Api(tags = "erp数据对接")
@Slf4j
public class YiLongController {

    @Autowired
    private PicapiService picapiService;

    @GetMapping("/opShift")
    @ApiOperation(value = "生产流程标记")
    public R opShift(@Validated OpenShiftRequest request) {

        log.info("开始生产流程标记操作:[request:{}]", request);

        R r = picapiService.opShift(request);

        log.info("生产流程标记完成:[request:{}]", request);
        return r;
    }


    @GetMapping("/opBriefer")
    @ApiOperation(value = "上报结束操作")
    public R opBriefer(@Validated OpBrieferRequest request) {

        log.info("开始上报操作:[request:{}]", request);

        picapiService.opBriefer(request);

        log.info("上报完成:[request:{}]", request);
        return R.success("上报操作完成");
    }

    @GetMapping("/boxInfo")
    @ApiOperation(value = "盒子实时信息状态")
    public R<BoxInfoVO> boxInfo(@Validated BoxInfoRequest request) {

        BoxInfoVO vo = picapiService.boxInfo(request);

        return R.data(vo);
    }

    @GetMapping("/wbNoInfo")
    @ApiOperation(value = "设备工单实时状态")
    public R<List<WbNoInfoVO>> wbNoInfo(@Validated WbNoInfoRequest request) {

        log.info("开始获取设备工单实时状态:[request:{}]", request);
        if (request == null) {
            return R.fail("请传递设备唯一标识或者工单唯一标识。");
        }


        if (request.getMaId() == null && request.getWbNo() == null) {
            return R.fail("工单唯一标识和设备唯一标识必须传一个信息。");
        } else {
            if (request.getTargetDay() == null) {
                String targetDay = DateUtil.refNowDay();
                request.setTargetDay(targetDay);
            }
        }

        List<WbNoInfoVO> vo = picapiService.wbNoInfo(request);

        log.info("获取设备工单实时状态完成:[request:{}]", request);
        return R.data(vo);
    }


    @GetMapping("/boxNumber")
    @ApiOperation(value = "盒子计数信息")
    public R<BoxInfoNumberVO> boxNumber(Integer maId) {

        BoxInfoNumberVO vo = picapiService.boxNumber(maId);

        return R.data(vo);
    }

    @GetMapping("/downPage")
    @ApiOperation(value = "停机数据分页")
    public R<IPage<MachineDownPageVO>> downPage(@Validated MachineDownPageRequest request) {

        if (request == null || request.getMaId() == null) {
            return R.fail("MachineDownPageRequest对象请求为空，请核查数据，设备maId为空");
        }

        log.info("开始获取停机数据:[request{}]", request);

        IPage<MachineDownPageVO> vos = picapiService.downPage(Condition.getPage(request), request);

        log.info("生产流程标记完成:[request:{}]", request);
        return R.data(vos);
    }
}