package com.anaysis.controller;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.parames.ProceduerParam;
import com.anaysis.sqlservermapper.HrProcedureMapper;
import com.anaysis.utis.ProceduerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.nxhr.param.*;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/4/1 16:43
 **/
@Api(tags = "和瑞存储过程接口")
@RestController
@RequestMapping("/procedure")
public class ProcedureController {

    @Autowired
    private ProceduerUtil proceduerUtil;
    @Autowired
    private HrProcedureMapper procedureMapper;

    @ApiOperation("调用erp存储过程")
    @PostMapping("executeProcedure")
    public R<List<JSONObject>> executeProcedure(@RequestBody ProceduerParam param) {
        List<JSONObject> result = proceduerUtil.execute(param.getProcedureName(), param.getParamList(), param.getParamMap());
        return R.data(result);
    }

    @ApiOperation("车间生产数据上报(次品)")
    @GetMapping("badProdInsert")
    public R badProdInsert(BadProdInsertParam param) {
        Map<String, String> map = procedureMapper.execProcedure(param.buildParam(), "SP_ODIWorkProdBInsert");
//        return R.data(proceduerUtil.execute("SP_ODIWorkProdBInsert", null, param.buildParam()));
        return R.data(map);
    }

    @ApiOperation("车间生产数据上报(正品)")
    @GetMapping("goodProdInsert")
    public R goodProdInsert(GoodProdInsertParam param) {
        Map<String, String> map = procedureMapper.goodProdInsert(param);
        return R.data(map);
//        return R.data(proceduerUtil.execute("SP_ODIWorkProdInsert", null, param.buildParam()));
    }

    @ApiOperation("成品入库")
    @GetMapping("storeFinishedProdInsert")
    public R storeFinishedProdInsert(StoreFinishedProdInsertParam param) {
//        return R.data(proceduerUtil.execute("SP_ODIStoreFGReceiveInsert", null, param.buildParam()));
        return R.data(procedureMapper.storeFinishedProdInsert(param.buildParam(), "SP_ODIStoreFGReceiveInsert"));
    }

    @ApiOperation("成品退车间新增")
    @GetMapping("storeRejectInsert")
    public R storeRejectInsert(StoreRejectInsertParam param) {
//        return R.data(proceduerUtil.execute("SP_ODIStoreFGRejectInsert", null, param.buildParam()));
        return R.data(procedureMapper.execProcedure(param.buildParam(), "SP_ODIStoreFGRejectInsert"));
    }

    @ApiOperation("修改工单状态")
    @GetMapping("workPlanUpdateStatus")
    public R workPlanUpdateStatus(WorkPlanUpdateStatusParam param) {
//        return R.data(proceduerUtil.execute("SP_ODIWorkPlanUpdateStatus", null, param.buildParam()));
        return R.data(procedureMapper.execProcedure(param.buildParam(), "SP_ODIWorkPlanUpdateStatus"));
    }
}
