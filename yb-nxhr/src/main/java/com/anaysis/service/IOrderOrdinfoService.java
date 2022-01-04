package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.OrderOrdinfo;
/**
* @author lzb
* @date 2020-11-27
*/

public interface IOrderOrdinfoService extends IService<OrderOrdinfo> {

    void sync();
}
