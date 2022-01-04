package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.vo.MachineAtPresentStatusVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import com.yb.supervise.wrapper.SuperviseBoxinfoWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/superviseboxinfo")
@Api(value = "设备当前状态表boxinfo-视图", tags = "设备当前状态表boxinfo-视图接口")
public class SuperviseBoxinfoController extends BladeController {
    @Autowired
    private ISuperviseBoxinfoService superviseBoxinfoService;

    /**
     * 分页 设备当前状态表boxinfo-视图
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "设备运行状态监控", notes = "传入部门id和工序id")
    public R<List<SuperviseBoxinfoVO>> list(Integer dpId, Integer prId) {
        List<SuperviseBoxinfoVO> processWorkinfoVOS = superviseBoxinfoService.selectSuperviseBoxinfoVO(dpId, prId);
        return R.data(processWorkinfoVOS);
    }

    /**
     * 不同状态的设备数量
     */
    @RequestMapping("/equipmentNum")
    @ApiOperationSupport(order = 8)
    public Integer equipmentNum(String i) {
        return superviseBoxinfoService.equipmentNum(i);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseBoxinfo")
    public R<SuperviseBoxinfoVO> detail(SuperviseBoxinfo superviseBoxinfo) {
        SuperviseBoxinfo detail = superviseBoxinfoService.getOne(Condition.getQueryWrapper(superviseBoxinfo));
        return R.data(SuperviseBoxinfoWrapper.build().entityVO(detail));
    }


    /**
     * 自定义分页 设备当前状态表boxinfo-视图
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseBoxinfo")
    public R<IPage<SuperviseBoxinfoVO>> page(SuperviseBoxinfoVO superviseBoxinfo, Query query) {
        IPage<SuperviseBoxinfoVO> pages = superviseBoxinfoService.selectSuperviseBoxinfoPage(Condition.getPage(query), superviseBoxinfo);
        return R.data(pages);
    }

    /**
     * 新增 设备当前状态表boxinfo-视图
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseBoxinfo")
    public R save(@Valid @RequestBody SuperviseBoxinfo superviseBoxinfo) {
        return R.status(superviseBoxinfoService.save(superviseBoxinfo));
    }

    /**
     * 修改 设备当前状态表boxinfo-视图
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseBoxinfo")
    public R update(@Valid @RequestBody SuperviseBoxinfo superviseBoxinfo) {
        return R.status(superviseBoxinfoService.updateById(superviseBoxinfo));
    }

    /**
     * 新增或修改 设备当前状态表boxinfo-视图
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseBoxinfo")
    public R submit(@Valid @RequestBody SuperviseBoxinfo superviseBoxinfo) {
        return R.status(superviseBoxinfoService.saveOrUpdate(superviseBoxinfo));
    }


    /**
     * 删除 设备当前状态表boxinfo-视图
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseBoxinfoService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 查询车间对应设备信息
     */
    @PostMapping("/selectDeptBoxinfoStatusList")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "首页接口")
    public R<List<BaseDeptinfoVO>> selectDeptBoxinfoStatusList() {
        List<BaseDeptinfoVO> baseDeptinfoVO = superviseBoxinfoService.selectDeptBoxinfoStatusList();
        return R.data(baseDeptinfoVO);
    }

    /**
     * 查询车间对应设备生产信息
     */
    @RequestMapping("/selectDeptBoxinfoSdList")
    @ApiOperation(value = "查询车间对应设备")
    @ApiOperationSupport(order = 7)
    public R<List<ProcessWorkinfoVO>> selectDeptBoxinfoSdList(Integer dpId) {
        List<ProcessWorkinfoVO> baseDeptinfoVO = superviseBoxinfoService.selectDeptBoxinfoSdList(dpId);
        return R.data(baseDeptinfoVO);
    }

    @GetMapping("/getBoxinfoStatusByMaId")
    @ApiOperation(value = "设备管理界面,设备当前状态接口")
    @ApiOperationSupport(order = 7)
    public R<MachineAtPresentStatusVO> getBoxinfoStatusByMaId(Integer maId) {
        MachineAtPresentStatusVO baseDeptinfoVO = superviseBoxinfoService.getBoxinfoStatusByMaId(maId);
        return R.data(baseDeptinfoVO);
    }


}
