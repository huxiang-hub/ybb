package com.yb.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.maintain.entity.MaintainFaultclassify;
import com.yb.maintain.service.IMaintainFaultclassifyService;
import com.yb.maintain.vo.MaintainFaultclassifyVO;
import com.yb.maintain.wrapper.MaintainFaultclassifyWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 设备故障表分类_yb_machine_faultclassify 控制器
 *
 * @author Blade
 * @since 2020-03-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/maintainfaultclassify")
@Api(value = "设备故障表分类_yb_machine_faultclassify", tags = "设备故障表分类_yb_machine_faultclassify接口")
public class MaintainFaultclassifyController extends BladeController {

    private IMaintainFaultclassifyService maintainFaultclassifyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入maintainFaultclassify")
    public R<MaintainFaultclassifyVO> detail(MaintainFaultclassify maintainFaultclassify) {
        MaintainFaultclassify detail = maintainFaultclassifyService.getOne(Condition.getQueryWrapper(maintainFaultclassify));
        return R.data(MaintainFaultclassifyWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备故障表分类_yb_machine_faultclassify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入maintainFaultclassify")
    public R<IPage<MaintainFaultclassifyVO>> list(MaintainFaultclassify maintainFaultclassify, Query query) {
        IPage<MaintainFaultclassify> pages = maintainFaultclassifyService.page(Condition.getPage(query), Condition.getQueryWrapper(maintainFaultclassify));
        return R.data(MaintainFaultclassifyWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备故障表分类_yb_machine_faultclassify
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入maintainFaultclassify")
    public R<IPage<MaintainFaultclassifyVO>> page(MaintainFaultclassifyVO maintainFaultclassify, Query query) {
        IPage<MaintainFaultclassifyVO> pages = maintainFaultclassifyService.selectMaintainFaultclassifyPage(Condition.getPage(query), maintainFaultclassify);
        return R.data(pages);
    }

    /**
     * 新增 设备故障表分类_yb_machine_faultclassify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入maintainFaultclassify")
    public R save(@Valid @RequestBody MaintainFaultclassify maintainFaultclassify) {
        return R.status(maintainFaultclassifyService.save(maintainFaultclassify));
    }

    /**
     * 修改 设备故障表分类_yb_machine_faultclassify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入maintainFaultclassify")
    public R update(@Valid @RequestBody MaintainFaultclassify maintainFaultclassify) {
        return R.status(maintainFaultclassifyService.updateById(maintainFaultclassify));
    }

    /**
     * 新增或修改 设备故障表分类_yb_machine_faultclassify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入maintainFaultclassify")
    public R submit(@Valid @RequestBody MaintainFaultclassify maintainFaultclassify) {
        return R.status(maintainFaultclassifyService.saveOrUpdate(maintainFaultclassify));
    }


    /**
     * 删除 设备故障表分类_yb_machine_faultclassify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(maintainFaultclassifyService.removeByIds(Func.toIntList(ids)));
    }
    /**
     * 删除 设备故障表分类_yb_machine_faultclassify
     */
    @GetMapping("/getMaintainFaultclassifyTree")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "获取故障停机分类树信息")
    public R getMaintainFaultclassifyTree() {
        List<MaintainFaultclassifyVO> maintainFaultclassifyTree = maintainFaultclassifyService.getMaintainFaultclassifyTree();
        return R.data(maintainFaultclassifyTree);
    }
    @GetMapping("/getMaintainFaultclassifyList")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "获取故障停机分类信息列表")
    public R<List<MaintainFaultclassify>> getMaintainFaultclassifyList() {
        return R.data(maintainFaultclassifyService.list(new QueryWrapper<MaintainFaultclassify>().ne("pid", 0)));
    }


}
