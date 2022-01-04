package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.service.IExecuteFaultService;
import com.yb.execute.vo.*;
import com.yb.execute.wrapper.ExecuteFaultWrapper;
import com.yb.maintain.service.IMaintainFaultclassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备停机故障记录表_yb_execute_fault 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executefault")
@Api(value = "设备停机故障记录表_yb_execute_fault", tags = "设备停机故障记录表_yb_execute_fault接口")
public class ExecuteFaultController extends BladeController {

    private IExecuteFaultService executeFaultService;
    @Autowired
    private IMaintainFaultclassifyService maintainFaultclassifyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executeFault")
    public R<ExecuteFaultVO> detail(ExecuteFault executeFault) {
        ExecuteFault detail = executeFaultService.getOne(Condition.getQueryWrapper(executeFault));
        return R.data(ExecuteFaultWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备停机故障记录表_yb_execute_fault
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeFault")
    public R<IPage<ExecuteFaultVO>> list(ExecuteFault executeFault, Query query) {
        IPage<ExecuteFault> pages = executeFaultService.page(Condition.getPage(query), Condition.getQueryWrapper(executeFault));
        return R.data(ExecuteFaultWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备停机故障记录表_yb_execute_fault
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeFault")
    public R<IPage<ExecuteFaultVO>> page(ExecuteFaultVO executeFault, Query query) {
        IPage<ExecuteFaultVO> pages = executeFaultService.selectExecuteFaultPage(Condition.getPage(query), executeFault);
        return R.data(pages);
    }


    /**
     * 自定义分页 设备停机故障记录表_yb_execute_fault
     */
    @GetMapping("/executeFaultPage")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeFault")
    public R<IPage<ExecuteFaultVO>> executeFaultPage(ExecuteFaultVO executeFault, Query query) {
        IPage<ExecuteFaultVO> pages = executeFaultService.selectExecuteFaultPages(Condition.getPage(query), executeFault);
        return R.data(pages);
    }

    /**
     * 新增 设备停机故障记录表_yb_execute_fault
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executeFault")
    public R save(@Valid @RequestBody ExecuteFault executeFault) {
        executeFault.setCreateAt(new Date());
        executeFault.setWay(1);
        return R.status(executeFaultService.save(executeFault));
    }

    /**
     * 修改 设备停机故障记录表_yb_execute_fault
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executeFault")
    public R update(@Valid @RequestBody ExecuteFault executeFault) {
        if (executeFault.getHandle() == 1) {
            executeFault.setHandleTime(new Date());
        }
        return R.status(executeFaultService.updateById(executeFault));
    }

    /**
     * 新增或修改 设备停机故障记录表_yb_execute_fault
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executeFault")
    public R submit(@Valid @RequestBody ExecuteFault executeFault) {
        Date date = new Date();
        Date endAt = executeFault.getEndAt();
        Date startAt = executeFault.getStartAt();
        String classify = executeFault.getClassify();
        if (endAt == null || startAt == null) {
            return R.fail("开始时间结束时间不能为空");
        }
        if (endAt.getTime() < startAt.getTime()) {
            return R.fail("停机的开始时间不能大于结束时间");
        }
        //executeFault.setDuration((int)((endAt.getTime() - startAt.getTime()) / 1000));//持续时间(秒)
        if (classify != null) {
            executeFault.setStatus(maintainFaultclassifyService.getParentFvalue(classify));
        }
        executeFault.setHandle(1);
        executeFault.setHandleTime(date);
        if (executeFault.getId() == null) {
            executeFault.setCreateAt(date);
        }
        return R.status(executeFaultService.saveOrUpdate(executeFault));
    }

    @PostMapping("/notarize")
    @ApiOperation(value = "停机确认", notes = "传入notarizeVO对象")
    public R notarize(@RequestBody NotarizeVO notarizeVO) {
        List<Integer> idList = notarizeVO.getIdList();
        if (idList == null || idList.isEmpty()) {
            return R.fail("传入的idList不能为空");
        }
        String classify = notarizeVO.getClassify();
        if(StringUtil.isEmpty(classify)){
            return R.fail("停机理由不能为空");
        }
        Integer usId = notarizeVO.getUsId();
        if(usId == null){
            return R.fail("确认人不能为空");
        }
        String fvalue = maintainFaultclassifyService.getParentFvalue(classify);
        List<ExecuteFault> executeFaultList = new ArrayList<>();
        Date date = new Date();
        ExecuteFault executeFault;
        for (Integer id : idList) {
            executeFault = new ExecuteFault();
            executeFault.setId(id);
            executeFault.setClassify(classify);
            executeFault.setStatus(fvalue);
            executeFault.setHandle(1);
            executeFault.setRemake(notarizeVO.getRemake());
            executeFault.setHandleTime(date);
            executeFault.setUsId(usId);
            executeFaultList.add(executeFault);
        }
        try {
            executeFaultService.updateBatchById(executeFaultList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("修改时发生异常,操作失败");
        }
        return R.success("操作成功");
    }


    /**
     * 删除 设备停机故障记录表_yb_execute_fault
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeFaultService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 停机子类型list
     */
    @GetMapping("/classfyList")
    @ApiOperation(value = "停机的子类型", notes = "停机子类型名称")
    public List<ExecuteFault> classfyList() {
        List<ExecuteFault> executeFaults = executeFaultService.getClassfyList();
        return executeFaults;
    }

    /**
     * 停机子类型list
     */
    @PostMapping("/getExecuteFaultList")
    @ApiOperation(value = "机台停机列表接口", notes = "传入executeFaultParamVO")
    public R<List<ExecuteFaultWfIdVO>> getExecuteFaultList(@RequestBody ExecuteFaultParamVO executeFaultParamVO) {
        return R.data(executeFaultService.getExecuteFaultList(executeFaultParamVO));
    }

    @PostMapping("/getAllUnconfirmedNum")
    @ApiOperation(value = "查询停机总未确认数量", notes = "传入executeFaultParamVO")
    public R getAllUnconfirmedNum(ExecuteFaultParamVO executeFaultParamVO) {
        Integer allUnconfirmedNum = executeFaultService.getAllUnconfirmedNum(executeFaultParamVO);
        if(allUnconfirmedNum == null){
            return R.fail("班次不存在,请检查设备和班次id是否正确");
        }
        return R.data(allUnconfirmedNum);
    }

    @PostMapping("/executeFaultList")
    @ApiOperation(value = "后台停机列表查询")
    public R<IPage<ExecuteFaultVO>> executeFaultList(ExecuteFaultRequest executeFaultRequest, Query query){
        IPage<ExecuteFaultVO> executeFault =  executeFaultService.executeFaultList(executeFaultRequest, Condition.getPage(query));
        return R.data(executeFault);
    }
}
