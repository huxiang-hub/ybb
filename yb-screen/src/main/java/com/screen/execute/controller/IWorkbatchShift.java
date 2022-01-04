package com.screen.execute.controller;

import com.screen.execute.vo.WorkbatchShiftProcessVO;
import com.screen.execute.vo.WorkbatchShiftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "执行工单执行过程操作：接口访问路径定义/workbactch")
public interface IWorkbatchShift {

    @ApiOperation(value = "排产单列表：访问路径/shiftList", notes = "根据设备唯一标识maId，得到本设备的近三日的设备列表信息")
    R<List<WorkbatchShiftVO>> shiftList(@RequestParam("maId") Integer maId);

    @ApiOperation(value = "详情工艺路线：访问路径/shiftDetail", notes = "根据排程wfId唯一标识")
    R<WorkbatchShiftProcessVO> shiftDetail(@RequestParam(required = true) Integer wfId);
}