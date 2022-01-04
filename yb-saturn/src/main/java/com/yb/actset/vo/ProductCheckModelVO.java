package com.yb.actset.vo;

import com.yb.prod.entity.ProdPdinfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductCheckModelVO extends ProdPdinfo implements Serializable {
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
}
