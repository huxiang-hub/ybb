package com.yb.execute.controller;

import com.yb.base.mapper.BasePictureMapper;
import com.yb.execute.service.IExecuteScrapService;
import com.yb.execute.vo.ExecuteScrapVO;
import com.yb.execute.vo.SubmitAuditRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;
import java.util.Optional;

/**
 * 审核清单_yb_execute_scrap 控制器
 *
 * @author BladeX
 * @since 2021-03-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executescrap")
@Api(value = "审核清单_yb_execute_scrap", tags = "审核清单_yb_execute_scrap接口")
public class ExecuteScrapController extends BladeController {

    private final IExecuteScrapService executeScrapService;


    @PostMapping("/submitAudit")
    @ApiOperation(value = "手机盘点-提交审核清单")
    public R submitAudit(@RequestBody SubmitAuditRequest submitAuditRequest) {
        executeScrapService.submitAudit(submitAuditRequest);
        return R.success("提交成功");
    }

    @GetMapping("/batchAudit")
    @ApiOperation(value = "手机盘点-审核", notes = "传入ids")
    public R batchAudit(
            @ApiParam(value = "审核清单主键集合", required = true) @RequestParam String ids,
            @ApiParam(value = "审核意见", required = true) @RequestParam String exApprove,
            @ApiParam(value = "接收人-待审核", required = true) @RequestParam Integer acceptUsids,
            @ApiParam(value = "审核状态 1待审核2审核通过3审核驳回", required = true) @RequestParam Integer exStatus,
            @ApiParam(value = "审核人id", required = true) @RequestParam Integer usId) {
        executeScrapService.batchAudit(Func.toLongList(ids), usId, exStatus, acceptUsids, exApprove);
        return R.success("审核成功");
    }

    @GetMapping("/getPhoneList")
    @ApiOperation("手机盘点-查询审核列表")
    public R<List<ExecuteScrapVO>> getPhoneList(
            @ApiParam(value = "审核人id", required = true) @RequestParam Integer usId,
            @ApiParam(value = "审核状态:1待审核，2已经审核", required = true) @RequestParam String exStatus) {
        List<ExecuteScrapVO> list = executeScrapService.getPhoneList(usId, exStatus);
        return R.data(list);
    }
    @GetMapping("/getPhoneDetail")
    @ApiOperation("手机盘点-查询审核详情")
    public R<ExecuteScrapVO> getPhoneDetail(@ApiParam(value = "审核列表id", required = true) @RequestParam Integer id) {
        ExecuteScrapVO list = executeScrapService.getPhoneDetail(id);
        return R.data(list);
    }




}
