package com.yb.statis.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.rule.service.RuleProdoeeService;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.request.StatisOrdreachPageRequest;
import com.yb.statis.request.StatisOrdreachSaveUpdateRequest;
import com.yb.statis.service.StatisOrdreachService;
import com.yb.statis.vo.HourRateVO;
import com.yb.statis.vo.StatisOrdreachListVO;
import com.yb.statis.vo.StatisOrdreachVO;
import com.yb.statis.vo.TodayOrdreachVO;
import com.yb.system.dict.entity.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author my
 * @date 2020-06-11
 * Description: 设备实时达成率_yb_statis_ordreach Controller
 */
@RestController
@AllArgsConstructor
@Api(tags = "设备实时达成率_yb_statis_ordreach接口")
@RequestMapping(value = "/statisOrdreach")
@Slf4j
public class StatisOrdreachController extends BladeController {

    @Autowired
    private StatisOrdreachService statisOrdreachService;

    @PostMapping("/list")
    @ApiOperation(value = "实时达成率列表")
    public R<List<StatisOrdreachListVO>> list(@Valid @RequestBody StatisOrdreachPageRequest request) {
        List<StatisOrdreachListVO> list = statisOrdreachService.list(request);
        return R.data(list);
    }

    @PostMapping(value = "/hourRateList")
    @ApiOperation(value = "小时达成率列表")
    public R<List<StatisOrdreachListVO>> hourRateList(@RequestBody @Validated StatisOrdreachPageRequest request) {
        List<StatisOrdreachListVO> list = statisOrdreachService.hourRateList(request);
        return R.data(list);
    }


    @PostMapping("update")
    @ApiOperation(value = "修改达成率备注信息")
    public R update(@Validated @RequestBody StatisOrdreachSaveUpdateRequest request) {
        log.info("开始修改达成率备注信息");

        statisOrdreachService.update(request);

        log.info("修改达成率备注信息成功");
        return R.success("修改成功");
    }

    /****
     * 测试方法未做使用
     * @deprecated
     * @param targetDay
     * @param wsId
     * @param maId
     * @return
     */
    @RequestMapping("setOrdreachByMaid")
    @ApiOperation(value = "修改达成率备注信息，测试方法，未使用")
    public R setOrdreachByMaid(String targetDay, Integer wsId, Integer maId) {
        statisOrdreachService.setOrdreachByMaid(targetDay, wsId, maId);
        return R.success("ok");
    }

    @GetMapping("get/{id}")
    @ApiOperation(value = "获取达成率备注")
    public R<StatisOrdreachVO> get(@PathVariable(value = "id") Integer id) {
        log.info("开始获取小时达成率详情信息:[id:{}]", id);

        StatisOrdreachVO statisOrdreachVO = statisOrdreachService.get(id);

        log.info("获取小时达成率详情信息成功:[id:{}]", id);
        return R.data(statisOrdreachVO);
    }

    @PostMapping("reachButtonList")
    @ApiOperation(value = "获取达成率备注按钮列表")
    public R reachButtonList() {
        List<Dict> reachDictList = statisOrdreachService.getReachDictList();
        return R.data(reachDictList);
    }

    /*****
     * 测试方法未做使用
     * @deprecated
     * @param targetDay
     * @param wsId
     * @param maId
     * @return
     */
    @RequestMapping("updateStatisOrdreach")
    @ApiOperation(value = "测试方法，未使用操作")
    public R updateStatisOrdreach(String targetDay, Integer wsId, Integer maId) {
        statisOrdreachService.updateStatisOrdreach(targetDay, wsId, maId);
        return R.success("修改成功");
    }

    @RequestMapping("setOrdreach")
    @ApiOperation(value = "执行小时达成率的算法")
    public R setOrdreach(String targetDay, Integer wsId, Integer maId) {
        statisOrdreachService.setOrdreach(targetDay, wsId, maId, DBIdentifier.getProjectCode());
        return R.success("执行小时达成率的算法：day：" + targetDay + "::设备名称：" + maId);
    }

    @Autowired
    private RuleProdoeeService ruleProdoeeService;

    @RequestMapping("setRuleOrdoee")
    @ApiOperation(value = "执行速度规则算法")
    public R setRuleOrdoee(Integer sdId,String targetDay, Integer wsId, Integer maId) {
        ruleProdoeeService.setRuleOrdoee(sdId, targetDay, wsId, maId,"7");//测试执行算法
        return R.success("执行速度规则算法：day：" + targetDay + "::设备名称：" + maId);
    }

    @ApiOperation(value = "大屏-获取当日各类型设备达成率")
    @GetMapping("todyOrdreach")
    public R<List<TodayOrdreachVO>> todyOrdreach() {
        List<TodayOrdreachVO> list = statisOrdreachService.todyOrdreach();
        return R.data(list);
    }

    @PostMapping("/gethourRateList")
    @ApiOperation(value = "获取小时达成率列表")
    public R<IPage<StatisOrdreachVO>> gethourRateList(HourRateVO hourRateVO,  Query query){
        return  R.data(statisOrdreachService.gethourRateList(hourRateVO, Condition.getPage(query)));
    }
}
