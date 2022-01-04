package com.yb.base.feign;


import com.yb.base.service.IBaseStaffextService;
import com.yb.base.service.IBaseStaffinfoService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.saturn.entity.BaseStaffinfo;
import org.springblade.saturn.feign.IBaseStaffInfoClient;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class BaseStaffInfoClient implements IBaseStaffInfoClient {

    private IBaseStaffinfoService iBaseStaffinfoService;
    private IBaseStaffextService iBaseStaffextService;


    @Override
    public R<BaseStaffinfo> getStaffinfoById(Integer id) {
        iBaseStaffinfoService.getById(id);
        return null;
    }

    @Override
    public R update(@Valid BaseStaffext baseStaffext) {
        return null;
    }

    @Override
    public R<BaseStaffext> getStaffextById(Integer id) {
        return null;
    }

    @Override
    public R updateStaffinfo(@Valid BaseStaffinfo baseStaffinfo) {
        return null;
    }
}
