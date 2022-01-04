/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.service.IBaseStaffclassService;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.prod.service.IProdClassifyService;
import com.yb.workbatch.entity.WorkbatchShiftinfo;
import com.yb.workbatch.service.IWorkbatchShiftinfoService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchShiftinfoVO;
import com.yb.workbatch.vo.WorkbatchShiftsetVO;
import com.yb.workbatch.wrapper.WorkbatchShiftinfoWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 排产班次执行表_yb_workbatch_shiftinfo（日志表） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbatchshiftinfo")
@Api(value = "排产班次执行表_yb_workbatch_shiftinfo（日志表）", tags = "排产班次执行表_yb_workbatch_shiftinfo（日志表）接口")
public class WorkbatchShiftinfoController extends BladeController {

    private IWorkbatchShiftinfoService workbatchShiftinfoService;
    private IWorkbatchShiftsetService workbatchShiftsetService;
    private IBaseStaffclassService baseStaffclassService;
    private IBaseStaffinfoService baseStaffinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入workbatchShiftinfo")
    public R<WorkbatchShiftinfoVO> detail(WorkbatchShiftinfo workbatchShiftinfo) {
        WorkbatchShiftinfo detail = workbatchShiftinfoService.getOne(Condition.getQueryWrapper(workbatchShiftinfo));
        return R.data(WorkbatchShiftinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入workbatchShiftinfo")
    public R<IPage<WorkbatchShiftinfoVO>> list(WorkbatchShiftinfo workbatchShiftinfo, Query query) {
        IPage<WorkbatchShiftinfo> pages = workbatchShiftinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(workbatchShiftinfo));
        return R.data(WorkbatchShiftinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入workbatchShiftinfo")
    public R<IPage<WorkbatchShiftinfoVO>> page(WorkbatchShiftinfoVO workbatchShiftinfo, Query query) {
        IPage<WorkbatchShiftinfoVO> pages = workbatchShiftinfoService.selectWorkbatchShiftinfoPage(Condition.getPage(query), workbatchShiftinfo);
        return R.data(pages);
    }

    /**
     * 新增 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入workbatchShiftinfo")
    public R save(@Valid @RequestBody WorkbatchShiftinfo workbatchShiftinfo) {
        return R.status(workbatchShiftinfoService.save(workbatchShiftinfo));
    }

    /**
     * 修改 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入workbatchShiftinfo")
    public R update(@Valid @RequestBody WorkbatchShiftinfo workbatchShiftinfo) {
        return R.status(workbatchShiftinfoService.updateById(workbatchShiftinfo));
    }

    /**
     * 新增或修改 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入workbatchShiftinfo")
    public R submit(@Valid @RequestBody WorkbatchShiftinfo workbatchShiftinfo) {
        return R.status(workbatchShiftinfoService.saveOrUpdate(workbatchShiftinfo));
    }


    /**
     * 删除 排产班次执行表_yb_workbatch_shiftinfo（日志表）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(workbatchShiftinfoService.removeByIds(Func.toIntList(ids)));
    }
    //每00:00:00 执行
//    查詢當天所以計劃排班
//    @Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(cron = "0 0 0 ? * *")
    public void pullAll() throws ParseException {
//        将到结束时间的换班信息修改为不可用
        baseStaffclassService.updataByisused();
//      查询今天的班次相关信息
        System.out.println("查询今天的班次相关信息");
        List<WorkbatchShiftsetVO> list = workbatchShiftsetService.getToDayList();
        if (!Func.isEmpty(list)){
            for (WorkbatchShiftsetVO workbatchShiftsetVO : list){
//                查询使用该班次的班组集合（查询所有处理调班人员以外的人员）
                List<BaseStaffinfoVO> baseStaffinfoVOList1 = baseStaffinfoService.getAllByBcId(workbatchShiftsetVO.getId());
//                查询换组来执行这个班次的人员
                List<BaseStaffinfoVO> baseStaffinfoVOList2 = baseStaffinfoService.getStaffClassAllByWsId(workbatchShiftsetVO.getId());
                if (!Func.isEmpty(baseStaffinfoVOList1)){
                    for (BaseStaffinfoVO baseStaffinfoVO : baseStaffinfoVOList1){
                        WorkbatchShiftinfo workbatchShiftinfo = new WorkbatchShiftinfo();
                        workbatchShiftinfo.setWsId(workbatchShiftsetVO.getId());
                        workbatchShiftinfo.setDbId(baseStaffinfoVO.getDpId());
                        workbatchShiftinfo.setModel(workbatchShiftsetVO.getModel());
                        Date newDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
                        workbatchShiftinfo.setCheckDate(sdf.parse(sdf.format(newDate)));
                        workbatchShiftinfo.setCkName(workbatchShiftsetVO.getCkName());
                        workbatchShiftinfo.setStartTime(sdf.parse(sdf.format(newDate)+" "+tdf.format(workbatchShiftsetVO.getStartTime())));
                        workbatchShiftinfo.setEndTime(sdf.parse(sdf.format(newDate)+" "+tdf.format(workbatchShiftsetVO.getEndTime())));
                        workbatchShiftinfo.setUsId(baseStaffinfoVO.getUserId());
                        workbatchShiftinfoService.save(workbatchShiftinfo);
                    }
                }
                if (!Func.isEmpty(baseStaffinfoVOList2)){
                    for (BaseStaffinfoVO baseStaffinfoVO : baseStaffinfoVOList2){
                        WorkbatchShiftinfo workbatchShiftinfo = new WorkbatchShiftinfo();
                        workbatchShiftinfo.setWsId(workbatchShiftsetVO.getId());
                        workbatchShiftinfo.setDbId(baseStaffinfoVO.getNewDpId());//临时调换的所在部门
                        workbatchShiftinfo.setModel(workbatchShiftsetVO.getModel());
                        Date newDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
                        workbatchShiftinfo.setCheckDate(sdf.parse(sdf.format(newDate)));
                        workbatchShiftinfo.setCkName(workbatchShiftsetVO.getCkName());
                        workbatchShiftinfo.setStartTime(sdf.parse(sdf.format(newDate)+" "+tdf.format(workbatchShiftsetVO.getStartTime())));
                        workbatchShiftinfo.setEndTime(sdf.parse(sdf.format(newDate)+" "+tdf.format(workbatchShiftsetVO.getEndTime())));
                        workbatchShiftinfo.setUsId(baseStaffinfoVO.getUserId());
                        workbatchShiftinfoService.save(workbatchShiftinfo);
                    }
                }
            }
        }
    }
}
