package com.yb.supervise.controller;

import com.yb.supervise.service.SuperviseIntervalalgService;
import com.yb.supervise.vo.SuperviseIntervalalgEventVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SuperviseIntervalalg")
@Api(tags = "设备数据记录表_yb_supervise_intervalalg")
public class SuperviseIntervalalgController {

    @Autowired
    private SuperviseIntervalalgService superviseIntervalalgService;

    @PostMapping("/createEvent")
    @ApiOperation(value = "创建事件")
    public R createEvent(@RequestBody SuperviseIntervalalgEventVO superviseIntervalalgEventVO) {
        try {
            superviseIntervalalgService.createEvent(superviseIntervalalgEventVO);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("创建失败");
        }
        return R.success("创建成功");
    }
    @PostMapping("/deleteSuperviseIntervalalg")
    @ApiOperation(value = "删除过期数据")
    public R deleteSuperviseIntervalalg(){
        try {
            superviseIntervalalgService.deleteSuperviseIntervalalg();
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("操作失败");
        }
        return R.success("删除成功");
    }


}
