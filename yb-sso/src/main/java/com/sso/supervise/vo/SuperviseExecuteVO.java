package com.sso.supervise.vo;


import com.sso.supervise.entity.SuperviseExecute;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备清零日志表视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseExecutVO对象", description = "设备实时订单状态")
public class SuperviseExecuteVO extends SuperviseExecute {
    private static final long serialVersionUID = 1L;
    /**
     * 补充关联表查询  限制弹窗时间
     */
    private Integer limitTime; //设置限制时间

    /**
     * 补充关联表查询 限制记录时间
     */
    private Integer syslimitTime;

    /*补充关联表人员名称*/
    private String userName;
    /*补充关联表设备名称*/
    private String equipmentName;
    /*补充关联表已生产数*/
    private Integer completeNum;
    /*补充关联表订单名称*/
    private String odName;
    /*补充关联表订单编号*/
    private String odNo;
    /*补充关联表生产总数*/
    private Integer odCount;
    /*补充关联表截止时间*/
    private String limitDate;
    /*补充关联表超期时间*/
    private Long exceedDate;
    /*补充关联表订单状态*/
    private String odStatus;
}
