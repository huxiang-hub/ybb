package com.yb.actset.vo;
import lombok.Data;
import java.io.Serializable;
@Data
public class CheckModelVO implements Serializable {

    /**
     * 审核记录id
     */
    private Integer logId;
    /***
     * ck_flow 主键Id
     */
    private Integer awId;
    /**
     *类型
     *    订单 A  ，产品 B
     */
    private String asType;
    /**
     * 审核的细分定位
     * sale 销售制单审核
     * technology 提交工艺
     * produce 生产审核
     * storage 仓库 审核
     * plan 计划产审核
     */
    private String awType;
    /**
     * 订单编号 或者 产品
    */
    private Integer orderId;
    /**
     * 审核结果
     * 同意 或者  拒绝
     */
    private String result;
    /**
     * 审核人的ID
     */
    private Integer userId;
    /**
     * 审核状态
     */
    private Integer status;
    /**
     * 标记区分是审核产品 还是 订单，还是 仓租
     */
    private Integer flag;
    /**
     * 订单编号
     */
    private String odNo;
    /**
     * 订货厂家
     */
    private String cmName;
    /**
     * 工序id
     */
    private Integer prId;
    /**
     * 设备id
     */
    private Integer maId;
}
