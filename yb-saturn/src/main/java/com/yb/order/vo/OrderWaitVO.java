package com.yb.order.vo;

import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class OrderWaitVO extends OrderOrdinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    String pdName;
    List<String> materMtinfos;
    List<String> prodProcelink;
    /*订单所包含的批次*/
    List<OrderWorkbatch> wbNames;
    /*订单未分批数量*/
    Integer orderUnfinishedNum;
}
