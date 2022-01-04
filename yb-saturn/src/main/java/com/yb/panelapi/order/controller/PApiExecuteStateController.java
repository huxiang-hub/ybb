package com.yb.panelapi.order.controller;

import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.panelapi.user.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@RestController
@RequestMapping("/plapi/execute")
public class PApiExecuteStateController {
    @Autowired
    private IExecuteStateService stateService;

    /**
     * 只要有操作执行状态表插入一条数据
     */
    @RequestMapping("/state/update")
    R update(ExecuteState executeState){
        if(stateService.save(executeState)){
            return R.ok("添加成功");
        }
        return R.error(400, "添加失败");
    }
    /**
     *
     * 插入记录时间的数据
     * */
//    @GetMapping("/insertTime")
//    @ApiOperation(value = "插入", notes = "时间")
//    public R insertTime(@RequestParam String startAt ,@RequestParam String endAt) {
//
//        int i = stateService.insertByTime(startAt, endAt);
//        if(i>0){
//            return R.ok("添加成功");
//        }
//
//        return R.error(400, "添加失败");
//    }
    /**
     *
     * 保养准备
     * */
    @GetMapping("/selectDayRecord")
    @ApiOperation(value = "插入", notes = "代表年月日季度保养信息")
    public R selectDayRecord(Date start_at) {
       stateService.selectDayRecord(start_at);

        return  R.ok("查询成功");
    }

    /**
     *
     * 订单正式生产接口
     *
     * */
    @PostMapping("/orderFormalProduction")
    public R updateStatus(@RequestBody ExecuteState executeState) {

        int insert = stateService.insert(executeState);

        return  R.ok(insert);
    }

}
