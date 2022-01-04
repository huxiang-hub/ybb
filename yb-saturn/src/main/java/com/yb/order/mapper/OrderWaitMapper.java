package com.yb.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.actset.vo.CheckModelVO;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.vo.OrderWaitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderWaitMapper extends BaseMapper<OrderOrdinfo> {

    /*查询待生产订单*/
    List<OrderWaitVO> selectOrderWaitV(CheckModelVO modelVO, IPage<OrderWaitVO> page, String indentor, String odNo);

    OrderWaitVO selectOrderWaitById(Integer odId);

    Integer orderWaitCount(String indentor, String odNo);

    List<OrderWaitVO> orderList(@Param("modelVO") CheckModelVO modelVO);
}
