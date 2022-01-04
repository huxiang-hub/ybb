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
import com.yb.process.entity.ProcessClasslink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.process.wrapper.ProcessWorkinfoWrapper;
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
 * 工序表--租户的工序内容（可以依据行业模版同步） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/processworkinfo")
@Api(value = "工序表--租户的工序内容（可以依据行业模版同步）", tags = "工序表--租户的工序内容（可以依据行业模版同步）接口")
public class ProcessWorkinfoController extends BladeController {

    private IProcessWorkinfoService processWorkinfoService;
    private IProcessClasslinkService classlinkService;
    private IProcessMachlinkService machlinkService;
    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processWorkinfo")
    public R<ProcessWorkinfoVO> detail(ProcessWorkinfo processWorkinfo) {
        ProcessWorkinfoVO detail = processWorkinfoService.selectSortProcessWorkinfo(processWorkinfo.getId());
        return R.data(ProcessWorkinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processWorkinfo")
    public R<IPage<ProcessWorkinfoVO>> list(ProcessWorkinfo processWorkinfo, Query query) {
        IPage<ProcessWorkinfo> pages = processWorkinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(processWorkinfo));
        return R.data(ProcessWorkinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processWorkinfo")
    public R<IPage<ProcessWorkinfoVO>> page(ProcessWorkinfoVO processWorkinfo, Query query) {
        IPage<ProcessWorkinfoVO> pages = processWorkinfoService.selectProcessWorkinfoPage(Condition.getPage(query), processWorkinfo);
        return R.data(pages);
    }

    /**
     * 新增 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processWorkinfo")
    public R save(@Valid @RequestBody ProcessWorkinfo processWorkinfo) {
        return R.status(processWorkinfoService.save(processWorkinfo));
    }

    /**
     * 修改 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processWorkinfo")
    public R update(@Valid @RequestBody ProcessWorkinfoVO processWorkinfo) {
        /**
         * 先修改工序表的内容
         * 通过修改后的主键找到对应的类型主键
         * 在修改中间表的对应关系
         */
        Map<String,Object> map = new HashMap<>();
        //先修改工序表的内容
        processWorkinfoService.updateById(processWorkinfo);
        //通过修改后的主键找到对应的类型主键
        map.put("pr_id",processWorkinfo.getId());
         ProcessClasslink classlink =
                 classlinkService.getBaseMapper().selectByMap(map).get(0);
         //在修改中间表的对应关系
        classlink.setPyId(processWorkinfo.getPyId());
        classlinkService.updateById(classlink);
        map.clear();
        return R.success("修改成功！");
    }

    /**
     * 新增或修改 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processWorkinfo")
    public R submit(@Valid @RequestBody ProcessWorkinfoVO processWorkinfo) {
        //去重复
        Map<String,Object> map = new HashMap<>();
        map.put("pr_name",processWorkinfo.getPrName());
        if (!processWorkinfoService.getBaseMapper().selectByMap(map).isEmpty()) {
            return R.fail("此工序已经存在！");
        }
        /**
         * 先增加工序
         */
        processWorkinfoService.saveOrUpdate(processWorkinfo);
        /**
         * 再增加工序入中间表
         */
        if (processWorkinfo.getId()==null||processWorkinfo.getPyId()==null) {
            return R.fail("新增失败");
        }else{
            ProcessClasslink classlink = new ProcessClasslink();
            classlink.setPrId(processWorkinfo.getId());//设置工序id
            classlink.setPyId(processWorkinfo.getPyId());//设置工序类型id
            classlinkService.save(classlink);//保存

        }
        return R.data(processWorkinfo);
    }


    /**
     * 删除 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(processWorkinfoService.removeByIds(Func.toIntList(ids)));
    }
    @RequestMapping("/unfinishNum")
    @ApiOperation(value = "查询未排产工序数量", notes = "prId, ptId, wbId")
    public Integer unfinishNum(Integer prId, Integer ptId, Integer wbId)  {
        return processWorkinfoService.unfinishNum(prId, ptId, wbId);
    }

    /**
     * 新增或修改 工序表--租户的工序内容（可以依据行业模版同步）
     */
    @PostMapping("/addWorkinfo")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processWorkinfo")
    public R addWorkinfo(@Valid @RequestBody ProcessWorkinfoVO processWorkinfo) {
        /**
         * 新增工序 要在
         *
         */
        return R.status(processWorkinfoService.saveOrUpdate(processWorkinfo));
    }

    /********************************QINBO*********************************************************/
    /**
     * 自定义分页
     */
    @GetMapping("/getPage")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processWorkinfo")
    public R<IPage<ProcessWorkinfoVO>> getPage(ProcessWorkinfoVO processWorkinfo, Query query) {
        IPage<ProcessWorkinfoVO> pages =
                processWorkinfoService.selectSortProcessWorkinfoPage(Condition.getPage(query), processWorkinfo);
        return R.data(pages);
    }

    /**
     * 工序分类id获取对应下的工序
     */
    @GetMapping("/workInfoBy")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processWorkinfo")
    public R<List<ProcessWorkinfoVO>> workInfoBy(Integer pyId) {
        return R.data(processWorkinfoService.workInfoBy(pyId));
    }

    @GetMapping("/processList")
    @ApiOperation(value = "所有的工序的名称" )
    public List<ProcessWorkinfo> getrocessList() {
        return processWorkinfoService.list();
    }
}
