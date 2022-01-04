package com.yb.stroe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.stroe.entity.StoreArea;

public interface StoreAreaService extends IService<StoreArea> {
    /**
     * 根据库位id查库区
     * @param stId
     * @return
     */
    StoreArea selectStoreAreaByStId(Integer stId);
}
