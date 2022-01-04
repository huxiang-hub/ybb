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
import com.yb.base.entity.BaseClassinfo;
import com.yb.base.entity.BaseStaffclass;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseClassinfoService;
import com.yb.base.service.IBaseStaffclassService;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseClassinfoVO;
import com.yb.base.vo.BaseStaffclassVO;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.base.wrapper.BaseClassinfoWrapper;
import com.yb.base.wrapper.BaseStaffclassWrapper;
import com.yb.base.wrapper.BaseStaffinfoWrapper;
import com.yb.system.dict.wrapper.DictWrapper;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.vo.WorkbatchShiftsetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 班组信息_yb_base_classinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseclassinfo")
@Api(value = "班组信息_yb_base_classinfo", tags = "班组信息_yb_base_classinfo接口")
public class BaseClassinfoController extends BladeController {

    private IBaseClassinfoService baseClassinfoService;
    private IBaseStaffinfoService  baseStaffinfoService;
    private IBaseStaffclassService baseStaffclassService;
    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入sf_id")
    public R<BaseClassinfoVO> detail(Integer id) {
        BaseClassinfo detail = baseClassinfoService.getById(id);
        BaseClassinfoVO baseClassinfoVO = BaseClassinfoWrapper.build().entityVO(detail);
//        获取对应班组下的组员ids
        baseClassinfoVO.setIds(baseStaffinfoService.getIdsBybcId(baseClassinfoVO.getId()));
//        查询负责人
        Map<String,Object> map = new HashMap<>();
        map.put("bc_id",baseClassinfoVO.getId());
        map.put("jobs",2);
        List<BaseStaffinfo> baseStaffinfoList = baseStaffinfoService.listByMap(map);
        if (!Func.isEmpty(baseStaffinfoList)){
//            暂设置一个组长
            baseClassinfoVO.setJobsId(baseStaffinfoList.get(0).getId());
        }
        return R.data(baseClassinfoVO);
    }

    /**
     * 分页 班组信息_yb_base_classinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseClassinfo")
    public R<IPage<BaseClassinfoVO>> list(BaseClassinfo baseClassinfo, Query query) {
        IPage<BaseClassinfo> pages = baseClassinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(baseClassinfo));
        return R.data(BaseClassinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 班组信息_yb_base_classinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入baseClassinfo")
    public R<IPage<BaseClassinfoVO>> page(BaseClassinfoVO baseClassinfoVO, Query query) {
        IPage<BaseClassinfoVO> pages = baseClassinfoService.selectBaseClassinfoPage(Condition.getPage(query), baseClassinfoVO);
//        获取对应的组员
        if(!Func.isEmpty(pages.getRecords())){
            pages.getRecords().stream().forEach(baseClassinfoVO1 -> baseClassinfoVO1.setNames(baseStaffinfoService.getNamesBybcId(baseClassinfoVO1.getId())));
        }
        return R.data(pages);
    }

    /**
     * 新增修改 班组信息_yb_base_classinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @Transactional
    @ApiOperation(value = "新增修改", notes = "baseClassinfoVo")
    public R save(@Valid @RequestBody BaseClassinfoVO baseClassinfoVo) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("bc_id",baseClassinfoVo.getId());
            if (baseClassinfoVo.getId() != null){
//                存在時修改了部所属部门
//                查询是否修改了所属部门
                BaseClassinfo classinfo = baseClassinfoService.getById(baseClassinfoVo.getId());
                if (classinfo.getDpId() != baseClassinfoVo.getDpId()){
//                    查询是否有人员属于这个班组，有则不可改变
                    if (!Func.isEmpty(baseStaffinfoService.listByMap(map))){
                        return  R.fail("有人员正在使用此班组，无法修改所属部门");
                    }
                }
            }
//            查询之前的所有班组人员，置空
            baseStaffinfoService.updateStaffInfoBcIdBybcId(Func.toIntList(baseClassinfoVo.getId()+""), null);
            baseClassinfoService.saveOrUpdate(baseClassinfoVo);
//            将最新的人员ids部门id修改
            if (baseClassinfoVo.getIds() != null && baseClassinfoVo.getIds() != ""){
//                修改所有员工的班组
                baseStaffinfoService.updateStaffInfoBcIdById(Func.toIntList(baseClassinfoVo.getIds()), baseClassinfoVo.getId());
            }
            if (baseClassinfoVo.getJobsId() != null){
//                设置了负责人
                BaseStaffinfo baseStaffinfo = baseStaffinfoService.getById(baseClassinfoVo.getJobsId());
                baseStaffinfo.setJobs(2);//设置为班长（组长，负责人）
                baseStaffinfoService.saveOrUpdate(baseStaffinfo);
            }
            return R.status(true);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.status(false);
        }
    }

    /**
     * 修改 班组信息_yb_base_classinfo
     */
    @PostMapping("/updateClassinfo")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入baseClassinfo")
    public R updateClassinfo(@Valid @RequestBody BaseClassinfoVO baseClassinfoVO) {
        return R.status(baseClassinfoService.updateById(baseClassinfoVO));
    }

    /**
     * 新增或修改 班组信息_yb_base_classinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseClassinfo")
    @Transactional
    public R submit(@Valid @RequestBody BaseClassinfoVO baseClassinfoVO) {
        return R.status(baseClassinfoService.saveOrUpdate(baseClassinfoVO));
    }


    /**
     * 删除 班组信息_yb_base_classinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @Transactional
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
       try{
//        将对应删除的班组，使用人员的班组全部置空
           baseStaffinfoService.updateStaffInfoBcIdBybcId(Func.toIntList(ids), null);
//           将临时调班表中的该bcid的全部关闭
            baseStaffclassService.updateIsUsedBybcIds(Func.toIntList(ids));
//            删除对应班组
           baseClassinfoService.removeByIds(Func.toIntList(ids));
           return R.status(true);
       }catch (Exception e){
           e.printStackTrace();
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return R.fail("删除失败");
       }
    }
    /**
     * 获取班组集合 班组信息_yb_base_classinfo
     */
    @PostMapping("/lists")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "传入dpId")
    public R<List<BaseClassinfo>> lists(@Valid @RequestBody WorkbatchShiftsetVO workbatchShiftset){
        List<BaseClassinfo> list = new ArrayList<>();
        if (workbatchShiftset.getDbId() == null){
            Map<String,Object> map = new HashMap<>();
            map.put("is_used",1);
            list = baseClassinfoService.list();
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("dp_id",workbatchShiftset.getDbId());
            map.put("is_used",1);
            list = baseClassinfoService.listByMap(map);
        }
        return R.data(list);
    }
    /**
     * 获取班次下的班组集合 班组信息_yb_base_classinfo
     */
    @PostMapping("/listsBywsId")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "传入wsId")
    public R<List<BaseClassinfo>> listsBywsId(@ApiParam(value = "班次Id", required = true) @RequestParam Integer wsId){
        Map<String,Object> map = new HashMap<>();
        map.put("ws_id",wsId);
        return R.data(baseClassinfoService.listByMap(map));
    }

    /**
     * 将班组移除班次
     * @param Id
     * @return
     */
    @PostMapping("/setNullById")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "传入Id")
    public R setNullById(@ApiParam(value = "主键", required = true) @RequestParam Integer Id){
        return R.status(baseClassinfoService.setWsIdIsNull(Id));
    }
    /**
     * 查詢班組详情及班组下所有人员，包括班组临时调出与调来
     */
    @GetMapping("/detailAndStaff")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入sf_id")
    public R<BaseClassinfoVO> detailAndStaff(@ApiParam(value = "班组Id", required = true) @RequestParam Integer id) {
//        查询班组信息
        BaseClassinfo detail = baseClassinfoService.getById(id);
//        查询属于本班组的人员
        List<BaseStaffinfoVO> list = baseStaffinfoService.getAllByBcId(id);
        if (!Func.isEmpty(list)){
//            班组人员不为空时，查询人员是否有临时调出
            for (BaseStaffinfoVO baseStaffinfoVO : list){
                BaseStaffclassVO baseStaffclassVO =  baseStaffclassService.getGoOutUser(baseStaffinfoVO.getUserId());
                if (!Func.isEmpty(baseStaffclassVO)){
                    baseStaffinfoVO.setNewClass(baseStaffclassVO.getNewClass());
                    baseStaffinfoVO.setStartDate(baseStaffclassVO.getStartDate());
                    baseStaffinfoVO.setEndDate(baseStaffclassVO.getEndDate());
                }
            }
        }
//        查询调来的人员信息
        List<BaseStaffinfoVO> listIn = baseStaffinfoService.getInUserByBcId(id);
        if (!Func.isEmpty(listIn)){
            if (!Func.isEmpty(list)){
                list.addAll(listIn);
            }else {
                list = listIn;
            }
        }
        BaseClassinfoVO baseClassinfoVO = BaseClassinfoWrapper.build().entityVO(detail);
        baseClassinfoVO.setBaseStaffinfoVOList(list);
        return R.data(baseClassinfoVO);
    }
    /**
     * 分页 班组信息_yb_base_classinfo
     */
    @GetMapping("/getAllLits")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseClassinfo")
    public R<List<BaseClassinfoVO>> getAllLits() {
        return R.data(baseClassinfoService.getAllLits());
    }
    /**
     * 临时调用 班组信息_yb_base_classinfo
     */
    @PostMapping("/submitExchange")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseClassinfo")
    @Transactional
    public R submitExchange(@Valid @RequestBody BaseClassinfoVO baseClassinfoVO) {
        try {
//            将所有userId对应的员工临时调班关闭
            baseStaffclassService.updateIsUsedByIds(Func.toIntList(baseClassinfoVO.getIds()));
            for (Integer id : Func.toIntList(baseClassinfoVO.getIds())){
        //            查詢原部门id
                BaseStaffinfoVO baseStaffinfoVO = baseStaffinfoService.getBaseStaffinfoByUsId(id);
                if(!Func.isEmpty(baseStaffinfoVO) && baseStaffinfoVO.getBcId() == baseClassinfoVO.getNewBcId()){
        //                改回原班组，不增加临时调班表
                }else{
                    BaseStaffclass baseStaffclass = new BaseStaffclass();
                    baseStaffclass.setUsId(id);
                    baseStaffclass.setBcId(baseClassinfoVO.getNewBcId());
                    baseStaffclass.setIsUsed(1);
                    baseStaffclass.setStartDate(baseClassinfoVO.getStartDate());
                    baseStaffclass.setEndDate(baseClassinfoVO.getEndDate());
                    baseStaffclass.setCreateAt(new Date());
                    baseStaffclassService.save(baseStaffclass);
                }
            }
            return R.status(true);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.status(false);
        }
    }
}
