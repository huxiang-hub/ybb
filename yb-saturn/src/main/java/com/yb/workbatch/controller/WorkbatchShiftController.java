package com.yb.workbatch.controller;

import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import com.yb.workbatch.vo.WorkbatchShiftVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/WorkbatchShift")
public class WorkbatchShiftController {

    @Autowired
    private WorkbatchShiftService workbatchShiftService;

    /**
     * 锁定或解锁状态,传入id字符串以逗号隔开,和锁定解锁状态
     * @param wfIds
     * @param wfsortIslock
     * @return
     */
    @RequestMapping("shiftLockSort")
    public R shiftLockSort(@RequestParam String wfIds, Integer wfsortIslock){
        return R.status(workbatchShiftService.shiftLockSort(Func.toIntList(wfIds), wfsortIslock));
    }

    @ApiOperation(value = "大屏-根据班次状态查询当天班次完成情况")
    @GetMapping("todyShift")
    public R todyShift(
            @ApiParam(value = "0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）")
            @RequestParam Integer shiftStatus) {
        return R.data(workbatchShiftService.todyShift(shiftStatus));
    }

}
