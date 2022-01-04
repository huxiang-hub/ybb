package com.yb.rule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.rule.entity.RuleCondition;
import com.yb.rule.entity.RuleProdoee;
import com.yb.rule.service.RuleProdoeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prodoee")
public class RuleProdoeeController {
    @Autowired
    private RuleProdoeeService ruleProdoeeService;


    /**
     * 详情
     */
    @RequestMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseBoxclean")
    public R<RuleProdoee> detail(RuleProdoee ruleProdoee) {
        RuleProdoee detail = ruleProdoeeService.getOne(Condition.getQueryWrapper(ruleProdoee));
        return R.data(detail);
    }

    @RequestMapping("/list")
    @ApiOperation(value = "查询全部信息", notes ="" )
    public R list(){
        List<RuleProdoee> list = ruleProdoeeService.list();
        return R.data(list);
    }

    @RequestMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseBoxclean")
    public R<IPage<RuleProdoee>> page(RuleProdoee ruleProdoee, Query query) {
        IPage<RuleProdoee> pages = ruleProdoeeService.page(Condition.getPage(query), Condition.getQueryWrapper(ruleProdoee));
        return R.data(pages);
    }
    @RequestMapping("/remove")
    @ApiOperation(value = "删除产品规则模型", notes = "ids" )
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids){

        ruleProdoeeService.removeByIds(Func.toIntList(ids));
        return R.success("删除成功");
    }
    @RequestMapping("/insert")
    @ApiOperation(value = "新增产品规则模型", notes = "ruleProdoee" )
    public R insert(@Valid @RequestBody RuleProdoee ruleProdoee){
        ruleProdoee.setStatus(1);
        ruleProdoeeService.save(ruleProdoee);
        return R.success("新增成功");
    }
    @RequestMapping("/update")
    @ApiOperation(value = "修改产品规则模型", notes = "ruleProdoee" )
    public R update(@Valid @RequestBody RuleProdoee ruleProdoee){

        ruleProdoeeService.updateById(ruleProdoee);
        return R.success("修改成功");
    }
    @RequestMapping("/submit")
    @ApiOperation(value = "修改产品规则模型", notes = "ruleProdoee" )
    public R save(@Valid @RequestBody RuleProdoee ruleProdoee){

        ruleProdoeeService.saveOrUpdate(ruleProdoee);
        return R.success("保存成功");
    }

    @RequestMapping("/selectRuleProdoee")
    @ApiOperation(value = "查询产品规则模型", notes = "ruleProdoee" )
    public R selectRuleProdoee(@Valid @RequestBody RuleCondition ruleCondition){
        RuleProdoee ruleProdoee = ruleProdoeeService.selectRuleProdoee(ruleCondition);
        return R.data(ruleProdoee);
    }
    @RequestMapping("/selectRuleProdoeeByMaId")
    @ApiOperation(value = "查询产品规则模型", notes = "maId" )
    public R<IPage<RuleProdoee>> selectRuleProdoeeByMaId(Integer maId, Query query){
        IPage<RuleProdoee> pages = ruleProdoeeService.selectRuleProdoeeByMaId(Condition.getPage(query), maId);
        return R.data(pages);
    }



}
