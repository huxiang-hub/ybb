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
package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.entity.StatisOrdinfo;
import com.yb.statis.entity.StatisOrdsingle;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.statis.service.IStatisOrdfullService;
import com.yb.statis.service.IStatisOrdinfoService;
import com.yb.statis.service.IStatisOrdsingleService;
import com.yb.statis.vo.StatisMachsingleVO;
import com.yb.statis.vo.StatisOrdsingleVO;
import com.yb.statis.wrapper.StatisOrdsingleWrapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShiftinfo;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftinfoService;
import com.yb.workbatch.vo.WorkbatchOrdoeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisordsingle")
@Api(value = "OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）", tags = "OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）接口")
public class StatisOrdsingleController extends BladeController {

    private IStatisOrdsingleService statisOrdsingleService;
    //OEE需要导入的service
    private IStatisOeesetService iStatisOeesetService;
    private IStatisOrdinfoService iStatisOrdinfoService;
    private IStatisOrdsingleService iStatisOrdsingleService;


    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisOrdsingle")
    public R<StatisOrdsingleVO> detail(StatisOrdsingle statisOrdsingle) {
        StatisOrdsingle detail = statisOrdsingleService.getOne(Condition.getQueryWrapper(statisOrdsingle));
        return R.data(StatisOrdsingleWrapper.build().entityVO(detail));
    }

    /**
     * 分页 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statisOrdsingle")
    public R<IPage<StatisOrdsingleVO>> list(StatisOrdsingle statisOrdsingle, Query query) {
        IPage<StatisOrdsingle> pages = statisOrdsingleService.page(Condition.getPage(query), Condition.getQueryWrapper(statisOrdsingle));
        return R.data(StatisOrdsingleWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisOrdsingle")
    public R<IPage<StatisOrdsingleVO>> page(StatisOrdsingleVO statisOrdsingle, Query query) {
        IPage<StatisOrdsingleVO> pages = statisOrdsingleService.selectStatisOrdsinglePage(Condition.getPage(query), statisOrdsingle);
        return R.data(pages);
    }

    /**
     * 新增 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisOrdsingle")
    public R save(@Valid @RequestBody StatisOrdsingle statisOrdsingle) {
        return R.status(statisOrdsingleService.save(statisOrdsingle));
    }

    /**
     * 修改 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisOrdsingle")
    public R update(@Valid @RequestBody StatisOrdsingle statisOrdsingle) {
        return R.status(statisOrdsingleService.updateById(statisOrdsingle));
    }

    /**
     * 新增或修改 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisOrdsingle")
    public R submit(@Valid @RequestBody StatisOrdsingle statisOrdsingle) {
        return R.status(statisOrdsingleService.saveOrUpdate(statisOrdsingle));
    }


    /**
     * 自定义分页 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @GetMapping("/pageOrdsingle")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisOrdsingle")
    public R<IPage<StatisOrdsingleVO>> pageOrdsingle(StatisOrdsingleVO statisOrdsingle, Query query) {
        IPage<StatisOrdsingleVO> pages = statisOrdsingleService.selectOEEStatisOrdsinglePage(Condition.getPage(query), statisOrdsingle);
        return R.data(pages);
    }



    /**
     * 删除 OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        Map<String,Object> iStatisOrdsingleMap = new HashMap<>();
        iStatisOrdsingleMap.put("so_id",ids);
        iStatisOrdsingleService.getBaseMapper().deleteByMap(iStatisOrdsingleMap);
        Map<String,Object> istatosOeeSet  = new HashMap<>();
        istatosOeeSet.put("db_id",ids);
        istatosOeeSet.put("st_type",2);
        iStatisOeesetService.getBaseMapper().deleteByMap(istatosOeeSet);
        return R.data(iStatisOrdinfoService.getBaseMapper().deleteById(ids));
    }
    /**
     * 生成OEEreport
     */
    @PostMapping("/generateOEEReport")
    @ApiOperation(value = "生成OEEReport")
    public void generateOEEReport() {
        Integer userId = 21;
        Integer mallId = 5;
        Integer sdId = 449;
        Double dutyNumber = 3.5;
        try {
            statisOrdsingleService.generateOrderOEEReport(userId, mallId, sdId,dutyNumber,null,1);//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成OEEreport
     */
    @GetMapping("/RecaculateOEEReport")
    @ApiOperation(value = "重新算OEEReport")
    public void RecaculateOEEReport(@ApiParam(value = "主键", required = true) @RequestParam String id) {
        StatisOrdinfo statisOrdinfo =iStatisOrdinfoService.getById(id);
        Integer userId = statisOrdinfo.getUsId();
        Integer mallId = statisOrdinfo.getMaId();
        Integer sdId = statisOrdinfo.getSdId();
        Double dutyNumber = 3.5;
        try {
            statisOrdsingleService.generateOrderOEEReport(userId, mallId, sdId,dutyNumber,id,statisOrdinfo.getExId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
