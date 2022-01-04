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
//import com.yb.common.utils.HolidaysAndFestivals;
import com.yb.common.DateUtil;
import com.yb.common.utils.HolidaysAndFestivals;
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.vo.StatisMachoeeVO;
import com.yb.statis.wrapper.StatisMachoeeWrapper;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 控制器
 *
 * @author Blade
 * @since 2020-04-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statismachoee")
@Api(value = "", tags = "oee接口")
public class StatisMachoeeController extends BladeController {

    @Autowired
    private IStatisMachoeeService statisMachoeeService;

    /**
     * 查询当周班次oee统计
     */
    @RequestMapping("/getCkMachoee")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R getCkMachoee(String dateTime, Integer maId) {
        return R.data(statisMachoeeService.getCkMachoee(dateTime, maId));
    }
    /**
     * 查询当周班次类型oee
     */
    @RequestMapping("/getCkMachTypeOee")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R getCkMachTypeOee(String dateTime, Integer maType, Integer wsId) {
        return R.data(statisMachoeeService.getCkMachTypeOee(dateTime, maType, wsId));
    }
    /**
     * 查询当周班次oee统计,返回日期key
     */
    @RequestMapping("/getCkDateMachoee")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R getCkDateMachoee(String dateTime, Integer maId) {
        return R.data(statisMachoeeService.getCkDateMachoee(dateTime, maId));
    }
    /**
     * 查询当周24小时oee统计
     */
    @RequestMapping("/getDayMachoee")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R getDayMachoee(String dateTime, Integer maType) {
        return R.data(statisMachoeeService.getDayMachoee(dateTime, maType));
    }
    /**
     * 根据日期.设备id,班次id查询oee
     */
    @RequestMapping("/getMachoeeByWsIdANDMaId")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R getMachoeeByWsIdANDMaId(String oeDate, Integer maId, Integer wsId) {
        Double oeeRate = statisMachoeeService.getMachoeeByWsIdANDMaId(oeDate, maId, wsId);
        return R.data(oeeRate);
    }
    /**
     *  根据传入日期查询本月的节假日
     */
    @RequestMapping("/getJJR")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "根据传入日期,返回是否为节假日", notes = "targeDay")
    public R getJJR(String targeDay) {
        Date date = DateUtil.changeDay(targeDay);
        SimpleDateFormat yyyysdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat mouthsdf = new SimpleDateFormat("MM");
        Integer year = Integer.valueOf(yyyysdf.format(date));
        Integer mouth = Integer.valueOf(mouthsdf.format(date)) - 1;
        return R.data(HolidaysAndFestivals.JJR(year, mouth));
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachoee")
    public R<StatisMachoeeVO> detail(StatisMachoee statisMachoee) {
        StatisMachoee detail = statisMachoeeService.getOne(Condition.getQueryWrapper(statisMachoee));
        return R.data(StatisMachoeeWrapper.build().entityVO(detail));
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statisMachoee")
    public R<IPage<StatisMachoeeVO>> list(StatisMachoee statisMachoee, Query query) {
        IPage<StatisMachoee> pages = statisMachoeeService.page(Condition.getPage(query), Condition.getQueryWrapper(statisMachoee));
        return R.data(StatisMachoeeWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisMachoee")
    public R<IPage<StatisMachoeeVO>> page(StatisMachoeeVO statisMachoee, Query query) {
        IPage<StatisMachoeeVO> pages = statisMachoeeService.selectStatisMachoeePage(Condition.getPage(query), statisMachoee);
        return R.data(pages);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisMachoee")
    public R save(@Valid @RequestBody StatisMachoee statisMachoee) {
        return R.status(statisMachoeeService.save(statisMachoee));
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisMachoee")
    public R update(@Valid @RequestBody StatisMachoee statisMachoee) {
        return R.status(statisMachoeeService.updateById(statisMachoee));
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisMachoee")
    public R submit(@Valid @RequestBody StatisMachoee statisMachoee) {
        return R.status(statisMachoeeService.saveOrUpdate(statisMachoee));
    }


    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(statisMachoeeService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * OEE折线图接口
     */
    @PostMapping("/showMachineReport")
    @ApiOperation(value = "获取oee的折线图所需要的数据")
    public R showMachineReport(String maId, String prId, String dpId, String checkDate, String mode) {
        if (checkDate == null || checkDate .equals("")) {
            return R.fail("日期不正确");
        }
        ArrayList<String> conditionArrMin = new ArrayList<>();
        ArrayList<String> conditionArrHour = new ArrayList<>();
        if (mode != null && mode != "") {
            // model=0 15 分钟的标准
            // model=1 30 分钟的标准
            // model=2 1 小时的标准
            // model=3 2 小时的标准
            if (mode.equals("0")) {
                conditionArrMin.add("0");
                conditionArrMin.add("15");
                conditionArrMin.add("30");
                conditionArrMin.add("45");
            }
            if (mode.equals("1")) {
                conditionArrMin.add("0");
                conditionArrMin.add("30");
            }
            if (mode.equals("2")) {
				conditionArrMin.add("0");
            }
            if (mode.equals("3")) {
            	//分钟条件
				conditionArrMin.add("0");
				//小时条件
				conditionArrHour.add("0");
				conditionArrHour.add("2");
				conditionArrHour.add("4");
				conditionArrHour.add("6");
				conditionArrHour.add("8");
				conditionArrHour.add("10");
				conditionArrHour.add("12");
				conditionArrHour.add("14");
				conditionArrHour.add("16");
				conditionArrHour.add("18");
				conditionArrHour.add("20");
				conditionArrHour.add("22");
			}

        }
        List<StatisMachoeeVO> statisMachoeeVOS = statisMachoeeService.getOeeRateByDay(maId, prId,dpId,checkDate,conditionArrHour,conditionArrMin);
        for (StatisMachoeeVO statisMachoeeVO : statisMachoeeVOS) {
            BigDecimal temp = statisMachoeeVO.getGatherRate();
            if (temp.compareTo(new BigDecimal("1")) == 1) {
                statisMachoeeVO.setGatherRate(new BigDecimal("1"));
            }
        }
        Map<Integer,List<StatisMachoeeVO>> listMap = new HashMap<>();
        //使用机器id 进行分组
        for (StatisMachoeeVO statisMachoee : statisMachoeeVOS) {
            Integer key=  statisMachoee.getMaId();
            if (listMap.get(key)==null){
                List<StatisMachoeeVO> list =new ArrayList<>();
                list.add(statisMachoee);
                listMap.put(key,list);
            }else {
                listMap.get(key).add(statisMachoee);
            }
        }
        return R.data(listMap);
    }

    /**
     * 导出当月设备班次oee
     * @return
     */
    @RequestMapping("machoeeExcelExport")
    @ApiOperation(value = "导出oee", notes = "传入targetDay")
    public R machoeeExcelExport(String targetDay){
        Boolean aBoolean = statisMachoeeService.machoeeExcelExport(targetDay);
        if(!aBoolean){
            return R.fail("数据不存在");
        }
        return null;
    }
}
