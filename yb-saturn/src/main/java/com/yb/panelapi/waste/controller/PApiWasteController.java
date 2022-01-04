package com.yb.panelapi.waste.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.OuterDataUrl;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.execute.wrapper.ExecuteWasteWrapper;
import com.yb.panelapi.waste.entity.WasteVo;
import com.yb.panelapi.waste.service.IPApiWasteService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@RestController
@RequestMapping("/plapi/waste")
public class PApiWasteController {

    @Autowired
    private IPApiWasteService apiWasteService;
    @Autowired
    private IExecuteWasteService executeWasteService;


    @GetMapping("/executelist")
    public R<IPage<ExecuteWasteVO>> list(ExecuteWaste executeWaste, Query query) {
        IPage<ExecuteWaste> pages = executeWasteService.page(Condition.getPage(query),
                Condition.getQueryWrapper(executeWaste));
        return R.data(ExecuteWasteWrapper.build().pageVO(pages));
    }

    /**
     * 订单上报需要通过设备id获取废品的种类
     */
    @RequestMapping("/list")
    R<List<WasteVo>> getWasteByMid(String maId) {
        return R.data(0, apiWasteService.getWaste(maId), "获取成功");
    }

    /**
     * 测试数据进行远程解析数据进行远程调用检测
     */
    @PostMapping("/start")
    public R exestatus(Model model) {
        //查询列表数据
        System.out.println(new Date());

        OuterDataUrl sendurl = new OuterDataUrl();
        sendurl.sendExestart(new ExecuteState());

        System.out.println(model);
        return R.success("访问OK");
    }
}
