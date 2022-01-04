package com.yb.mater.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author my
 * @date 2020-07-25
 * Description: 排产物料关联表_yb_mater_batchlink
 */
@Data
@TableName("yb_mater_batchlink")
public class MaterBatchlink implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer Id;
    /**
     * 排产Id
     */
    private Integer sdId;

    /**
     * 工单编号
     */
    private String wkNo;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 供应商
     */
    private String supplyName;

    /**
     * 摆放位置
     */
    private String location;

    /**
     * 工序应产数
     */
    private Integer processNum;

    /**
     * 实收数量
     */
    private Integer realacceptNum;

    /**
     * 板数（可选）
     */
    private String plateNum;

    /**
     * 发起-用户id-pmc要求人员
     */
    private Integer usId;

    /**
     * 收货人usid
     */
    private Integer receiveUsid;

    /**
     * 收货人姓名
     */
    private String contacterName;

    /**
     * 入库提交时间-请求
     */
    private String instorageTime;

    /**
     * 入库实际时间
     */
    private Date inTime;

    /**
     * 0发起请求1部分入库2全部入库
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 物料id
     */
    private Integer mlId;

    /**
     * 物料类型id
     */
    private Integer mcId;
}
