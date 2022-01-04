package com.anaysis.hdverify.controller;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.anaysis.hdverify.entity.HdverifyMach;
import com.anaysis.hdverify.service.HdverifyMachService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/hdmac")
public class HdverifyMachController {

    @Autowired
    private HdverifyMachService hdverifyMachService;


    @PostMapping("/list")
    public R list(Model model) {
        //查询列表数据
        //System.out.println(new Date());
        List<SuperviseBoxinfoVo> machlist = hdverifyMachService.getMachList(new SuperviseBoxinfoVo());
        //model.addAttribute("machlist", machlist);
        return R.data(machlist);
    }

    /****
     * 查询设备id信息，判断是否是停机设备内容
     * @param maId
     * @return
     */
    @PostMapping("/hdrun/{maId}")
    public R hdrun(@PathVariable("maId") Integer maId) {
        if (maId > 0) {
            //判断设备是否停机状态
            SuperviseBoxinfo boxinfo = hdverifyMachService.isStopByMaid(maId);
            if (boxinfo != null) {//停机可以进入该页面
                return R.data(boxinfo);
            } else {
                return R.fail("不是停机设备。");
            }
        }
        return R.fail("没有选中设备信息。");
    }

    /*****
     * 进入验证页面，进行数据保存，第一要保证为停机状态，然后运行后就开始计数操作
     * @param hdverifyMach
     * @return
     */
    @PostMapping("/hdverify")
    public R hdverify(HdverifyMach hdverifyMach) {
        if (hdverifyMach != null && hdverifyMach.getMaId() != null) {
            SuperviseBoxinfo boxinfo = hdverifyMachService.isStopByMaid(hdverifyMach.getMaId());
            if (boxinfo != null) {//表示设备为停机设备
                //TODO
            } else {
                return R.fail("不是停机设备。");
            }
        }
        return R.fail("验证数据保存失败。");

    }
}
