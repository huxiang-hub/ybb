package com.yb.rule.controller;


import com.yb.rule.response.RuleScalesetListVO;
import com.yb.rule.service.RuleScalesetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author my
 * @date 2020-09-19
 * Description:  Controller
 */
@RestController
@Api(tags = "规则设定字段关系接口")
@RequestMapping(value = "/ruleScaleset")
@Slf4j
public class RuleScalesetController extends BladeController {


    @Autowired
    private RuleScalesetService ruleScalesetService;

    /**
     * 列表
     */
    @ApiOperation("列表")
    @GetMapping("list")
    public R<List<RuleScalesetListVO>> saveOrUpdate() {
        List<RuleScalesetListVO> list = ruleScalesetService.list();

        return R.data(list);
    }

}
