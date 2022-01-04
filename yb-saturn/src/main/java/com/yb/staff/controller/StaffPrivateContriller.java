package com.yb.staff.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.staff.entity.StaffPrivate;
import com.yb.staff.service.StaffPrivateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/StaffPrivate")
@Api(tags = "自定义样式")
public class StaffPrivateContriller {

    @Autowired
    private StaffPrivateService staffPrivateService;

    /**
     * 保存排产字段样式
     * @return
     */
    @PostMapping("/saveXYWorkbatchOrdlink")
    @ApiOperation(value = "保存自定义样式")
    public R saveStaffPrivate(StaffPrivate staffPrivate){
        Integer usId = SaSecureUtil.getUserId();//获取当前登录人id
        Date date = new Date();
        String funKey = staffPrivate.getFunKey();
        String tabId = staffPrivate.getTabId();

        StaffPrivate usprv = new StaffPrivate();
        usprv.setUsId(usId);
        usprv.setTabId(tabId);
        usprv.setFunKey(funKey);
        usprv.setModel(1);//类型1表样式2通知
        usprv.setIsUsed(1);//设定可用的个性设置。
        StaffPrivate pivate = staffPrivateService.getPrivateInfo(usprv);

//        StaffPrivate pivate = staffPrivateService.getBaseMapper().selectOne(new QueryWrapper<StaffPrivate>()
//                .eq("us_id", usId).eq("model", 1).eq("fun_key", funKey).eq("tab_id", tabId).eq("is_used", 1));

        if(pivate != null){
            staffPrivate.setId(pivate.getId());
        }else if(pivate.getId() == null){
            staffPrivate.setCreateAt(date);
            staffPrivate.setIsUsed(1);//启用
            staffPrivate.setModel(1);//样式
            staffPrivate.setUsId(usId);//登录人id
            return R.status(staffPrivateService.saveOrUpdate(staffPrivate));
        }
        return R.status(true);
    }

    /**
     * 查询排产已设置的字段样式
     * @param
     * @return
     */
    @ApiOperation(value = "查询自定义样式")
    @RequestMapping("/selectXYWorkbatchOrdlink")
    public R selectStaffPrivate(String tabId, String funKey){
        Integer usId = SaSecureUtil.getUserId();//获取当前登录人id
        StaffPrivate usprv = new StaffPrivate();
        usprv.setUsId(usId);
        usprv.setTabId(tabId);
        usprv.setFunKey(funKey);
        usprv.setModel(1);//类型1表样式2通知
        usprv.setIsUsed(1);//设定可用的个性设置。
        StaffPrivate staffPrivate = staffPrivateService.getPrivateInfo(usprv);
//        StaffPrivate staffPrivate = staffPrivateService.getBaseMapper().selectOne(new QueryWrapper<StaffPrivate>()
//                .eq("tab_id", tabId).eq("fun_key", funKey).eq("model", 1).eq("us_id", usId).eq("is_used", 1));
        return R.data(staffPrivate);
    }

}
