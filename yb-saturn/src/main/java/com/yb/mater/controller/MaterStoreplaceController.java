package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.mater.entity.MaterStoreplace;
import com.yb.mater.service.MaterStoreplaceService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/MaterStoreplace")
public class MaterStoreplaceController {

    @Autowired
    private MaterStoreplaceService materStoreplaceService;

    @RequestMapping("save")
    public R save(@RequestBody MaterStoreplace materStoreplace){
        Date date = new Date();
        materStoreplace.setIsUsed(1);
        materStoreplace.setCreateAt(date);
        materStoreplace.setUpdateAt(date);
        return R.status(materStoreplaceService.save(materStoreplace));
    }
    @RequestMapping("update")
    public R update(@RequestBody MaterStoreplace materStoreplace){
        Date date = new Date();
        materStoreplace.setIsUsed(1);
        materStoreplace.setCreateAt(date);
        materStoreplace.setUpdateAt(date);
        return R.status(materStoreplaceService.updateById(materStoreplace));
    }
    @RequestMapping("getByDbId")
    public R getByDbId(Integer mpType, Integer dbId){
        return R.data(materStoreplaceService.list(new QueryWrapper<MaterStoreplace>()
                .eq("mp_type", mpType).eq("db_id", dbId).eq("is_used", 1)));
    }

}
