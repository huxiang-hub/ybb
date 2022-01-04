package com.yb.actset.controller;


import com.yb.actset.entity.ActsetCkset;
import com.yb.actset.service.IActsetCkflowService;
import com.yb.actset.service.IActsetCksetService;
import com.yb.actset.vo.CheckViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;

/**
 * yb_actset_ckflow
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/actset")
@Api(value = "yb_actset_ckflow", tags = "yb_actset_ckflow")
public class ActsetCkflowController extends BladeController {
    private IActsetCksetService iActsetCksetService;
    private IActsetCkflowService iActsetCkflowService;
    /***
     * as_ype A订单 B产品 C仓储
     * 审核流程图查询
     * POSTMAN 测试 http://192.168.0.106:2020/satapi/actset/getCheckView?checkType=A&orderId=109
     * 已经测试
     */
    @PostMapping("/getCheckView")
    @ApiOperation(value = "查找待审核记录", notes = "checkType 订单还是产品.. ,orderId id 订单或者产品")
    public R getCheckSort(String checkType,Integer orderId){
        /*先获取下流程设置表的流程*/
        HashMap<String,Object> map = new HashMap<>();
        map.put("as_type",checkType);
        ActsetCkset ckset =
                iActsetCksetService.getBaseMapper().selectByMap(map).get(0);
        /**
         * 设定的审核流程
         */
        List<CheckViewModel> setCheckView =
                iActsetCkflowService.getSetCheckSortInfo(ckset.getId(),ckset.getLeve());
        /**
         * 审核的整个流程 在审核的流程
         */
        List<CheckViewModel> checkView =
                iActsetCkflowService.getCheckSortInfo(ckset.getId(),ckset.getLeve(),orderId);
        map.clear();
        map.put("setCheckView",setCheckView);
        map.put("checkView",checkView);
        return R.data(map);
    }
}
