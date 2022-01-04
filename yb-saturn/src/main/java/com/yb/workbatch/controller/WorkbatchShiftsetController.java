package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.base.entity.BaseClassinfo;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.service.IBaseClassinfoService;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.system.user.entity.SaUser;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.request.WorkbatchShiftsetPageRequest;
import com.yb.workbatch.request.WorkbatchShiftsetSaveAndUpdateRequest;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import com.yb.workbatch.vo.WorkbatchShiftsetPageVO;
import com.yb.workbatch.vo.WorkbatchShiftsetVO;
import com.yb.workbatch.wrapper.WorkbatchShiftsetWrapper;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbatchshiftset")
@Api(value = "排产班次设定_yb_workbatch_shifts（班次）", tags = "排产班次设定_yb_workbatch_shifts（班次）接口")
public class WorkbatchShiftsetController extends BladeController {

    private IWorkbatchShiftsetService workbatchShiftsetService;
    private IBaseClassinfoService baseClassinfoService;
    private IBaseDeptinfoService baseDeptinfoService;
    private WorkbatchShiftMapper workbatchShiftMapper;
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入workbatchShiftset")
    public R<WorkbatchShiftsetVO> detail(WorkbatchShiftset workbatchShiftset) {
        WorkbatchShiftset detail = workbatchShiftsetService.getOne(Condition.getQueryWrapper(workbatchShiftset));
        WorkbatchShiftsetVO workbatchShiftsetVO = WorkbatchShiftsetWrapper.build().entityVO(detail);
        workbatchShiftsetVO.setBcIds(baseClassinfoService.getBcIdsBywsId(workbatchShiftsetVO.getId()));
        return R.data(workbatchShiftsetVO);
    }

//    /**
//     * 分页 排产班次设定_yb_workbatch_shifts（班次）
//     */
//    @GetMapping("/list")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "分页", notes = "传入workbatchShiftset")
//    public R<IPage<WorkbatchShiftsetVO>> list(WorkbatchShiftset workbatchShiftset, Query query) {
//        IPage<WorkbatchShiftset> pages = workbatchShiftsetService.page(Condition.getPage(query), Condition.getQueryWrapper(workbatchShiftset));
//        return R.data(WorkbatchShiftsetWrapper.build().pageVO(pages));
//    }

    /**
     * 自定义分页 排产班次设定_yb_workbatch_shifts（班次）
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页")
    public R<IPage<WorkbatchShiftsetPageVO>> page(@RequestBody WorkbatchShiftsetPageRequest request) {

        IPage<WorkbatchShiftsetPageVO> vos = workbatchShiftsetService.page(Condition.getPage(request), request);

        return R.data(vos);
    }

    /**
     * 新增 排产班次设定_yb_workbatch_shifts（班次）
     */
    @PostMapping("/saveAndUpdate")
    @ApiOperationSupport(order = 4)
    @Transactional
    @ApiOperation(value = "新增/修改")
    public R saveAndUpdate(@Validated @RequestBody WorkbatchShiftsetSaveAndUpdateRequest request) {
        SaUser user = SaSecureUtil.getUser();//获取登录着的信息
        workbatchShiftsetService.saveAndUpdate(request, user);
        return R.success("增加成功");
    }


    /**
     * 删除 排产班次设定_yb_workbatch_shifts（班次）
     */
    @PostMapping("/delete")
    @Transactional
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true)
                    @RequestBody List<Integer> ids) {

        workbatchShiftsetService.delete(ids);

        return R.data("删除成功");
    }

    @GetMapping("/shiftTypeList")
    @ApiOperation(value = "班次类型列表")
    public R shiftTypeList() {

        Map map = workbatchShiftsetService.shiftTypeList();

        return R.data(map);
    }

    /**
     * 查询 排产班次设定_yb_workbatch_shifts（班次）
     */
    @RequestMapping("/getWorkbatchShift")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "全部", notes = "传入ids")
    public R getWorkbatchShift() {
        return R.data(workbatchShiftsetService.getWorkbatchShift());
    }


    /**
     * 分页 排产班次设定_yb_workbatch_shifts（班次）
     */
    @GetMapping("/lists")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "全部", notes = "传入workbatchShiftset")
    public R<List<WorkbatchShiftset>> lists(Integer dpId) {
        //查公司的班次
        Map<String, Object> map = new HashMap<>();
        map.put("model", 1);
        List<WorkbatchShiftset> list = workbatchShiftsetService.listByMap(map);
        //查車間的班次
        Map<String, Object> dpMap = new HashMap<>();
        dpMap.put("model", 2);
        dpMap.put("db_id", dpId);
        List<WorkbatchShiftset> dpList = workbatchShiftsetService.listByMap(dpMap);

        if (Func.isEmpty(list)) {
            list = new ArrayList<>();
        }
        if (Func.isEmpty(dpList)) {
            dpList = new ArrayList<>();
        }
        list.addAll(dpList);
        return R.data(list);
    }

    /**
     * 车间查询班次，按照时间区间分组
     */
    @PostMapping("/listBydbId")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "车间下的班次", notes = "传入workbatchShiftset")
    public R<List<WorkbatchShiftsetVO>> listBydbId(@Valid @RequestBody WorkbatchShiftset workbatchShiftset) {
//        查询公司下的班次信息
        List<WorkbatchShiftsetVO> list = workbatchShiftsetService.getWorkbatchShiftByStartAndEndDate(1, null);
//        查询部门车间下的班次
        List<WorkbatchShiftsetVO> listBydp = workbatchShiftsetService.getWorkbatchShiftByStartAndEndDate(workbatchShiftset.getModel(), workbatchShiftset.getDbId());
        if (!Func.isEmpty(list)) {
            if (!Func.isEmpty(listBydp)) {
                list.addAll(listBydp);
            }
        } else {
            if (!Func.isEmpty(listBydp)) {
                list = listBydp;
            }
        }
        return R.data(list);
    }

    /**
     * 查询当前时间所在班次
     */
    @RequestMapping("/getWorkbatchShiftsetList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "班次信息")
    public R<List<WorkbatchMachShiftVO>> getWorkbatchShiftsetList(Integer maId) {
        List<WorkbatchMachShiftVO> workbatchShiftsetList = workbatchShiftMapper.findByMaId(maId);
        return R.data(workbatchShiftsetService.getWorkbatchShiftsetList(workbatchShiftsetList));
    }


    @GetMapping("/getlists")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "全部", notes = "传入workbatchShiftset")
    public R<List<WorkbatchShiftset>> getlists(Integer dpId) {
        //DBIdentifier.setProjectCode("xingyi");
        //查公司的班次
//        Map<String, Object> map = new HashMap<>();
//        map.put("model", 1);
//        List<WorkbatchShiftset> list = workbatchShiftsetService.getWorkbatchShiftsetByModel(1);//设定模型为1的，默认为公司
//        //List<WorkbatchShiftset> list = workbatchShiftsetService.listByMap(map);
//        //查車間的班次
//        Map<String, Object> dpMap = new HashMap<>();
//        dpMap.put("model", 2);
//        dpMap.put("db_id", dpId);
//        List<WorkbatchShiftset> dpList = workbatchShiftsetService.getWorkbatchShiftsetByModel(2);//设定模型为2的，默认为公司
//        //List<WorkbatchShiftset> dpList = workbatchShiftsetService.listByMap(dpMap);
//
//        if (Func.isEmpty(list)) {
//            list = new ArrayList<>();
//        }
//        if (Func.isEmpty(dpList)) {
//            dpList = new ArrayList<>();
//        }
        List<WorkbatchShiftset> list = workbatchShiftsetService.getMainShift();
//        list.addAll(list);
        return R.data(list);
    }

    /**
     * 查询 排产班次设定_yb_workbatch_shifts（公司）
     */
    @RequestMapping("/getWorkbatchModel")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "公司的班次", notes = "无参")
    public R getWorkbatchModel() {
        Map<String, Object> map = new HashMap<>();
        map.put("db_id", null);
        return R.data(workbatchShiftsetService.listByMap(map));
    }


    /**
     * 查询 排产班次设定_yb_workbatch_shifts（设备）
     */
    @GetMapping("/getWorkBatch")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "设备的班次", notes = "设备id")
    public R getWorkBatch(String maId) {
        return R.data(workbatchShiftMapper.findByMaIds(maId));
    }

    @GetMapping("/getShiftsetByMatype")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "设备的班次", notes = "设备id")
    public R getShiftsetByMatype(Integer maType) {
        return R.data(workbatchShiftsetMapper.getShiftsetByMatype(maType));
    }

}


