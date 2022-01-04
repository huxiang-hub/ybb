package com.yb.stroe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.stroe.entity.StoreArea;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreAreaMapper extends BaseMapper<StoreArea> {
    /**
     * 根据库位id查库区
     * @param stId
     * @return
     */
    StoreArea selectStoreAreaByStId(Integer stId);
}
