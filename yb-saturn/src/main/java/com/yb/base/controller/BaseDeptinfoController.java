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
package com.yb.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.base.vo.DeptNameModel;
import com.yb.base.wrapper.BaseDeptinfoWrapper;
import com.yb.machine.entity.MachineMainfo;
import io.protostuff.Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门结构_yb_ba_dept 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basedeptinfo")
@Api(value = "部门结构_yb_ba_dept", tags = "部门结构_yb_ba_dept接口")
public class BaseDeptinfoController extends BladeController {

    private IBaseDeptinfoService baseDeptinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入baseDeptinfo")
    public R<BaseDeptinfoVO> detail(BaseDeptinfo baseDeptinfo) {
        BaseDeptinfo detail = baseDeptinfoService.getOne(Condition.getQueryWrapper(baseDeptinfo));
        return R.data(BaseDeptinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 部门结构_yb_ba_dept
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseDeptinfo")
    public R<IPage<BaseDeptinfoVO>> list(BaseDeptinfo baseDeptinfo, Query query) {
        IPage<BaseDeptinfo> pages = baseDeptinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(baseDeptinfo));
        return R.data(BaseDeptinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 部门结构_yb_ba_dept
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入baseDeptinfo")
    public R<IPage<BaseDeptinfoVO>> page(BaseDeptinfoVO baseDeptinfo, Query query) {
        IPage<BaseDeptinfoVO> pages = baseDeptinfoService.selectBaseDeptinfoPage(Condition.getPage(query), baseDeptinfo);
        return R.data(pages);
    }

    /**
     * 新增 部门结构_yb_ba_dept
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入baseDeptinfo")
    public R save(@Valid @RequestBody BaseDeptinfo baseDeptinfo) {
        if(baseDeptinfo.getPId().equals("")){
            baseDeptinfo.setPId(0);
        }
        return R.status(baseDeptinfoService.save(baseDeptinfo));
    }

    /**
     * 修改 部门结构_yb_ba_dept
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入baseDeptinfo")
    public R update(@Valid @RequestBody BaseDeptinfo baseDeptinfo) {
        return R.status(baseDeptinfoService.updateById(baseDeptinfo));
    }

    /**
     * 新增或修改 部门结构_yb_ba_dept
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseDeptinfo")
    public R submit(@Valid @RequestBody BaseDeptinfo baseDeptinfo) {
        return R.status(baseDeptinfoService.saveOrUpdate(baseDeptinfo));
    }


    /**
     * 删除 部门结构_yb_ba_dept
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(baseDeptinfoService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 获取 部门结构_yb_ba_dept
     */
    @PostMapping("/getBaseDeptinfo")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "删除", notes = "传入userId")
    public R getBaseDeptinfo(Integer userId){
        return R.data("null");
    }

    /**
     * 获取 所有生产部门的名称
     */
    @GetMapping("/getProduceName")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "获取所有生产部门的名称")
    public List<BaseDeptinfo> getProduceName(){
        Map<String,Object> conditionMap = new HashMap<>();
        conditionMap.put("classify",2);
        List<BaseDeptinfo> list = baseDeptinfoService.getBaseMapper().selectByMap(conditionMap);
        return list;
    }

    /**
     * 获取 所有生产部门的名称
     */
    @GetMapping("/getDpNameByClassify")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "获取所有生产部门的名称")
    public List<DeptNameModel> getDpNameByClassify(){
        Map<String,Integer> conditionMap = new HashMap<>();
        conditionMap.put("classify",2);
        List<DeptNameModel> list = baseDeptinfoService.getPdNameByClassify(conditionMap.get("classify"));
        return list;
    }
    /**
     * 获取工序对应的生产部门的名称
     */
    @RequestMapping("/getDpNameByPrId")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "获取工序对应的生产部门的名称")
    public List<BaseDeptinfo> getDpNameByPrId(Integer prId){
        List<BaseDeptinfo> list = baseDeptinfoService.getDpNameByPrId(prId);
        return list;
    }


}
