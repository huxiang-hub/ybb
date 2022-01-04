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
package com.yb.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.actset.common.StaticFinaly;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.service.IProcessClassifyService;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.vo.PyModelVO;
import com.yb.process.wrapper.ProcessClassifyWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工序分类表_yb_process_classify 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/processclassify")
@Api(value = "工序分类表_yb_process_classify", tags = "工序分类表_yb_process_classify接口")
public class ProcessClassifyController extends BladeController {

    private IProcessClassifyService processClassifyService;
    private IProcessClasslinkService processClasslinkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processClassify")
    public R<ProcessClassifyVO> detail(ProcessClassify processClassify) {
        ProcessClassify detail = processClassifyService.getOne(Condition.getQueryWrapper(processClassify));
        return R.data(ProcessClassifyWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序分类表_yb_process_classify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<IPage<ProcessClassifyVO>> list(ProcessClassify processClassify, Query query) {
        IPage<ProcessClassify> pages = processClassifyService.page(Condition.getPage(query), Condition.getQueryWrapper(processClassify));
        return R.data(ProcessClassifyWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 工序分类表_yb_process_classify
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<IPage<ProcessClassifyVO>> page(ProcessClassifyVO processClassify, Query query) {
        IPage<ProcessClassifyVO> pages = processClassifyService.getSortProClassifys(Condition.getPage(query), processClassify);
        return R.data(pages);
    }

    /**
     * 新增 工序分类表_yb_process_classify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processClassify")
    public R save(@Valid @RequestBody ProcessClassify processClassify) {
        return R.status(processClassifyService.save(processClassify));
    }

    /**
     * 修改 工序分类表_yb_process_classify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processClassify")
    public R update(@Valid @RequestBody ProcessClassify processClassify) {
        /**
         * 修改分类编号是 要修改中间表的对应关系
         */

        return R.status(processClassifyService.updateById(processClassify));
    }

    /**
     * 新增或修改 工序分类表_yb_process_classify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processClassify")
    public R submit(@Valid @RequestBody ProcessClassify processClassify) {
        Map<String,Object> map = new HashMap<>();
        map.put("py_name",processClassify.getPyName());
        if (!processClassifyService.getBaseMapper().selectByMap(map).isEmpty()) {
            return R.fail("已经有此类工序了！");
        }
        processClassify.setStatus(processClassify.getStatus());
        return R.status(processClassifyService.saveOrUpdate(processClassify));
    }

    /**物理删除（删除就不可找回）**/
    /**
     * 删除 工序分类表_yb_process_classify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        boolean result = processClassifyService.removeByIds(Func.toIntList(ids));
        if (result) {//工序分类删除成功后删除所有子工序的中间表数据
            Map<String,Object> map = new HashMap<>();
           for(Integer id : Func.toIntList(ids)){ //遍历出每一个工序分类ID
               map.put("pr_id",id);
               processClasslinkService.getBaseMapper().deleteByMap(map);
               map.clear();//清空map
           }
        }
        return R.status(result);
    }

    /**
     * 查询所有工序的分类对应工序id
     */
    @GetMapping("/listAll")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<List<ProcessClassifyVO>> listAll(ProcessClassify processClassify, Query query) {
        List<ProcessClassifyVO> list = processClassifyService.getProClassifys();
        return R.data(list);
    }
    /**
     * 查询所有工序的分类
     */
    @GetMapping("/listAllClassify")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<List<ProcessClassify>> listAllClassify() {
        List<ProcessClassify> list = processClassifyService.list();
        return R.data(list);
    }

    /**
     * 数字字典 查询所有分类
     */
    @GetMapping("/getPrModelVO")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R getPrModelVO() {
        List<PyModelVO> list = processClassifyService.getPrModelVO();
        return R.data(list);
    }
    /**
     * 数字字典 查询所有分类
     */
    @GetMapping("/getProcessClassifyByWfId")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据排产班次id获取工序分类", notes = "传入wfId")
    public R<ProcessClassify> getProcessClassifyByWfId(Integer wfId) {
        return R.data(processClassifyService.getProcessClassifyByWfId(wfId));
    }
}
