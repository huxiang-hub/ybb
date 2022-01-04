package com.yb.stroe.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.mapper.StoreOutlogMapper;
import com.yb.stroe.service.IStoreOutlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 库位出库管理_yb_store_outlog 服务实现类
 * </p>
 *
 * @author lzb
 * @since 2020-09-19
 */
@Service
public class StoreOutlogServiceImpl extends ServiceImpl<StoreOutlogMapper, StoreOutlog> implements IStoreOutlogService {

    @Autowired
    private StoreOutlogMapper storeOutlogMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;


    @Override
    public List<StoreOutlog> getByWbNoPrId(String wbNo, Integer prId) {
        List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.selectList(
                Wrappers.<WorkbatchOrdlink>lambdaQuery()
                        .eq(WorkbatchOrdlink::getWbNo, wbNo)
                        .eq(WorkbatchOrdlink::getPrId, prId)
                        .select(WorkbatchOrdlink::getId)
        );
        if (workbatchOrdlinks.size() == 0) {
            return null;
        }
        List<Integer> workIds = workbatchOrdlinks.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<ExecuteTraycard> executeTraycards = executeTraycardMapper.selectList(
                Wrappers.<ExecuteTraycard>lambdaQuery()
                        .in(ExecuteTraycard::getSdId, workIds)
                        .select(ExecuteTraycard::getId)
        );
        if (executeTraycards.size() == 0) {
            return null;
        }
        List<Integer> etIds = executeTraycards.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<StoreOutlog> storeOutlogs = storeOutlogMapper.selectList(
                Wrappers.<StoreOutlog>lambdaQuery()
                        .in(StoreOutlog::getEtId, etIds));
        return storeOutlogs;
    }

    @Override
    public void saveByInventory(StoreInventory inventory, Integer number) {
        StoreOutlog storeOutlog = new StoreOutlog();
        storeOutlog.setStType(inventory.getStType());
        storeOutlog.setStId(inventory.getStId());
        storeOutlog.setStNo(inventory.getStNo());
        storeOutlog.setStSize(inventory.getStSize());
        storeOutlog.setMlId(inventory.getMlId());
        storeOutlog.setEtId(inventory.getEtId());
        storeOutlog.setLayNum(inventory.getLayNum());
        storeOutlog.setEtPdnum(number);
        storeOutlog.setDbId(inventory.getDbId());
        storeOutlog.setStModel(inventory.getStModel());
//            storeOutlog.setUsId();
        storeOutlog.setOperateType(1);
        storeOutlog.setCreateAt(LocalDateTime.now());
        storeOutlogMapper.insert(storeOutlog);
    }
}
