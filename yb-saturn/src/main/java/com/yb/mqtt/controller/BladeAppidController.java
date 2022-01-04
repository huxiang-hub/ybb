package com.yb.mqtt.controller;

import com.yb.mqtt.entity.BladeAppid;
import com.yb.mqtt.service.IBladeAppidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户连接app管理对应信息表 控制器
 *
 * @author BladeX
 * @since 2021-04-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bladeappid")
@Api(value = "租户连接app管理对应信息表", tags = "租户连接app管理对应信息表接口")
public class BladeAppidController extends BladeController {

    private final IBladeAppidService bladeAppidService;

    /**
     * 获取海王星列表信息
     */
    @GetMapping("/info")
    @ApiOperation(value = "海王星设备列表信息")
    public R<BladeAppid> info() {

        BladeAppid info = bladeAppidService.info();

        return R.data(info);

    }
}
