/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.vo.EquipmentVO;
import com.yb.execute.vo.ExecuteStateParamVO;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.wrapper.ExecuteStateWrapper;
import com.yb.statis.vo.StatisMachsingleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 执行表状态_yb_execute_state 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executestate")
@Api(value = "执行表状态_yb_execute_state", tags = "执行表状态_yb_execute_state接口")
public class ExecuteStateController extends BladeController {

    private IExecuteStateService executeStateService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executeState")
    public R<ExecuteStateVO> detail(ExecuteState executeState) {
        ExecuteState detail = executeStateService.getOne(Condition.getQueryWrapper(executeState));
        return R.data(ExecuteStateWrapper.build().entityVO(detail));
    }

    /**
     * 分页 执行表状态_yb_execute_state
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeState")
    public R<IPage<ExecuteStateVO>> list(ExecuteState executeState, Query query) {
        IPage<ExecuteState> pages = executeStateService.page(Condition.getPage(query), Condition.getQueryWrapper(executeState));
        return R.data(ExecuteStateWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 执行表状态_yb_execute_state
     */
    @GetMapping("/executeStatePage")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeState")
    public R<IPage<ExecuteStateVO>> page(ExecuteStateVO executeState, Query query) {
        IPage<ExecuteStateVO> pages = executeStateService.selectExecuteStatePage(Condition.getPage(query), executeState);
        return R.data(pages);
    }

    /**
     * 新增 执行表状态_yb_execute_state
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executeState")
    public R save(@Valid @RequestBody ExecuteState executeState) {
        return R.status(executeStateService.save(executeState));
    }

    /**
     * 修改 执行表状态_yb_execute_state
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executeState")
    public R update(@Valid @RequestBody ExecuteState executeState) {
        return R.status(executeStateService.updateById(executeState));
    }

    /**
     * 新增或修改 执行表状态_yb_execute_state
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executeState")
    public R submit(@Valid @RequestBody ExecuteState executeState) {
        return R.status(executeStateService.saveOrUpdate(executeState));
    }


    /**
     * 删除 执行表状态_yb_execute_state
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeStateService.removeByIds(Func.toIntList(ids)));
    }
    /**
     * 生产入库上报 执行表状态_yb_execute_state
     */
    /*@PostMapping("/selectProductReport")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询", notes = "传入id")
    public R selectProductReport( @RequestParam Integer  id) {
        return R.data(executeStateService.selectProductReport(id));
    }*/

    @PostMapping("/getExcuteStateDetailBysdId")
    @ApiOperation(value = "根据sdId查询sdId所有的组件", notes = "传入sdId")
    public R getExcuteStateDetailBysdId( @RequestParam String sdId,@RequestParam String maId) {
        if (sdId==null || sdId==""){
          return  R.fail("sdId不正确");
        }
        System.out.println(maId+"-------------------------");
        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
      List<ExecuteStateVO> executeStateList = executeStateService.getExcuteStateDetailBysdId(sdId,maId);
        for (ExecuteStateVO executeStateVO:executeStateList) {
            //接单B1保养B2换模B3正式生产C1停机C2质检C3结束生产D1生产上报D2
            // 接单B1 #F19149
            if (executeStateVO.getEvent().equals("B1")){
                executeStateVO.setProduceName("接单");
                executeStateVO.setProduceType("start");
                executeStateVO.setProduceTime(sdf.format(executeStateVO.getStartAt()));
                executeStateVO.setProduceColor("#F19149");
            }
            // 保养 #F8B551
            if (executeStateVO.getEvent().equals("B2")){
                executeStateVO.setProduceName("保养");
                executeStateVO.setProduceType("pre");
                String start= sdf.format(executeStateVO.getStartAt());
                String end = sdf.format(executeStateVO.getEndAt());
                executeStateVO.setProduceTime(start+"-"+end);
                executeStateVO.setProduceColor("#F8B551");
            }
            // 换膜 #A84200
            if (executeStateVO.getEvent().equals("B3")){
                executeStateVO.setProduceName("换膜");
                executeStateVO.setProduceType("pre");
                String start= sdf.format(executeStateVO.getStartAt());
                String end = sdf.format(executeStateVO.getEndAt());
                executeStateVO.setProduceTime(start+"-"+end);
                executeStateVO.setProduceColor("#A84200");
            }

            // 正式生产 #32B16C
            if (executeStateVO.getEvent().equals("C1")){
                executeStateVO.setProduceName("正式生产");
                executeStateVO.setProduceType("pro");
                String start= sdf.format(executeStateVO.getStartAt());
                String end = sdf.format(executeStateVO.getEndAt());
                executeStateVO.setProduceTime(start+"-"+end);
                executeStateVO.setProduceColor("#32B16C");
            }

            // 停机 #E60012
            if (executeStateVO.getEvent().equals("C2")){
                executeStateVO.setProduceName("停机");
                executeStateVO.setProduceType("pro");
                String start= sdf.format(executeStateVO.getStartAt());
                String end = sdf.format(executeStateVO.getEndAt());
                executeStateVO.setProduceTime(start+"-"+end);
                executeStateVO.setProduceColor("#E60012");
            }

            // 质检 #00B7EE
            if (executeStateVO.getEvent().equals("C3")){
                executeStateVO.setProduceName("质检");
                executeStateVO.setProduceType("pro");
                String start= sdf.format(executeStateVO.getStartAt());
                executeStateVO.setProduceTime(start);
                executeStateVO.setProduceColor("#00B7EE");
            }
            // 结束生产 #556FB5
            if (executeStateVO.getEvent().equals("D1")){
                executeStateVO.setProduceName("结束生产");
                executeStateVO.setProduceType("end");
                String start= sdf.format(executeStateVO.getStartAt());
                executeStateVO.setProduceTime(start);
                executeStateVO.setProduceColor("#556FB5");
            }
            // 生产上报 #8957A1
            if (executeStateVO.getEvent().equals("D2")){
                executeStateVO.setProduceName("生产上报");
                executeStateVO.setProduceType("end");
                String start= sdf.format(executeStateVO.getStartAt());
                executeStateVO.setProduceTime(start);
                executeStateVO.setProduceColor("#8957A1");
            }
        }
        return R.data(executeStateList);
    }


    /**
     * 分页 执行表状态_yb_execute_state
     */
    @PostMapping("/stateTimeList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "时序图接口", notes = "executeStateVO")
    public R<List<EquipmentVO>> stateList(@RequestBody ExecuteStateParamVO executeStateParamVO) {
        List<EquipmentVO> executeStateVOList = executeStateService.stateList(executeStateParamVO);
        Integer wsId = executeStateParamVO.getWsId();
        if(executeStateVOList.isEmpty() && wsId != null){
            return R.fail("所选班次数据不存在");
        }
        return R.data(executeStateVOList);
    }

    @PostMapping("/stateMachineList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询所有符合要求的设备名称", notes = "executeStateParamVO")
    public R stateMachineList(@RequestBody ExecuteStateParamVO executeStateParamVO) {

        return R.data(executeStateService.stateMachineList(executeStateParamVO));
    }
}
