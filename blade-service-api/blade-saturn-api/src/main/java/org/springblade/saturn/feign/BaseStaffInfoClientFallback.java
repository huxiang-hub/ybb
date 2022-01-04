package org.springblade.saturn.feign;
import org.springblade.core.tool.api.R;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.saturn.entity.BaseStaffinfo;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class BaseStaffInfoClientFallback implements IBaseStaffInfoClient {

    @Override
    public R<BaseStaffinfo> getStaffinfoById(Integer id) {
        return R.fail("服务不可用");
    }


    @Override
    public R update(@Valid BaseStaffext baseStaffext) {
         return R.fail("服务不可用");
    }

    @Override
    public R<BaseStaffext> getStaffextById(Integer id) {
        return R.fail("服务不可用");
    }


    @Override
    public R updateStaffinfo(@Valid BaseStaffinfo baseStaffinfo) {
        return R.fail("服务不可用");
    }
}
