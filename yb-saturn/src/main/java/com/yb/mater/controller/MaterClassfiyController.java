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
package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.customer.entity.CrmCustomer;
import com.yb.mater.entity.MaterClassfiy;
import com.yb.mater.mapper.MaterClassfiyMapper;
import com.yb.mater.service.IMaterClassfiyService;
import com.yb.mater.service.IMaterMtinfoService;
import com.yb.mater.vo.MaterClassfiyVO;
import com.yb.mater.wrapper.MaterClassfiyWrapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料列表_yb_mater_classfiy 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/materclassfiy")
@Api(value = "物料分类表_yb_mater_classfiy", tags = "物料分类表_yb_mater_classfiy接口")
public class MaterClassfiyController extends BladeController {

    private IMaterClassfiyService materClassfiyService;
    private IMaterMtinfoService materMtinfoService;
    private MaterClassfiyMapper materClassfiyMapper;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入materClassfiy")
    public R<MaterClassfiyVO> detail(MaterClassfiy materClassfiy) {
        MaterClassfiy detail = materClassfiyService.getOne(Condition.getQueryWrapper(materClassfiy));
        return R.data(MaterClassfiyWrapper.build().entityVO(detail));
    }

    /**
     * 分页 物料列表_yb_mater_classfiy
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入materClassfiy")
    public R<IPage<MaterClassfiyVO>> list(MaterClassfiy materClassfiy, Query query) {
        IPage<MaterClassfiy> pages = materClassfiyService.page(Condition.getPage(query), Condition.getQueryWrapper(materClassfiy));
        return R.data(MaterClassfiyWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 物料列表_yb_mater_classfiy
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入materClassfiy")
    public R<IPage<MaterClassfiy>> page(MaterClassfiyVO materClassfiy, Query query) {
        QueryWrapper<MaterClassfiy> materClassfiyQueryWrapper = new QueryWrapper<>();
        if (StringUtil.isNoneBlank(materClassfiy.getMcName())) {
            materClassfiyQueryWrapper.like("mc_name", materClassfiy.getMcName());
        }
        if (StringUtil.isNoneBlank(materClassfiy.getMcNo())) {
            materClassfiyQueryWrapper.like("mc_no", materClassfiy.getMcNo());
        }
        Page<MaterClassfiy> objectPage = new Page<MaterClassfiy>();
        objectPage.setSize(query.getSize());
        objectPage.setCurrent(query.getCurrent());
        IPage<MaterClassfiy> page = materClassfiyMapper.selectPage(objectPage, materClassfiyQueryWrapper);
        return R.data(page);
    }

    /**
     * 新增 物料列表_yb_mater_classfiy
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入materClassfiy")
    public R save(@Valid @RequestBody MaterClassfiy materClassfiy) {
        return R.status(materClassfiyService.save(materClassfiy));
    }

    /**
     * 修改 物料列表_yb_mater_classfiy
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入materClassfiy")
    public R update(@Valid @RequestBody MaterClassfiy materClassfiy) {
        return R.status(materClassfiyService.updateById(materClassfiy));
    }

    /**
     * 新增或修改 物料列表_yb_mater_classfiy
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入materclassfiy")
    public R submit(@Valid @RequestBody MaterClassfiy materClassfiy) {
        return R.status(materClassfiyService.saveOrUpdate(materClassfiy));
    }


    /**
     * 删除 物料列表_yb_mater_classfiy
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//        此物料分类查询是否使用
        String error = "";
        for (Integer id : Func.toIntList(ids)){
            Map<String,Object> map = new HashMap<>();
            map.put("mc_id",id);
            if (!Func.isEmpty(materMtinfoService.listByMap(map))){
                MaterClassfiy materClassfiy = materClassfiyService.getById(id);
                error += materClassfiy.getMcName()+"正在使用中不可删除!";
            }
        }
        if (error != ""){
            return R.fail(error);
        }
        return R.status(materClassfiyService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 查询所有的物料类型
     */
    @GetMapping("/listType")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "所有物料类型")
    public List<MaterClassfiy> listType(){
        return materClassfiyService.list();
    }

}
