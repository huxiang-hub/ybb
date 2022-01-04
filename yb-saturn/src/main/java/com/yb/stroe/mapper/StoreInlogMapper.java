package com.yb.stroe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.stroe.entity.StoreInlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzb
 * @date 2020-09-19
 */

@Mapper
public interface StoreInlogMapper extends BaseMapper<StoreInlog> {
    /**
     * 删除日志
     * @param noEtIdList
     */
    void deleteNoEtIdList(@Param("noEtIdList") List<Integer> noEtIdList);
}
