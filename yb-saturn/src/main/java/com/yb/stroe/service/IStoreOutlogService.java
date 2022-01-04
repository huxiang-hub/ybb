package com.yb.stroe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lzb
* @date 2020-09-19
*/

@Service
public interface IStoreOutlogService extends IService<StoreOutlog> {

    List<StoreOutlog> getByWbNoPrId(String wbNo, Integer prId);

    /**
     * 根据台账记录保存
     * @param inventory 台账记录
     * @param number 出库数量
     */
    void saveByInventory(StoreInventory inventory, Integer number);
}
