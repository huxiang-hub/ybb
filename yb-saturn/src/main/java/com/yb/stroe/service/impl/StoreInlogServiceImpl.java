package com.yb.stroe.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.mapper.StoreInlogMapper;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 库位入库管理_yb_store_inlog 服务实现类
 * </p>
 *
 * @author lzb
 * @since 2020-09-19
 */
@Service
public class StoreInlogServiceImpl extends ServiceImpl<StoreInlogMapper, StoreInlog> implements IStoreInlogService {

    @Autowired
    private StoreInlogMapper inlogMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;

    /**
     * 根据标识卡ids删除
     * @param etIds
     */
    public void deleteBatchByEtIds(String etIds) {
        List<String> etIdList = Arrays.asList(etIds.split(etIds));
        QueryWrapper<StoreInlog> wrapper = new QueryWrapper<>();
        wrapper.in("et_id", etIdList);
        inlogMapper.delete(wrapper);
    }

    @Override
    public void deleteNoEtIdList(List<Integer> noEtIdList) {
        inlogMapper.deleteNoEtIdList(noEtIdList);
    }

    @Override
    public List<StoreInlog> getByWbNoPrId(String wbNo, Integer prId) {
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
        List<StoreInlog> storeInlogs = inlogMapper.selectList(
                Wrappers.<StoreInlog>lambdaQuery()
                        .in(StoreInlog::getEtId, etIds));
        return storeInlogs;
    }

    @Override
    public void saveByInventory(StoreInventory inventory) {
        StoreInlog storeInlog = new StoreInlog();
        storeInlog.setStType(inventory.getStType());
        storeInlog.setStId(inventory.getStId());
        storeInlog.setStNo(inventory.getStNo());
        storeInlog.setStSize(inventory.getStSize());
        storeInlog.setMlId(inventory.getMlId());
        storeInlog.setOperateType(1);
        storeInlog.setEtId(inventory.getEtId());
        storeInlog.setLayNum(inventory.getLayNum());
        storeInlog.setEtPdnum(inventory.getEtPdnum());
        storeInlog.setStModel(inventory.getStModel());
        storeInlog.setDbId(inventory.getDbId());
//        storeInlog.setUsId();
        storeInlog.setCreateAt(LocalDateTime.now());
        this.save(storeInlog);
    }
}

