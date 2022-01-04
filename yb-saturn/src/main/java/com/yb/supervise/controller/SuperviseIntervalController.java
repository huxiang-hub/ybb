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
package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.supervise.entity.SuperviseInterval;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.supervise.vo.SuperviseIntervalVO;
import com.yb.supervise.vo.SuperviseTowMonthVO;
import com.yb.supervise.vo.queryInterval;
import com.yb.supervise.wrapper.SuperviseIntervalWrapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 设备状态间隔表interval- 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/superviseinterval")
@Api(value = "设备状态间隔表interval-", tags = "设备状态间隔表interval-接口")
public class SuperviseIntervalController extends BladeController {

    private ISuperviseIntervalService superviseIntervalService;

    private IWorkbatchShiftsetService workbatchShiftsetService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseInterval")
    public R<SuperviseIntervalVO> detail(SuperviseInterval superviseInterval) {
        SuperviseInterval detail = superviseIntervalService.getOne(Condition.getQueryWrapper(superviseInterval));
        return R.data(SuperviseIntervalWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备状态间隔表interval-视图
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入superviseInterval")
    public R<IPage<SuperviseIntervalVO>> list(SuperviseInterval superviseInterval, Query query) {
        IPage<SuperviseInterval> pages = superviseIntervalService.page(Condition.getPage(query), Condition.getQueryWrapper(superviseInterval));
        return R.data(SuperviseIntervalWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备状态间隔表interval-视图
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseInterval")
    public R<IPage<SuperviseIntervalVO>> page(SuperviseIntervalVO superviseInterval, Query query) {
        IPage<SuperviseIntervalVO> pages = superviseIntervalService.selectSuperviseIntervalPage(Condition.getPage(query), superviseInterval);
        return R.data(pages);
    }

    /**
     * 新增 设备状态间隔表interval-视图
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseInterval")
    public R save(@Valid @RequestBody SuperviseInterval superviseInterval) {
        return R.status(superviseIntervalService.save(superviseInterval));
    }

    /**
     * 修改 设备状态间隔表interval-视图
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseInterval")
    public R update(@Valid @RequestBody SuperviseInterval superviseInterval) {
        return R.status(superviseIntervalService.updateById(superviseInterval));
    }

    /**
     * 新增或修改 设备状态间隔表interval-视图
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseInterval")
    public R submit(@Valid @RequestBody SuperviseInterval superviseInterval) {
        return R.status(superviseIntervalService.saveOrUpdate(superviseInterval));
    }


    /**
     * 删除 设备状态间隔表interval-视图
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseIntervalService.removeByIds(Func.toIntList(ids)));
    }


    @PostMapping("/getCalculateDayResult")
    @ApiOperation(value = "获取统计的结果", notes = "传入日期和当前统计的分钟")
    public R getCalculateDayResult(String targetDate, String dpId, String proType) {
        //进行验证
        if (targetDate == null) {
            return R.fail("日期时间不能为null");
        }
        //这里对day 进行处理
        Date date = null;
        String targetDateStr = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        try {
            date = sdf.parse(targetDate);
            targetDateStr = sdfMonth.format(date);
        } catch (Exception e) {
            return R.fail("日期时间有错误");
        }
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseIntervalService.getDayResultByCheckDay(targetDateStr, dpId, proType);
        Map<Integer, List<SuperviseIntervalVO>> superviseList = new HashMap<>();
        for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOS) {
            Integer key = superviseIntervalVO.getMaId();
            if (superviseList.get(key) == null) {
                List<SuperviseIntervalVO> list = new ArrayList<>();
                list.add(superviseIntervalVO);
                superviseList.put(key, list);
            } else {
                superviseList.get(key).add(superviseIntervalVO);
            }
        }
        return R.data(superviseList);
    }

    @PostMapping("/getCalculateHourResult")
    @ApiOperation(value = "获取统计的结果", notes = "传入日期和当前统计的分钟")
    public R getCalculateHourResult(String targetDate, String dpId, String proType, String wsId) throws ParseException {
        //进行验证
        if (targetDate == null) {
            return R.fail("日期时间不能为null");
        }

        if (wsId == null || wsId.length() <= 0 || "undefined".equalsIgnoreCase(wsId)) {
            WorkbatchShiftset workbatchShiftset = workbatchShiftsetService.getDefaultWsid();
            if (workbatchShiftset != null && workbatchShiftset.getWsId() != null)
                wsId = workbatchShiftset.getWsId().toString();
        }

        List<SuperviseIntervalVO> superviseIntervalVOS = null;
//            WorkbatchShiftset workbatchShiftset = workbatchShiftsetService.getById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetService.selectByMaid(Integer.parseInt(wsId), null);//需要有设备id，暂时没有

        Integer startHour = workbatchShiftset.getStartTime().getHours();
        Integer endHour = workbatchShiftset.getEndTime().getHours();
        if (startHour < endHour) {
            superviseIntervalVOS = superviseIntervalService.getHourResultByCheckDay(targetDate, dpId, proType, startHour, endHour);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(targetDate);
            date.setDate(date.getDate() + 1);
            String tomorrowDay = simpleDateFormat.format(date);
            superviseIntervalVOS = superviseIntervalService.getHourResutPassDay(targetDate, tomorrowDay, dpId, proType, startHour, endHour);
        }
        Map<Integer, List<SuperviseIntervalVO>> superviseList = new HashMap<>();
        for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOS) {
            Integer key = superviseIntervalVO.getMaId();
            if (superviseList.get(key) == null) {
                List<SuperviseIntervalVO> list = new ArrayList<>();
                list.add(superviseIntervalVO);
                superviseList.put(key, list);
            } else {
                superviseList.get(key).add(superviseIntervalVO);
            }
        }
        return R.data(superviseList);
    }


    @PostMapping("/getCalculateWeekResult")
    @ApiOperation(value = "获取统计的结果", notes = "按照周去统计数据")
    public R getCalculateWeekResult(String targetDate, String dpId, String proType) throws ParseException {
        //进行验证
        if (targetDate == null) {
            return R.fail("日期时间不能为null");
        }
        if ("1".equals(targetDate)) {
            // 为1代表当天
            targetDate = LocalDate.now().toString();
        }
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseIntervalService.getWeekResultByCheckDay(targetDate, dpId, proType);
        Map<Integer, List<SuperviseIntervalVO>> superviseList = new HashMap<>();
        if (superviseList != null) {
            for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOS) {
                Integer key = superviseIntervalVO.getMaId();
                if (superviseList.get(key) == null) {
                    List<SuperviseIntervalVO> list = new ArrayList<>();
                    list.add(superviseIntervalVO);
                    superviseList.put(key, list);
                } else {
                    superviseList.get(key).add(superviseIntervalVO);
                }
            }
            return R.data(superviseList);
        }
        return R.data(new HashMap<>());
    }

    @RequestMapping("/getCalculateCkNameResult")
    @ApiOperation(value = "获取统计的结果", notes = "按照班次去统计数据")
    public R getCalculateCkNameResult(String targetDate, String dpId, String proType) {
        //进行验证
        if (targetDate == null) {
            targetDate = DateTimeUtil.format(new Date(), DateUtil.PATTERN_DATE);
        }
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseIntervalService.getCalculateCkNameResult(targetDate, dpId, proType);
        Map<Integer, List<SuperviseIntervalVO>> superviseList = new HashMap<>();
        if (superviseList != null) {
            for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOS) {
                Integer key = superviseIntervalVO.getMaId();
                if (superviseList.get(key) == null) {
                    List<SuperviseIntervalVO> list = new ArrayList<>();
                    list.add(superviseIntervalVO);
                    superviseList.put(key, list);
                } else {
                    superviseList.get(key).add(superviseIntervalVO);
                }
            }
        }
        return R.data(superviseList);
    }

    @RequestMapping("/getLineData")
    @ApiOperation(value = "获取统计的结果", notes = "按照班次去统计数据")
    public R getLineData(@RequestBody queryInterval queryInterval) {
        Map<String, Object> map = new HashMap<>();
//        查询有哪些设备
        List<Integer> maList = superviseIntervalService.getLineDataMa(queryInterval);
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseIntervalService.getLineData(queryInterval);
        map.put("maList", maList);
        map.put("superviseIntervalVOS", superviseIntervalVOS);
        return R.data(map);
    }

    @PostMapping("/getCalculateMonthResult")
    @ApiOperation(value = "获取统计的结果", notes = "按照月去统计数据")
    public R getCalculateMonthResult(String targetDate, String dpId, String proType) throws ParseException {
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseIntervalService.getMonthResultByCheckDay(targetDate, dpId, proType);
        Map<Integer, List<SuperviseIntervalVO>> superviseList = new HashMap<>();
        if (superviseIntervalVOS != null && superviseIntervalVOS.size() > 0) {
            for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOS) {
                Integer key = superviseIntervalVO.getMaId();
                if (superviseList.get(key) == null) {
                    List<SuperviseIntervalVO> list = new ArrayList<>();
                    list.add(superviseIntervalVO);
                    superviseList.put(key, list);
                } else {
                    superviseList.get(key).add(superviseIntervalVO);
                }
            }
            superviseList = (superviseList == null) ? new HashMap<>() : superviseList;
        }
        return R.data(superviseList);
    }

    @GetMapping("/productivityOfToday")
    @ApiOperation(value = "大屏-获取设备当天产能", notes = "返回当天开始时间到第二天开始时间设备产能key(设备名称)-value(产能)")
    public R productivityOfToday() {
        List<Map<String, Integer>> map = superviseIntervalService.productivityOfToday();
        return R.data(map);
    }

    @GetMapping("/productivityOfTowMonth")
    @ApiOperation(value = "大屏-根据设备类型分别统计近两个月设备生产数量")
    public R productivityOfTowMonth(String maType) {
        List<SuperviseTowMonthVO> list = superviseIntervalService.productivityOfTowMonth(maType);
        return R.data(list);
    }
}
