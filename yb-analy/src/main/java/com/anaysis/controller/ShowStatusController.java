package com.anaysis.controller;

import com.anaysis.entity.BoxinfoViewEntity;
import com.anaysis.service.BoxinfoViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/show")
public class ShowStatusController {

    @Autowired
    private BoxinfoViewService boxinfoViewService;

    Map<String, Date> map = new HashMap<>();

    @GetMapping("")
    public String list(Model model) {
        //查询列表数据
        System.out.println(new Date());
        List<BoxinfoViewEntity> boxlist = boxinfoViewService.getList();
        model.addAttribute("boxlist", boxlist);
        return "showstatus/showstatus";
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable("id") Integer id) {
        if (id > 0) {
            if (map!=null&&!map.isEmpty()) {
                Date date = map.get("1");
                if (System.currentTimeMillis() - date.getTime() > 0) {
                    map.remove("1");
                }
            }
        } else {
            map.put("1", new Date());
        }
//        MachineOperate machineOperate = new MachineOperate();
//        machineOperate.extOperate("38FF71064E4D373527372343", 3000, 2000, "2");
    }
}
