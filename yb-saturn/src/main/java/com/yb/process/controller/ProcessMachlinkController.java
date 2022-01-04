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
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.entity.ProcessClasslink;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessClassifyService;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.process.vo.ProModelVO;
import com.yb.process.vo.ProcessMachlinkVO;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.process.vo.PyModelVO;
import com.yb.process.wrapper.ProcessMachlinkWrapper;
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
import java.util.*;

/**
 * 设备工序关联表yb_process_machlink 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/process")
@Api(value = "设备工序关联表yb_process_machlink", tags = "设备工序关联表yb_process_machlink接口")
public class ProcessMachlinkController extends BladeController {

    private IProcessMachlinkService processMachlinkService;
    private IBaseStaffinfoService staffinfoService;
    private IProcessWorkinfoService workinfoService;
    private IProcessClassifyService classifyService;
    private IProcessClasslinkService classlinkService;
    private IMachineMainfoService machineMainfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processMachlink")
    public R<ProcessMachlinkVO> detail(ProcessMachlink processMachlink) {
        ProcessMachlink detail = processMachlinkService.getOne(Condition.getQueryWrapper(processMachlink));
        return R.data(ProcessMachlinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备工序关联表yb_process_machlink
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processMachlink")
    public R<IPage<ProcessMachlinkVO>> list(ProcessMachlink processMachlink, Query query) {
        IPage<ProcessMachlink> pages = processMachlinkService.page(Condition.getPage(query), Condition.getQueryWrapper(processMachlink));
        return R.data(ProcessMachlinkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备工序关联表yb_process_machlink
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processMachlink")
    public R<IPage<ProcessMachlinkVO>> page(ProcessMachlinkVO processMachlink, Query query) {
        IPage<ProcessMachlinkVO> pages = processMachlinkService.selectProcessMachlinkPage(Condition.getPage(query), processMachlink);
        return R.data(pages);
    }

    /**
     * 新增 设备工序关联表yb_process_machlink
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processMachlink")
    public R save(@Valid @RequestBody ProcessMachlink processMachlink) {
        return R.status(processMachlinkService.save(processMachlink));
    }

    /**
     * 修改 设备工序关联表yb_process_machlink
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processMachlink")
    public R update(@Valid @RequestBody ProcessMachlink processMachlink) {
        return R.status(processMachlinkService.updateById(processMachlink));
    }

    /**
     * 新增或修改 设备工序关联表yb_process_machlink
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processMachlink")
    public R submit(@Valid @RequestBody ProcessMachlink processMachlink) {
        return R.status(processMachlinkService.saveOrUpdate(processMachlink));
    }


    /**
     * 删除 设备工序关联表yb_process_machlink
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(processMachlinkService.removeByIds(Func.toIntList(ids)));
    }

    /**
     *工序管理/查询  工序分类，工序编号 ，工序对用的设备 ，该工序的工人总数
     */
    @RequestMapping("/getProInfo")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R getProInfoPage(Query query,ProModelVO proModelVO) {
        Map<String,Object> result = new HashMap<>();
        IPage<ProModelVO> proList  =
                processMachlinkService.getAllPyName(Condition.getPage(query),proModelVO);
        List<ProModelVO>  proModelVOList = new ArrayList<>();
        if (!proList.getRecords().isEmpty()) {
            for (ProModelVO proItem :proList.getRecords()){
                proItem.setMaName(
                    processMachlinkService.getMaNamesByPyId(proItem.getId())
                );

                String prId = "|"+proItem.getId()+"|";
                proItem.setStaffSum(staffinfoService.getProcessesSum(prId));
                if(!proItem.getMaName().isEmpty()){
                    proModelVOList.add(proItem);
                }
                proList.setTotal(proModelVOList.size());
                proList.setRecords(proModelVOList);
            }

        }
        List<PyModelVO> pyModelVOS = processMachlinkService.getPyModels();

        result.put("proList",proList);
       // result.put("pyModelVOS",pyModelVOS);
        return R.data(result);
    }
    /**
     *查询工序关联设备
     */
    @RequestMapping("/processMachlinkList")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R processMachlinkList(Query query,ProcessMachlinkVO processMachlinkVO) {
        IPage<ProcessMachlink> processMachlinkList = processMachlinkService.processMachlinkList(Condition.getPage(query), processMachlinkVO);
        return R.data(processMachlinkList);
    }


    /**
     *工序管理/查询  获取工序名字的数据字典，只查询出为启用的主工序名字
     */
    @RequestMapping("/getPrName")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R getPrName() {
        List<ProcessWorkinfo> prNames = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("status",1); //查询状态为1的 也就是启用的
        List<ProcessClassify> classifies =
                classifyService.getBaseMapper().selectByMap(map);
        if (!classifies.isEmpty()) {
            for(ProcessClassify  classify : classifies){
                map.clear();
                map.put("py_id",classify.getId());
                List<ProcessClasslink> classlinks =
                        classlinkService.getBaseMapper().selectByMap(map);
                for (ProcessClasslink classlink : classlinks){
                    map.clear();
                    map.put("id",classlink.getPrId());
                    map.put("status",1);//状态为启用的
                    List<ProcessWorkinfo> prName =
                            workinfoService.getBaseMapper().selectByMap(map);
                    prNames.addAll(prName); //合并集合
                }

            }
        }
        return R.data(prNames);
    }

    /**
     *工序管理/查询    获取工序类型名字的数据字典
     */
    @RequestMapping("/getPyName")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R getPyName() {
        List<PyModelVO> pyName =
                processMachlinkService.getPyModels();
        return R.data(pyName);
    }

    /**
     *工序管理/查询    获取工序类型名字的数据字典
     */
    @RequestMapping("/getPyNameById")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R getPyNameById(Integer id) {
        String pyName =
                processMachlinkService.getPyNameById(id);
        return R.data(pyName);
    }
    @RequestMapping("/getMaName")
    @ApiOperationSupport(order = 7)
    public R getMaName() {
        return R.data(processMachlinkService.getMaNameList());
    }

    @RequestMapping("/getMaName/{prId}")
    @ApiOperationSupport(order = 7)
    public R getPrMaName(@PathVariable Integer prId) {
        Map map= new HashMap();
        map.put("pr_id", prId);
        List<MachineMainfo> maNameList = processMachlinkService.getMaNameList();
        List<ProcessMachlink> processMachlinkList = processMachlinkService.getBaseMapper().selectByMap(map);
        if(!processMachlinkList.isEmpty()){
            Iterator<MachineMainfo> it = maNameList.iterator();
            while (it.hasNext()) {
                MachineMainfo machineMainfo = it.next();
                for(ProcessMachlink processMachlink : processMachlinkList){
                    if(machineMainfo.getId().equals(processMachlink.getMaId())){
                        it.remove();
                        continue;
                    }
                }
            }
        }

        return R.data(maNameList);
    }



    @RequestMapping("/addWorkinfo")
    @ApiOperationSupport(order = 7)
    public R addWorkinfo(@RequestBody ProcessMachlink processMachlink) {
        //判定是否已经存在绑定
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",processMachlink.getMaId());
        map.put("pr_id",processMachlink.getPrId());
        List<ProcessMachlink> machlinks =
                processMachlinkService.getBaseMapper().selectByMap(map);
        if (!machlinks.isEmpty()) {
            return success("该工序设备已被绑定!");
        }
        return R.status(processMachlinkService.save(processMachlink));
    }

    @RequestMapping("/delete")
    @ApiOperationSupport(order = 7)
    public R delete(@RequestParam Integer ids) {
        return R.status(processMachlinkService.delete(ids));
    }

    /**
     * 工序以及工序下的所有设备
     */
    @GetMapping("/getProcessMach")
    @ApiOperationSupport(order = 8)
    public R getProcessMach () {
//        查询所有工序，查询工序下的所有设备
        List<ProcessWorkinfo> workinfoList = workinfoService.listByMachine();
        List<ProcessWorkinfoVO> list = new ArrayList<>();
        if (!Func.isEmpty(workinfoList)) {
            workinfoList.forEach(workinfo -> {
                ProcessWorkinfoVO processWorkinfoVO = ProcessWorkinfoWrapper.build().entityVO(workinfo);
//                查询工序下的设备
                Map<String,Object> maMap = new HashMap<>();
                maMap.put("pro_id", processWorkinfoVO.getId());
                List<MachineMainfo> machins = machineMainfoService.listByMap(maMap);
                processWorkinfoVO.setMainfos(machins);
                list.add(processWorkinfoVO);
            });
        }
        return R.data(list);
    }
}
