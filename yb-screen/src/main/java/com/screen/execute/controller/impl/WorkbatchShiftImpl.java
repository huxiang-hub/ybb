package com.screen.execute.controller.impl;

import com.screen.execute.controller.IWorkbatchShift;
import com.screen.execute.service.WorkbatchShiftService;
import com.screen.execute.vo.WorkbatchShiftProcessVO;
import com.screen.execute.vo.WorkbatchShiftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/workbactch")
@RestController
@AllArgsConstructor
public class WorkbatchShiftImpl implements IWorkbatchShift {

    @Autowired
    WorkbatchShiftService workbatchShiftService;

    /*****
     * 根据设备id以及当前日期信息，已经下发单为生产完成工单内容
     * @param maId
     * @return
     */
    @GetMapping("/shiftList")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<List<WorkbatchShiftVO>> shiftList(@RequestParam(required = false) Integer maId) {
        System.out.println("======================" + maId);
        List<WorkbatchShiftVO> wklist = workbatchShiftService.getWorkShiftList(maId);

        if (wklist != null && wklist.size() > 0) {
            return R.data(wklist);
        } else {
            return R.fail("没有查到相应排程工单");
        }
    }

    @GetMapping("/shiftDetail")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<WorkbatchShiftProcessVO> shiftDetail(@RequestParam(required = false) Integer wfId) {
        System.out.println("======================" + wfId);
        WorkbatchShiftProcessVO processVO = workbatchShiftService.getShiftDetail(wfId);
        if (processVO != null)
            return R.data(processVO);
        else
            return R.fail("没有查询到对应的数据信息");
    }
}
