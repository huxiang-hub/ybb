package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.statis.entity.StatisShiftreach;
import com.yb.statis.request.ShiftReachExeinfoRequest;
import com.yb.statis.service.StatisShiftreachService;
import com.yb.statis.vo.GetShiftreachVO;
import com.yb.statis.vo.ShiftreachListVO;
import com.yb.statis.vo.StatisShiftreachVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/StatisShiftreach")
@Api(tags = "统计班次达成率_yb_statis_shiftreach")
public class StatisShiftreachController {

    @Autowired
    private StatisShiftreachService statisShiftreachService;

    @Autowired
    private IExecuteInfoService iExecuteinfo;


    @RequestMapping("list")
    @ApiOperation(value = "分页查询班次达成率", notes = "")
    public R<IPage<StatisShiftreachVO>> list(StatisShiftreachVO statisShiftreachVO, Query query) {

        IPage<StatisShiftreachVO> pages = statisShiftreachService.statisShiftreachList(Condition.getPage(query), statisShiftreachVO);
        return R.data(pages);
    }

    /**
     * 导出达成率统计
     *
     * @param targetDay
     * @param maType
     * @param wsId
     * @return
     */
    @RequestMapping("/statisShiftreachExcelExport")
    @ApiOperation(value = "导出保存达成率Excel", notes = "传入targetDay 日期, maType 设备类型, wsId 班次id")
    public R statisShiftreachExcelExport(String targetDay, Integer maType, Integer wsId) {
        Integer i = statisShiftreachService.statisShiftreachExcelExport(targetDay, maType, wsId);
        if (i == null) {
            return R.fail("没有数据可导出");
        }
        return null;
    }

    @RequestMapping("timedTaskStatisShiftreach")
    @ApiOperation(value = "重新生成班次达成率", notes = "传入targetDay 日期, wsId 班次id")
    public R timedTaskStatisShiftreach(String targetDay, Integer wsId) {
        return R.status(statisShiftreachService.timedTaskStatisShiftreach(targetDay, wsId));
    }


    @PostMapping("/getShiftreachList")
    @ApiOperation(value = "获取设备班次达成率")
    public R<List<ShiftreachListVO>> getShiftreachList(@RequestBody GetShiftreachVO getShiftreachVO) {
        if (StringUtil.isEmpty(getShiftreachVO.getEndTime())) {
            getShiftreachVO.setEndTime(null);
        }
        List<ShiftreachListVO> shiftreachListVOS = statisShiftreachService.getShiftreachList(getShiftreachVO);
        return R.data(shiftreachListVOS);
    }

    /******
     * 执行单_yb_execute_info 数据的关联查询
     * @param
     * @return
     */
    @PostMapping("/getReachExecuteInfo")
    @ApiOperation(value = "根据wfId排程ID查询执行单详情情况，是否上报等相关数据")
    public R<List<ExecuteInfoVO>> getReachExecuteInfo(@RequestBody ShiftReachExeinfoRequest request) {
        List<ExecuteInfoVO> executeInfolist = iExecuteinfo.getReachExecuteInfo(request);
        return R.data(executeInfolist);
    }


    @GetMapping("/shiftreachExcelExport")
    @ApiOperation(value = "导出设备班次达成率")
    public R shiftreachExcelExport(GetShiftreachVO getShiftreachVO) {
        if (StringUtil.isEmpty(getShiftreachVO.getEndTime())) {
            getShiftreachVO.setEndTime(null);
        }
        Integer i = statisShiftreachService.shiftreachExcelExport(getShiftreachVO);
        if (i == null) {
            return R.fail("没有数据可导出");
        }
        return null;
    }

    @PostMapping("/getShiftreachTotal")
    @ApiOperation(value = "获取计划达成率班次合计")
    public R<List<ShiftreachListVO>> getShiftreachTotal(@RequestBody GetShiftreachVO getShiftreachVO) {
        if (StringUtil.isEmpty(getShiftreachVO.getEndTime())) {
            getShiftreachVO.setEndTime(null);
        }
        List<ShiftreachListVO> shiftreachListVOList =
                statisShiftreachService.getShiftreachTotal(getShiftreachVO);
        return R.data(shiftreachListVOList);
    }
}
