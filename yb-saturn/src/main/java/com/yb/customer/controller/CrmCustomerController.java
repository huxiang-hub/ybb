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
package com.yb.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.customer.entity.CrmCustomer;
import com.yb.customer.mapper.CrmCustomerMapper;
import com.yb.customer.service.CrmCustomerService;
import com.yb.customer.vo.CrmCustomerVO;
import com.yb.customer.wrapper.CrmCustomerWrapper;
import com.yb.order.service.IOrderOrdinfoService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人事请假管理_yb_staff_dayoff 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/crmCustomer")
@Api(value = "人事请假管理_yb_crm_customer", tags = "人事请假管理_yb_crm_customer接口")
public class CrmCustomerController extends BladeController {

    private CrmCustomerService crmCustomerService;
    private IOrderOrdinfoService orderOrdinfoService;
    private CrmCustomerMapper crmCustomerMapper;


    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入staffDayoff")
    public R<CrmCustomerVO> detail(CrmCustomer crmCustomer) {
        CrmCustomer detail = crmCustomerService.getOne(Condition.getQueryWrapper(crmCustomer));
        CrmCustomerVO crmCustomerVO = CrmCustomerWrapper.build().entityVO(detail);
//        产生一个最新的odNo订单编号
        DateFormat format = new SimpleDateFormat("yyyy");
        String odNo = orderOrdinfoService.getNewOdId(format.format(new Date()) + detail.getCmNo() + "%");
        String no = null;
        if (odNo == null) {
            no = format.format(new Date()) + detail.getCmNo() + "0001";
        } else {
            String od = odNo.substring(0, 8);
            Integer i = Integer.parseInt(odNo.substring(8, 12)) + 1;
            String o = null;
            switch ((i + "").trim().length()) {
                case 1:
                    o = "000" + i;
                    break;
                case 2:
                    o = "00" + i;
                    break;
                case 3:
                    o = "0" + i;
                    break;
                case 4:
                    o = "" + i;
                    break;

            }
            no = od + o;
        }
        crmCustomerVO.setOdNo(no);
        return R.data(crmCustomerVO);
    }

    /**
     * 分页 客户列表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入staffDayoff")
    public R<IPage<CrmCustomerVO>> list(CrmCustomer crmCustomer, Query query) {
        QueryWrapper<CrmCustomer> cm_name = new QueryWrapper<>();
        if (StringUtil.isNoneBlank(crmCustomer.getCmNo())) {
            cm_name.eq("cm_no", crmCustomer.getCmNo());
        }
        if (StringUtil.isNoneBlank(crmCustomer.getCmName())) {
            cm_name.like("cm_name", crmCustomer.getCmName());
        }
        if (StringUtil.isNoneBlank(crmCustomer.getCompany())) {
            cm_name.like("company", crmCustomer.getCompany());
        }
        Page<CrmCustomer> objectPage = new Page<CrmCustomer>();
        objectPage.setSize(query.getSize());
        objectPage.setCurrent(query.getCurrent());
        IPage<CrmCustomer> pages = crmCustomerMapper.selectPage(objectPage, cm_name);
        return R.data(CrmCustomerWrapper.build().pageVO(pages));
    }


    /**
     * 自定义分页 客户列表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入staffDayoff")
    public R<IPage<CrmCustomerVO>> page(CrmCustomerVO staffDayoff, Query query) {
        IPage<CrmCustomerVO> pages = crmCustomerService.selectCrmCustomerPage(Condition.getPage(query), staffDayoff);
        return R.data(pages);
    }

    /**
     * 新增 客户列表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入staffDayoff")
    public R save(@Valid @RequestBody CrmCustomer crmCustomer) {
//        验证客户编号不重复
        Map<String, Object> map = new HashMap<>();
        map.put("cm_no", crmCustomer.getCmNo());
        if (!Func.isEmpty(crmCustomerService.getBaseMapper().selectByMap(map))) {
            return R.fail("客户编号重复");
        }
//        验证社会唯一编号不重复
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("uniqueNo", crmCustomer.getUniqueNo());
        if (!Func.isEmpty(crmCustomerService.getBaseMapper().selectByMap(newMap))) {
            return R.fail("社会唯一编号重复");
        }
        return R.status(crmCustomerService.save(crmCustomer));
    }

    /**
     * 修改 客户列表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入staffDayoff")
    public R update(@Valid @RequestBody CrmCustomer crmCustomer) {
        return R.status(crmCustomerService.updateById(crmCustomer));
    }

    /**
     * 新增或修改 客户列表
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入staffDayoff")
    public R submit(@Valid @RequestBody CrmCustomer crmCustomer) {
        return R.status(crmCustomerService.saveOrUpdate(crmCustomer));
    }


    /**
     * 删除 客户列表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(crmCustomerService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 客户列表
     */
    @GetMapping("/lists")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取客户列表")
    public List<CrmCustomer> lists() {
        List<CrmCustomer> list = crmCustomerService.list();
        return list;
    }
}
