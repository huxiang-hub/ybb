package com.yb.stroe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.vo.StoreSeatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreSeatMapper extends BaseMapper<StoreSeat> {
    /**
     * 查询上工序托盘的库位信息
     * @param upProcessSdId
     * @return
     */
    List<StoreSeatVO> upStoreSeatList(@Param("upProcessSdId") Integer upProcessSdId);

    /**
     * 根据执行单查询本工序库位详情列表
     * @param exId
     * @return
     */
    List<StoreSeatVO> getStoreSeatByExId(@Param("exId") Integer exId);
}
