package com.yb.rule.controller;


import com.yb.rule.request.RuleExecuteSaveRequest;
import com.yb.rule.response.RuleExecuteListVO;
import com.yb.rule.service.RuleExecuteService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;


import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author my
 * @date 2020-09-19
 * Description:  Controller
 */
@RestController
@Api(tags = "规则设定接口")
@RequestMapping(value = "/ruleExecute")
@Slf4j
public class RuleExecuteController extends BladeController {


    @Autowired
    private RuleExecuteService ruleExecuteService;

    /**
     * 新增/更新
     *
     * @param request request
     */
    @ApiOperation("新增/更新")
    @PostMapping("saveOrUpdate")
    public R saveOrUpdate(@Validated @RequestBody RuleExecuteSaveRequest request) {

        log.debug("开始新增规则信息:request:{}", request);

        ruleExecuteService.save(request);

        log.debug("新增规则信息成功");
        return R.success("ok");
    }

    /**
     * 删除
     *
     * @param ids ids
     */
    @ApiOperation("删除")
    @PostMapping("delete")
    public R delete(@Validated @RequestBody @ApiParam("删除的id") @NotEmpty List<Integer> ids) {

        log.debug("开始删除规则信息:ids:{}", ids);

        ruleExecuteService.delete(ids);

        log.debug("删除规则信息成功");
        return R.success("ok");
    }

    /**
     * 列表
     */
    @ApiOperation("列表")
    @GetMapping("list")
    public R<List<RuleExecuteListVO>> list(@ApiParam("设备id") @Validated @NotNull Integer maId) {

        log.debug("开始获取规则设定列表");

        List<RuleExecuteListVO> list = ruleExecuteService.list(maId);

        log.debug("获取规则设定列表成功");
        return R.data(list);
    }

    /**
     * 新增/更新
     *
     * @param request request
     */
    @ApiOperation("测试")
    @PostMapping("test")
    public R test(@Validated @RequestBody RuleExecuteSaveRequest request) {

        log.debug("开始测试规则信息:request:{}", request);

        try {
            List<WorkbatchOrdlink> vos = ruleExecuteService.test(request);
        } catch (Exception e) {
            return  R.fail("sql异常");
        }

        log.debug("测试规则信息成功");
        return R.data("ok");
    }
}
