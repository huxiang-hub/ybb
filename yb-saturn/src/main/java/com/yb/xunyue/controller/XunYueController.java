package com.yb.xunyue.controller;

import com.yb.xunyue.request.*;
import com.yb.yilong.service.PicapiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/6 14:09
 */
@RestController
@RequestMapping("/xunyue")
@Api(tags = "erp数据对接")
@Slf4j
public class XunYueController {

    @Autowired
    private PicapiService picapiService;

    @GetMapping("/opShift")
    @ApiOperation(value = "生产流程标记")
    public R opShift(@Validated XueYueOpenShiftRequest request) {

        log.info("开始生产流程标记操作:[request:{}]", request);
        R r = picapiService.xunYueOpShift(request);
        log.info("生产流程标记完成:[request:{}]", request);
        return r;
    }

    @GetMapping("/opStatus")
    @ApiOperation(value = "生产过程状态变化")
    public R opStatus(Integer maId, Integer status) {
        //status 状态为：1调机状态，2正式生产状态
        log.info("进行状态切换:[maId:{}]状态[status:{}]", maId, status);
        R r = picapiService.xunYueOpStatus(maId, status);
        log.info("进行状态完成--end:[maId:{}]状态[status:{}]", maId, status);
        return r;
    }

    @GetMapping("/opClasses")
    @ApiOperation(value = "生产执行换班操作；无需修改工单信息。需要结束上个班次信息，重新建立一个新的换班记录")
    public R opClasses(@Validated XueYueOpenShiftRequest request) {
        //status 状态为：1调机状态，2正式生产状态
        log.info("进行状态切换:[maId:{}]状态[classes:{}]", request.getMaId(), request.getClasses());
        R r = picapiService.xunYueOpClasses(request);
        log.info("进行状态完成--end:[maId:{}]状态[classes:{}]", request.getMaId(), request.getClasses());
        return r;
    }

}