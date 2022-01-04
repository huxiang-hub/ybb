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
package com.yb.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.order.wrapper.OrderWorkbatchWrapper;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdPdinfoService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.system.user.entity.SaUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 作业批次_yb_order_workbatch 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderworkbatch")
@Api(value = "作业批次_yb_order_workbatch", tags = "作业批次_yb_order_workbatch接口")
public class OrderWorkbatchController extends BladeController {

    private IOrderWorkbatchService orderWorkbatchService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入orderWorkbatch")
    public R<OrderWorkbatchVO> detail(OrderWorkbatch orderWorkbatch) {
        OrderWorkbatch detail = orderWorkbatchService.getOne(Condition.getQueryWrapper(orderWorkbatch));
        return R.data(OrderWorkbatchWrapper.build().entityVO(detail));
    }

    /**
     * 分页 作业批次_yb_order_workbatch
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入orderWorkbatch")
    public R<IPage<OrderWorkbatchVO>> list(OrderWorkbatch orderWorkbatch, Query query) {
        IPage<OrderWorkbatch> pages = orderWorkbatchService.page(Condition.getPage(query), Condition.getQueryWrapper(orderWorkbatch));
        return R.data(OrderWorkbatchWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 作业批次_yb_order_workbatch
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入orderWorkbatch")
    public R<IPage<OrderWorkbatchVO>> page(OrderWorkbatchVO orderWorkbatch, Query query) {
        log.info("日志来了");
        IPage<OrderWorkbatchVO> pages = orderWorkbatchService.selectOrderWorkbatchPage(Condition.getPage(query), orderWorkbatch);
        return R.data(pages);
    }

    /**
     * 新增 作业批次_yb_order_workbatch
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入orderWorkbatch")
    public R save(@Valid @RequestBody OrderWorkbatch orderWorkbatch) {
        SaUser user = SaSecureUtil.getUser();//获取登录着的信息
        if (Func.isEmpty(user)){
            return R.fail("请重新登陆");
        }
        orderWorkbatch.setUserId(user.getUserId());
//        生成批次编号
        OrderWorkbatch workbatch = orderWorkbatchService.getNewest(orderWorkbatch.getOdId());
//        查询最后一条批次编号，如果没有，则生成1
        if (Func.isEmpty(workbatch)){
            orderWorkbatch.setBatchNo(orderWorkbatch.getOdNo()+"_1");
        }else{
//        有则最后一天末尾数字+1组装
            String no[] = workbatch.getBatchNo().split("_");
            orderWorkbatch.setBatchNo(orderWorkbatch.getOdNo()+"_"+(Integer.parseInt(no[1])+1));
        }
        orderWorkbatch.setCreateAt(new Date());
        orderWorkbatch.setStatus(1);
        return R.status(orderWorkbatchService.save(orderWorkbatch));
    }

    /**
     * 修改 作业批次_yb_order_workbatch
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入orderWorkbatch")
    public R update(@Valid @RequestBody OrderWorkbatch orderWorkbatch) {
        return R.status(orderWorkbatchService.updateById(orderWorkbatch));
    }

    /**
     * 新增或修改 作业批次_yb_order_workbatch
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入orderWorkbatch")
    public R submit(@Valid @RequestBody OrderWorkbatch orderWorkbatch) {
        return R.status(orderWorkbatchService.saveOrUpdate(orderWorkbatch));
    }

    /**
     * 新增或修改 作业批次_yb_order_workbatch
     */
    @GetMapping("/updateByIds")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入orderWorkbatch")
    @Transactional
    public R updateByIds(String ids, Integer status) {
        try {
            for (Integer id : Func.toIntList(ids)) {
                OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(id);
                orderWorkbatch.setStatus(status);
                orderWorkbatchService.saveOrUpdate(orderWorkbatch);
            }
            return R.success("下发成功");
        } catch (Exception e) {
            return R.fail("下发失败");
        }
    }


    /**
     * 删除 作业批次_yb_order_workbatch
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(orderWorkbatchService.removeByIds(Func.toIntList(ids)));
    }
    @RequestMapping("/batchNumberList")
    @ApiOperation(value = "查询已排产工序数量", notes = "prId, ptId, wbId")
    public R<List<OrderWorkbatchVO>> batchNumberList(Integer prId, Integer ptId, Integer wbId)  {
        return R.data(orderWorkbatchService.batchNumberList(prId, ptId, wbId));
    }

}
