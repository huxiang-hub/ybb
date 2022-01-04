package com.yb.actset.vo;

import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.order.entity.OrderOrdinfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrderCheckModelVO extends OrderOrdinfo implements Serializable {
    /***
     * yb_actset_checklog 表中的id
     */
    private Integer logId;
    /***
     * yb_actset_checklog 审核时间
     */
    private Date checkTime;
    /***
     * yb_actset_checklog 审核结果
     */
    private String result;
    /***
     * yb_actset_checklog 审核的状态
     */
    private Integer status;
    /***
     * yb_actset_checklog 审核流程主键
     */
    private Integer awId;
//    产品名称
    private String pdName;
//    所需物料
    private String mlNames;
    //    所需物料
    private String mtNums;
}
