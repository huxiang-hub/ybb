package com.anaysis.controller;

import com.anaysis.entity.WorkbatchOrdlink;
import com.anaysis.service.impl.MachineMainfoServiceImpl;
import com.anaysis.service.impl.ProcessWorkinfoServiceImpl;
import com.anaysis.service.impl.WorkbatchOrdlinkServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzb
 * @Date 2020/11/25 11:22
 **/
@Api(tags = "排产单导入")
@RestController
@RequestMapping("/wbol")
public class WorkbatchOrdlinkController {
    @Autowired
    private WorkbatchOrdlinkServiceImpl workbatchOrdlinkService;
    @Autowired
    private ProcessWorkinfoServiceImpl processWorkinfoService;
    @Autowired
    private MachineMainfoServiceImpl machineMainfoService;


    @ApiOperation("同步工序、设备、排产单")
    @GetMapping("/sync")
    public R sync() {
        processWorkinfoService.sync();
        machineMainfoService.sync();
        workbatchOrdlinkService.sync();
        return R.success("同步成功");
    }

    @ApiOperation("查询未导入工序、设备、排产单")
    @GetMapping("/proce-mach-work/notImport")
    public R notImport() {
        List<String> processNotImport = processWorkinfoService.notImport();
        List<String> machineNotImport = machineMainfoService.notImport();
        List<String> workOrderNotImport = workbatchOrdlinkService.notImport();
        HashMap<String, List<String>> notImport = new HashMap<>(1);
        notImport.put("processNotImport", processNotImport);
        notImport.put("machineNotImport", machineNotImport);
        notImport.put("workOrderNotImport", workOrderNotImport);
        return R.data(notImport);
    }

    @ApiOperation("同步工序、设备")
    @GetMapping("/syncProcMachine")
    public R syncProcMachine() {
        processWorkinfoService.sync();
        machineMainfoService.sync();
        return R.success("同步成功");
    }

    @ApiOperation("查询未同步排产工单")
    @GetMapping("notImportWorkPlan")
    public R notImportWorkPlan() {
        List<String> workOrderNotImport = workbatchOrdlinkService.notImport();
        List<WorkbatchOrdlink> list = workbatchOrdlinkService.notImportWorkPlan(workOrderNotImport);
        if (null == list) {
            return R.data(null);
        }
        List<String> collect = list.stream().map(WorkbatchOrdlink::getOdNo).collect(Collectors.toList());
        return R.data(collect);
    }
}
