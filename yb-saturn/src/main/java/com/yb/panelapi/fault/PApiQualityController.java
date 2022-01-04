package com.yb.panelapi.fault;

import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.exeset.entity.ExesetQuality;
import com.yb.exeset.service.IExesetQualityService;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.panelapi.common.TempEntity;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author by SUMMER
 * @date 2020/3/16.
 */
@RestController
@RequestMapping("/plapi/quality")
public class PApiQualityController {

    @Autowired
    private IExesetQualityService qualityService;
    @Autowired
    private IExecuteWasteService wasteService;
    @Autowired
    private IWorkbatchOrdlinkService ordlinkService;
    @Autowired
    private IOrderWorkbatchService workbatchService;

    /**
     *
     */
    @PostMapping("/saveQuality")
    public R saveQuality(@RequestBody ExecuteWasteVO waste) {
        waste.setHandle(1);  //已处理
        waste.setHandleTime(new Date());
        if (wasteService.updateById(waste)) {
            return R.success("保存成功");
        }
        return R.fail("保存失败");
    }

    /**
     * 通过设备id查询当前设备的质量巡检方式
     */
    @GetMapping("/getQuality")
    public R getQualityModel(Integer maId) {
        //通过设备id查询当前设备的工单的停机列表
        List<Map<String, Object>> result = new ArrayList<>();
        String event = "C3";   //体积
        List<TempEntity> temps = ordlinkService.getFaultAndQuality(maId, event);
        ExesetQuality quality = qualityService.getQualityModel(maId);
        Integer limitTime = 2;  //默认2个小时
        if (quality != null && quality.getModel() == 2) {   //计时质检
            limitTime = quality.getLimitTime();
        }
        Integer limitNum = 10000;  //默认一万张质检
        if (quality != null && quality.getModel() == 1) {   //计数质检
            limitNum = quality.getLimitNum();
        }
        for (TempEntity temp : temps) {
            Map<String, Object> map = new HashMap();
            List<ExecuteWasteVO> qualityList = qualityService.getQualityList(temp.getSdId());
            WorkbatchOrdlink ordlink = ordlinkService.getById(temp.getSdId());
            OrderWorkbatchVO workBatch = workbatchService.getWorkBatchByWbId(temp.getWbId());
            map.put("qualityList", qualityList);
            map.put("orderLink", ordlink);
            map.put("workBatch", workBatch);
            map.put("limitTime", limitTime);
            map.put("limitNum", limitNum);
            result.add(map);
        }
        return R.data(result);
    }
}
