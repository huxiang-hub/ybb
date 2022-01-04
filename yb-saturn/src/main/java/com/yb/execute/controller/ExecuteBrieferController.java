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
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.vo.*;
import com.yb.execute.wrapper.ExecuteBrieferWrapper;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdPdinfoService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 生产执行上报信息_yb_execute_briefer 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executebriefer")
@Api(value = "生产执行上报信息_yb_execute_briefer", tags = "生产执行上报信息_yb_execute_briefer接口")
public class ExecuteBrieferController extends BladeController {
    @Autowired
    private IExecuteBrieferService executeBrieferService;
    private IOrderOrdinfoService orderOrdinfoService;
    private IProdPdinfoService prodPdinfoService;
    private IProdPartsinfoService prodPartsinfoService;
    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executeBriefer")
    public R<ExecuteBrieferVO> detail(ExecuteBriefer executeBriefer) {
        ExecuteBriefer detail = executeBrieferService.getOne(Condition.getQueryWrapper(executeBriefer));

        return R.data(ExecuteBrieferWrapper.build().entityVO(detail));
    }

    /**
     * 分页 生产执行上报信息_yb_execute_briefer
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeBriefer")
    public R<IPage<ExecuteBrieferVO>> list(Query query, ExecuteBrieferVO executeBrieferVO) {
        Integer courrent = (query.getCurrent()-1)*query.getSize();
        IPage<ExecuteBrieferVO>  page= executeBrieferService.pageFindList(courrent, query.getSize(), executeBrieferVO);
        return R.data(page);

    }

    /**
     * 新增 生产执行上报信息_yb_execute_briefer
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executeBriefer")
    public R save(@Valid @RequestBody ExecuteBriefer executeBriefer) {
        return R.status(executeBrieferService.save(executeBriefer));
    }

    /**
     * 修改 生产执行上报信息_yb_execute_briefer
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executeBriefer")
    public R update(@Valid @RequestBody ExecuteBriefer executeBriefer) {
        return R.status(executeBrieferService.updateById(executeBriefer));
    }

    /**
     * 新增或修改 生产执行上报信息_yb_execute_briefer
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executeBriefer")
    public R submit(@Valid @RequestBody ExecuteBriefer executeBriefer) {
        return R.status(executeBrieferService.saveOrUpdate(executeBriefer));
    }


    /**
     * 删除 生产执行上报信息_yb_execute_briefer
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeBrieferService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 查询对应作业批次的对应部件生产状况
     */
    @GetMapping("/detailBatchNo")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "批次的对应部件生产状况详情", notes = "传入orderWorkbatchVO")
    public R<List<ProdPartsinfoVo>> detailBatchNo(OrderWorkbatchVO orderWorkbatchVO) {
//        1查询订单对应部件信息集合
        OrderOrdinfoVO detail = orderOrdinfoService.getOneById(orderWorkbatchVO.getOdId());
        //        查询对应产品信息
        ProdPdinfoVO pdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(detail.getPdId(),1);
//        2查询部件对应的工单集合，与通过批次id，部件id，工序id与sort对应工单以及工单的生产详情
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
//            查入批次ID
            partsinfoVo.setWbId(orderWorkbatchVO.getId());
            List<ExecuteBriefer> list = executeBrieferService.detailBatchNo(partsinfoVo);
            partsinfoVo.setExecuteBrieferList(list);
        }
        return R.data(prodPartsinfoVoList);
    }

    @GetMapping("/getExecuteBrieferList")
    @ApiOperation(value = "获取本设备对应日期班次的上报数据")
    public R<List<ReportedVO>> getExecuteBrieferList(@ApiParam(value = "日期(yyyy-MM-dd)")@RequestParam("targetDay") String targetDay,
                                                     @ApiParam(value = "设备id")@RequestParam("maId")Integer maId,
                                                     @ApiParam(value = "班次id")@RequestParam("wsId")Integer wsId){

        List<ReportedVO> reportedVOList = executeBrieferService.getExecuteBrieferList(targetDay, maId, wsId);
        return R.data(reportedVOList);
    }
    @GetMapping("/getExecuteBrieferDetail")
    @ApiOperation(value = "机台上报列表详情")
    public R<ExecuteBrieferDetailVO> getExecuteBrieferDetail(@ApiParam(value = "上报列表id")@RequestParam("id") Integer id){
        return R.data(executeBrieferService.getExecuteBrieferDetail(id));
    }

    @PostMapping("/notExecuteBrieferList")
    @ApiOperation(value = "查询未上报的数据")
    public R<IPage<ExecuteExamineVO>> notExecuteBrieferList(Query query, NotExecuteBrieferRequest notExecuteBrieferRequest){
        return R.data(executeBrieferService.notExecuteBrieferList(notExecuteBrieferRequest, Condition.getPage(query)));
    }

}
