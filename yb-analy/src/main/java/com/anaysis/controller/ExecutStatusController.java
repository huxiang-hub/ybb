package com.anaysis.controller;

import com.anaysis.common.StringUtils;
import com.anaysis.executSupervise.ExecutConstant;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.service.AnalyTenantService;
import com.anaysis.executSupervise.service.SuperviseBoxinfoService;
import com.anaysis.executSupervise.service.SuperviseExecuteService;
import com.anaysis.service.BoxinfoViewService;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.exception.CommonException;
import org.springblade.core.tool.api.R;
import org.springblade.system.feign.ICheckClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ExecutStatusController implements ICheckClient {

    @Autowired
    private BoxinfoViewService boxinfoViewService;
    @Autowired
    private SuperviseExecuteService executeService;
    @Autowired
    private SuperviseBoxinfoService boxInfoService;
    @Autowired
    private AnalyTenantService analyTenantService;

    Map<String, Date> map = new HashMap<>();


    @Override
    @GetMapping("/exprod/start")
    public R exeStatus(@RequestParam("maId") Integer maId, @RequestParam("uuid") String uuid) {
        if (!StringUtils.isNotBlank(uuid)) {
            log.error("更改实时订单异常结束，设备id不存在实时订单表中:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "解析设置缓存失败，uuid不存在");
        }
        analyTenantService.getTenantId(uuid);
        //查询列表数据
        System.out.println("::::::::" + new Date());
        System.out.println("executeState:::::::::" + maId);
        //TODO 订单正式开始触发的方法内容，需要加载对应的对象到内部缓存中信息
        SuperviseExecute execute = executeService.getUuidByMaId(maId);
        ExecutConstant.setMachineProc(execute.getUuid());
        return R.success("访问OK");
    }
}
