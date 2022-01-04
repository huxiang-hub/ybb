package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.service.StatisDayreachService;
import com.yb.statis.vo.DayreachParmsVO;
import com.yb.statis.vo.StatisDayreachVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/StatisDayreach")
@Api(tags = "车间计划达成率日报_yb_statis_dayreach")
public class StatisDayreachController {

    @Autowired
    private StatisDayreachService statisDayreachService;

    /**
     * 导出excel
     * @param dayreachParmsVO
     * @return
     */
    @RequestMapping("exportDayreach")
    @ApiOperation(value = "导出达成率日报excel", notes = "传入dayreachParmsVO")
    public R exportDayreach(@RequestBody DayreachParmsVO dayreachParmsVO){
//    public R exportDayreach(String targetDay, Integer maType){
//        DayreachParmsVO dayreachParmsVO = new DayreachParmsVO();
//        dayreachParmsVO.setTargetDay(targetDay);
//        dayreachParmsVO.setMaType(maType);
        Integer i = statisDayreachService.exportDayreach(dayreachParmsVO);
        if(i == null){
            return R.fail("数据不存在");
        }
        return null;
    }


    @RequestMapping("statisDayreachPage")
    @ApiOperation(value = "分页查询达成率日报", notes = "")
    public R<IPage<StatisDayreachVO>> statisDayreachPage(StatisDayreachVO statisDayreachVO, Query query){
        return R.data(statisDayreachService.statisDayreachPage(statisDayreachVO, Condition.getPage(query)));
    }

    @ApiOperation(value = "重新生成达成率", notes = "传入需要重新生成达成率的日期targetDay")
    @RequestMapping("timedTaskStatisDayreach")
    public R timedTaskStatisDayreach(String targetDay){
        return R.status(statisDayreachService.timedTaskStatisDayreach(targetDay));
    }


}
