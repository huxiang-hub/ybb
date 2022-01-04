package com.anaysis.controller;

import com.anaysis.entity.BoxinfoViewEntity;
import com.anaysis.service.BoxinfoViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/show")
public class ShowStatusController {

    @Autowired
    private BoxinfoViewService boxinfoViewService;

    @GetMapping("")
    public String list(Model model){
        //查询列表数据
        System.out.println(new Date());
        List<BoxinfoViewEntity> boxlist = boxinfoViewService.getList();
        model.addAttribute("boxlist", boxlist);
        return "showstatus/showstatus";

    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        //查询列表数据
        System.out.println("13232321");
      return "success";
    }
}
