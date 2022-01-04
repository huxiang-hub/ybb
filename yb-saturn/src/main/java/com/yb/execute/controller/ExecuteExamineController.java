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
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.base.entity.BasePicture;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.request.ExecuteBrieferinfoRequest;
import com.yb.execute.request.ExecuteExamineRequest;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteExamineService;
import com.yb.execute.vo.ExamineParamVO;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecuteExamineVO;
import com.yb.execute.vo.TakeStockVO;
import com.yb.system.user.entity.SaUser;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 上报审核表_yb_execute_examine 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/executeexamine")
@Api(value = "修改确认表_yb_execute_examine", tags = "修改确认表_yb_execute_examine接口")
public class ExecuteExamineController extends BladeController {
    @Autowired
    private IExecuteExamineService executeExamineService;
    @Autowired
    private IExecuteBrieferService executeBrieferService;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;
    @Autowired
    private IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;


    /**
     * 分页 上报审核表_yb_execute_examine
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询上报列表(上报审核界面接口)", notes = "传入executeExamine")
    public R<IPage<ExecuteExamineVO>> list(Query query, ExamineParamVO examineParamVO) {
        IPage<ExecuteExamineVO> pages = executeExamineService.pageFindList(Condition.getPage(query), examineParamVO);
        return R.data(pages);
    }

    /**
     * 分页 上报查询
     */
    @PostMapping("/Rlist")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询上报列表(上报审核界面接口)", notes = "传入executeExamine")
    public R<IPage<ExecuteExamineVO>> Rlist(@RequestBody ExecuteBrieferinfoRequest reqeust) {
        IPage<ExecuteExamineVO> pages = executeExamineService.pageQueryList(Condition.getPage(reqeust), reqeust);
        return R.data(pages);
    }

    /**
     * 自定义分页 上报审核表_yb_execute_examine
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeExamine")
    public R<IPage<ExecuteExamineVO>> page(ExecuteExamineVO executeExamine, Query query) {
        IPage<ExecuteExamineVO> pages = executeExamineService.selectExecuteExaminePage(Condition.getPage(query), executeExamine);

        return R.data(pages);
    }

    /**
     * 新增 上报审核表_yb_execute_examine
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "executeExamine")
    public R save(@Valid @RequestBody ExecuteExamine executeExamine) {
        return R.status(executeExamineService.save(executeExamine));
    }

    /**
     * 修改 生产执行上报信息_yb_execute_briefer
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "executeExamineVO")
    public R update(@Valid @RequestBody ExecuteExamineVO executeExamineVO) {
        /*查询上报表信息*/
        ExecuteBriefer executeBriefer = executeBrieferService.getById(executeExamineVO.getBfId());
        if (!executeBriefer.getWasteNum().equals(executeExamineVO.getWasteNum())) {
            executeBriefer.setAlterWasteNum(executeBriefer.getWasteNum());
            executeBriefer.setWasteNum(executeExamineVO.getWasteNum());
        }
        if (!executeBriefer.getCountNum().equals(executeExamineVO.getCountNum())) {
            executeBriefer.setAlterCountNum(executeBriefer.getCountNum());
            executeBriefer.setCountNum(executeExamineVO.getCountNum());
        }
        if (!executeBriefer.getProductNum().equals(executeExamineVO.getProductNum())) {
            executeBriefer.setAlterProductNum(executeBriefer.getProductNum());
            executeBriefer.setProductNum(executeExamineVO.getProductNum());
        }
        executeExamineVO.setExStatus(1);
        executeBrieferService.updateById(executeBriefer);
        return R.status(executeExamineService.updateById(executeExamineVO));
    }

    /**
     * 新增或修改 上报审核表_yb_execute_examine
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @ApiOperation(value = "新增或修改", notes = "executeExamine")
    public R submit(@Valid @RequestBody ExecuteExamineVO executeExamineVO) {
        /*查询上报表信息，历史数据*/
        ExecuteBriefer oldbrfer = executeBrieferService.getById(executeExamineVO.getBfId());
        //创建对应的更新数据对象内容
        ExecuteBriefer executeBriefer = new ExecuteBriefer();
        executeBriefer.setId(executeExamineVO.getBfId());
        executeBriefer.setProductNum(executeExamineVO.getProductNum());
        executeBriefer.setCountNum(executeExamineVO.getCountNum());
        executeBriefer.setWasteNum(executeExamineVO.getWasteNum());
        executeBrieferService.updateCheck(executeBriefer);//更新状态数据


        //执行审核数据的记录信息  本地代码不能获取用户登录信息，是不是需要关联登录的nacos问题
        SaUser loguser = SaSecureUtil.getUser();
        String databefore = "|" + oldbrfer.getProductNum() + "|" + oldbrfer.getCountNum() + "|" + oldbrfer.getWasteNum() + "|";
        String dataafter = "|" + executeExamineVO.getProductNum() + "|" + executeExamineVO.getCountNum() + "|" + executeExamineVO.getWasteNum() + "|";
        ExecuteExamine examine = new ExecuteExamine();
        examine.setBfId(executeExamineVO.getBfId());
        examine.setExUserid((loguser != null) ? loguser.getUserId() : null);
        examine.setRptUserid(oldbrfer.getHandleUsid());//上报人
        examine.setRptTime(oldbrfer.getHandleTime());
        examine.setDataBefore(databefore);
        examine.setDataAfter(dataafter);
        examine.setExStatus(1);//默认审核通过
        examine.setExWay(1);//默认1、系统修改流程；2、走正式审核流程手机审核流程；
        examine.setCreateAt(new Date());
        boolean success = executeExamineService.save(examine);


        //提前把finishnum、complatenum数据进行更新修改，然后再进行状态更新
        workbatchOrdlinkNewService.setOrdShift(executeExamineVO.getWfId());
        log.info("上报审核进行待排数量计算，执行修改数据操作；---------------------sdId：【" + executeExamineVO.getSdId() + "】");
        workbatchOrdlinkNewService.setWorksNum(executeExamineVO.getSdId(), executeExamineVO.getMaId(), "7");
        return R.status(success);
    }

    /**
     * 新增或修改 上报审核表_yb_execute_examine
     */
//    @PostMapping("/submit")
//    @ApiOperationSupport(order = 6)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @ApiOperation(value = "新增或修改", notes = "executeExamine")
//    public R submit(@Valid @RequestBody ExecuteExamineVO executeExamineVO) {
//        /*查询上报表信息*/
//        ExecuteBriefer executeBriefer = executeBrieferService.getById(executeExamineVO.getBfId());
//        if (!executeBriefer.getProductNum().equals()
//                || !executeBriefer.getCountNum().equals(executeExamineVO.getCountNum())
//                || !executeBriefer.getWasteNum().equals(executeExamineVO.getWasteNum())) {
//            executeBriefer.setWasteNum(executeExamineVO.getWasteNum());
//            executeBriefer.setCountNum(executeExamineVO.getCountNum());
//            executeBriefer.setProductNum(executeExamineVO.getProductNum());
//            executeBriefer.setAlterCountNum(executeBriefer.getCountNum());
//            executeBriefer.setAlterWasteNum(executeBriefer.getWasteNum());
//            executeBriefer.setAlterProductNum(executeBriefer.getProductNum());
//        }
//        executeExamineVO.setExStatus(1);
//        /*WorkbatchOrdlinkShiftVO workbatchOrdlinkShiftVO = workbatchOrdlinkService.getById(executeExamineVO.getSdId());
//        int completeNum = workbatchOrdlinkShiftVO.getCompleteNum() + executeExamineVO.getCountNum();//完成数
//        workbatchOrdlinkShiftVO.setId(executeExamineVO.getSdId());
//        workbatchOrdlinkShiftVO.setCompleteNum(completeNum);//完成数
//        Integer waste = executeExamineVO.getWasteNum();//废品数
//        if( workbatchOrdlinkShiftVO.getWaste() != null){
//            waste += workbatchOrdlinkShiftVO.getWaste();
//        }
//        workbatchOrdlinkShiftVO.setWaste(waste);//废品数
//        workbatchOrdlinkShiftVO.setIncompleteNum(workbatchOrdlinkShiftVO.getPlanNum() - completeNum);//未完成数
//        if(workbatchOrdlinkShiftVO.getRunStatus().equals(2)){
//            workbatchOrdlinkShiftVO.setEndTime(new Date());
//            workbatchOrdlinkShiftVO.setStatus("3");
//        }else {
//            workbatchOrdlinkShiftVO.setUpdateAt(new Date());
//        }*/
//        Integer esId = executeExamineVO.getEsId();
//        executeExamineVO.setId(esId);
//        executeBrieferService.updateById(executeBriefer);
//        return R.status(executeExamineService.saveOrUpdate(executeExamineVO));
//    }


    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeExamineService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 删除 上报审核表_yb_execute_examine
     */
    @RequestMapping("/detail")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "详情", notes = "传入id")
    public R<ExecuteExamineVO> detail(Integer id) {
        return R.data(executeExamineService.selectExecuteExamine(id));
    }


    @GetMapping("getPhoneDetail")
    @ApiOperation(value = "手机盘点-获取手机端上报审核记录")
    public R<IPage<TakeStockVO>> getPhoneDetail(
            @ApiParam(value = "工单编号") @RequestParam String odNo,
            @ApiParam(value = "设备id") @RequestParam Integer maId,
            @ApiParam(value = "工序id") @RequestParam Integer prId,
            @ApiParam(value = "审核状态:0已审核，1未审核，2无需审核") @RequestParam Integer exStatus,
            @ApiParam(value = "仓库(库区)id") @RequestParam Integer storeAreaId,
            Query query) {
        IPage<TakeStockVO> phoneDetail = executeExamineService.getPhoneDetail(Condition.getPage(query), odNo, maId, prId, exStatus, storeAreaId);
        return R.data(phoneDetail);
    }

    @GetMapping("getPhoneDetail/nocheck")
    @ApiOperation(value = "手机盘点-获取手机端无需审核上报审核记录")
    public R<IPage<TakeStockVO>> getPhoneDetailNocheck(
            @ApiParam(value = "工单编号") @RequestParam String odNo,
            @ApiParam(value = "设备id") @RequestParam Integer maId,
            @ApiParam(value = "工序id") @RequestParam Integer prId,
            @ApiParam(value = "仓库(库区)id") @RequestParam Integer storeAreaId,
            Query query) {
        IPage<TakeStockVO> phoneDetail = executeExamineService.getPhoneDetailNocheck(Condition.getPage(query), odNo, maId, prId, null, storeAreaId);
        return R.data(phoneDetail);
    }

    @GetMapping("phoneUpdateEtIdTdNo")
    @ApiOperation(value = "手机盘点-确定修改库位或数量")
    public R phoneUpdateEtIdTdNo(
            @ApiParam(value = "上报id") @RequestParam Integer bfId,
            @ApiParam(value = "审核人id") @RequestParam(required = false) Integer exUserid,
            @ApiParam(value = "托盘id") @RequestParam(required = false) Integer tyId,
            @ApiParam(value = "图片urls,多个用'｜'隔开") @RequestParam(required = false) String exPics,
            @ApiParam(value = "原始库位") @RequestParam(required = false) String originalStNo,
            @ApiParam(value = "修改后库位") @RequestParam(required = false) String modifyStNo,
            @ApiParam(value = "原始数量") @RequestParam(required = false) Integer originalNumber,
            @ApiParam(value = "修改后数量") @RequestParam(required = false) Integer modifyNumber,
            @ApiParam(value = "上报人id") @RequestParam(required = false) Integer rptUserid,
            @ApiParam(value = "修改类型1、盘点2、报废、3机长") @RequestParam String exMold) {
        executeExamineService.phoneUpdateEtIdTdNo(bfId, tyId, exPics, originalStNo, modifyStNo, originalNumber, modifyNumber, rptUserid, exMold, exUserid);
        return R.success("修改成功");
    }

    @GetMapping("phoneUpdateEtIdTdNoNew")
    @ApiOperation(value = "手机盘点审核托盘数量或者上报表数量信息")
    public R phoneUpdateEtIdTdNoNew(@ApiParam(value="审核传入参数") ExecuteExamineRequest request) {

        executeExamineService.phoneUpdateEtIdTdNo(request);
        return R.success("修改成功");
    }

    @GetMapping("getSumByBfId")
    @ApiOperation(value = "手机盘点-根据上报id和类型获取修改前后数据")
    public R<Map<String, Integer>> getSumByBfId(
            @ApiParam(value = "上报id") @RequestParam Integer bfId,
            @ApiParam(value = "修改类型1、盘点2、报废、3机长") @RequestParam String exMold) {
        Map<String, Integer> map = executeExamineService.getSumByBfId(bfId, exMold);
        return R.data(map);
    }

    @GetMapping("getAllSumByBfId")
    @ApiOperation(value = "手机盘点-根据上报id获取【所有】修改前后数据")
    public R<Map<String, Map<String, Integer>>> getAllSumByBfId(
            @ApiParam(value = "上报id") @RequestParam Integer bfId) {
        Map<String, Map<String, Integer>> allSumByBfId = executeExamineService.getAllSumByBfId(bfId);
        return R.data(allSumByBfId);
    }

    @GetMapping("getDetailByBfId")
    @ApiOperation(value = "手机盘点-根据上报id和类型获修改的托盘数据")
    public R<List<ExecuteExamineVO>> getDetailByBfId(
            @ApiParam(value = "上报id") @RequestParam Integer bfId,
            @ApiParam(value = "修改类型1、盘点2、报废、3机长") @RequestParam String exMold) {
        List<ExecuteExamineVO> list = executeExamineService.getModifyTrayByBfId(bfId, exMold);
        return R.data(list);
    }

    @GetMapping("getImageByBfId")
    @ApiOperation(value = "手机盘点-根据上报id和类型获修改的托盘图片")
    public R<List<BasePicture>> getImageByBfId(
            @ApiParam(value = "上报id") @RequestParam Integer bfId,
            @ApiParam(value = "修改类型1、盘点2、报废、3机长") @RequestParam String exMold) {
        List<BasePicture> list = executeExamineService.getImageByBfId(bfId, exMold);
        return R.data(list);
    }

}
