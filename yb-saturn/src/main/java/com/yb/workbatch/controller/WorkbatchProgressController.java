package com.yb.workbatch.controller;

import com.yb.workbatch.service.WorkbatchProgressService;
import com.yb.workbatch.vo.WorkbatchProgressVO;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WorkbatchProgress")
public class WorkbatchProgressController {

    @Autowired
    private WorkbatchProgressService workbatchProgressService;
    @RequestMapping("/page")
    public R page(WorkbatchProgressVO workbatchProgressVO, Query query){
        return R.data(workbatchProgressService.selectWorkbatchProgressPage(Condition.getPage(query), workbatchProgressVO));
    }
    @RequestMapping("/workbatchProgressPage")
    public R workbatchProgressPage(WorkbatchProgressVO workbatchProgressVO, Query query){
        return R.data(workbatchProgressService.workbatchProgressPage(Condition.getPage(query), workbatchProgressVO));
    }
}
