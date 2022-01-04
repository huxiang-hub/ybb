package com.yb.panelapi.exeset.controller;

import com.yb.exeset.entity.ExesetFaultwaste;
import com.yb.exeset.service.IExesetFaultwasteService;
import com.yb.panelapi.exeset.entity.ExesetReadyWaste;
import com.yb.panelapi.exeset.service.ExesetReadyWasteService;
import com.yb.panelapi.exeset.service.IBlindBoxInfoService;
import com.yb.panelapi.user.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "计数设置",tags = "")
@RequestMapping("/plapi")
public class CountSettingController {
    @Autowired
    private IExesetFaultwasteService iExesetFaultwasteService;
    @Autowired
    private IBlindBoxInfoService iBlindBoxInfoService;
    @Autowired
    private ExesetReadyWasteService readyWasteService;
    /**
     * Object : ExesetFaultwaste
     * Table :yb_exeset_faultwaste
     * @param maId
     * @return list
     *  qinbo
     */
    @PostMapping("/getwastecount")
    @ApiOperation(value = "获得所有故障废品数和时间设置",tags = "yb_exeset_faultwaste")
    @ResponseBody
    public R getWasteCountType(Integer maId){
        if (maId ==null){
            return R.error("maId为null!");
        }
        List<ExesetFaultwaste> wastesList =  iExesetFaultwasteService.getExesetFaultwaste(maId);
        if(wastesList==null){
           return R.error("该设备没有找到故障记录！");
        }
        return R.ok(wastesList,"返回该设备的故障废品 时间参数");
    }

    /**
     * Object : ExesetFaultwaste
     * Table :yb_exeset_faultwaste
     * @param waste
     * @return flag == 1 成功  flag == 0 失败
     *  qinbo
     *  remark:
     */
    @PostMapping("/setwastecount")
    @ApiOperation(value = "获得所有故障废品数和时间设置",tags = "yb_exeset_faultwaste")
    @ResponseBody
    public R setFaultwaste(@RequestBody List<ExesetFaultwaste> waste){
        if(waste==null){
            return R.error("请输入有效的数据！");
        }
        /*前端判断是不是数字*/
        for(ExesetFaultwaste e: waste){
            e.setOverTime(e.getOverTime());
            e.setWaste(e.getWaste());
            iExesetFaultwasteService.updateById(e);
        }

        return R.ok(waste,"");
    }

    @PostMapping("/getWaste")
    @ResponseBody
    public R getWaste(Integer maId){

        return R.ok(iBlindBoxInfoService.getExesetReadyWaste(maId));
    }

    @PostMapping("/setWaste")
    @ResponseBody
    public R setWaste(@RequestBody ExesetReadyWaste readyWaste){
        if (readyWaste.getId()!=null) {
            return R.ok(readyWasteService.updateById(readyWaste));
        }else {
            //如果没有的话就保存
            readyWasteService.save(readyWaste);
            return  R.ok();
        }
    }
}
