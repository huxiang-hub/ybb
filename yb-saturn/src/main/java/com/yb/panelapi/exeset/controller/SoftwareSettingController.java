package com.yb.panelapi.exeset.controller;

import com.yb.exeset.service.IExesetFaultService;
import com.yb.exeset.service.IExesetQualityService;
import com.yb.panelapi.exeset.vo.ModelBean;
import com.yb.panelapi.user.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "软件参数设置",tags = "")
@RequestMapping("/plapi")
public class SoftwareSettingController {

    @Autowired
    private IExesetQualityService iExesetQualityService;
    @Autowired
    private IExesetFaultService iExesetFaultService;
    /**
     * @param
     */
    @PostMapping("/setFaultpop")
    @ApiOperation(value = "设置停机窗口参数",tags = "yb_exeset_fault前端"+
            "需要给我返回一个带有多个用“|”参数拼接的字符串（弹窗设置，工单间隔，隐藏时间） ")
    @ResponseBody
    public R AjaxUpdateFaultPoP (@RequestBody ModelBean model){
        if (iExesetFaultService.selectExesetFault(model.getFault().getMaId())==null) {
            iExesetFaultService.save(model.getFault());
        }else {
            iExesetFaultService.updateById(model.getFault());
        }
        if (iExesetQualityService.getQualityModel(model.getQuality().getMaId())==null) {
            iExesetQualityService.save(model.getQuality());
        }else {
            iExesetQualityService.updateById(model.getQuality());
        }



        return R.ok("修改成功！");
    }

    @PostMapping("/getFaultinfo")
    @ApiOperation(value = "设置停机窗口参数",tags = "yb_exeset_fault前端"+
            "需要给我返回一个带有多个用“|”参数拼接的字符串（弹窗设置，工单间隔，隐藏时间） ")
    @ResponseBody
    public R getAllFaultPoP(Integer maId){
        Map<String ,Object>  result = new HashMap<>();
        result.put("quality",iExesetQualityService.getQualityModel(maId));
        result.put("fault",iExesetFaultService.selectExesetFault(maId));
        return R.ok(result,"信息返回成功！");
    }

}
