package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-11-27
 */
@Data
@TableName("yb_order_ordinfo")
@Accessors(chain = true)
@ApiModel(value = "订单表_yb_order_ordinfoOrderOrdinfo实体")
public class OrderOrdinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "产品id")
    @TableField("pd_id")
    private Integer pdId;


    @ApiModelProperty(value = "订单名称")
    @TableField("od_name")
    private String odName;


    @ApiModelProperty(value = "订单编号")
    @TableField("od_no")
    private String odNo;


    @ApiModelProperty(value = "订单数量")
    @TableField("od_count")
    private Integer odCount;


    @ApiModelProperty(value = "合同编号")
    @TableField("contract_num")
    private String contractNum;


    @ApiModelProperty(value = "合同名称")
    @TableField("contract_name")
    private String contractName;


    @ApiModelProperty(value = "预计废品")
    @TableField("waste_count")
    private Integer wasteCount;


    @ApiModelProperty(value = "客户id")
    @TableField("cm_id")
    private Integer cmId;


    @ApiModelProperty(value = "客户缩写")
    @TableField("cm_shortname")
    private String cmShortname;


    @ApiModelProperty(value = "订货厂家-客户名称")
    @TableField("cm_name")
    private String cmName;


    @ApiModelProperty(value = "截止日期")
    @TableField("limit_date")
    private LocalDate limitDate;


    @ApiModelProperty(value = "审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过  ")
    @TableField("audit_status")
    private Integer auditStatus;


    @ApiModelProperty(value = "生产状态   0 未执行  1 正在执行 2 已完成")
    @TableField("production_state")
    private Integer productionState;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "操作人")
    @TableField("us_id")
    private Integer usId;


    @ApiModelProperty(value = "订单备注")
    @TableField("remark")
    private String remark;


    @ApiModelProperty(value = "ERP的UUID")
    @TableField("erp_id")
    private String erpId;

    @ApiModelProperty(value = "客户ErpId")
    @TableField(exist = false)
    private String customerErpId;

    @ApiModelProperty(value = "产品编号")
    @TableField(exist = false)
    private String prodNo;

    @ApiModelProperty(value = "产品erp唯一id")
    @TableField(exist = false)
    private String prodErpId;

}
