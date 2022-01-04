package com.yb.stroe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lzb
* @date 2020-09-19
*/

@Service
public interface IStoreInlogService extends IService<StoreInlog> {
    /**
     * 删除日志
     * @param noEtIdList
     */
    void deleteNoEtIdList(List<Integer> noEtIdList);

    /**
     * 根据工单号和工序获取入库信息
     * @param wbNo
     * @param prId
     * @return
     */
    List<StoreInlog> getByWbNoPrId(String wbNo, Integer prId);

    /**
     * 根据台账记录保存
     * @param inventory 台账入库记录
     */
    void saveByInventory(StoreInventory inventory);
}
