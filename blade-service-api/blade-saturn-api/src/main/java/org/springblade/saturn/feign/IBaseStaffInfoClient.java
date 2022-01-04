package org.springblade.saturn.feign;

import org.springblade.core.tool.api.R;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.saturn.entity.BaseStaffinfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value ="yb-saturn",
        fallback = BaseStaffInfoClientFallback.class)
public interface IBaseStaffInfoClient {

    @PostMapping("/getStaffinfoById")
    public R<BaseStaffinfo> getStaffinfoById(Integer id);

    @PostMapping("/update")
    public R update(@Valid @RequestBody BaseStaffext baseStaffext);


    @PostMapping("/getStaffextById")
    public R<BaseStaffext> getStaffextById(Integer id);


    @PostMapping("/updateStaffinfo")
    public R updateStaffinfo(@Valid @RequestBody BaseStaffinfo baseStaffinfo);
}
