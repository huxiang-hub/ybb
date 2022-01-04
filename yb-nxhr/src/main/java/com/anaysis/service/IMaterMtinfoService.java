package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.MaterMtinfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @author lzb
* @date 2020-11-30
*/

public interface IMaterMtinfoService extends IService<MaterMtinfo> {

    void syn();

    @Select("")
    MaterMtinfo getByErpId(String erpId);
}
