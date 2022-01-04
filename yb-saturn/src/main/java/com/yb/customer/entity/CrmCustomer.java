package com.yb.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 客户管理管理_yb_crm_customer实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_crm_customer")
@ApiModel(value = "CrmCustomer对象", description = "客户管理_yb_crm_customer")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户编码（当前默认4个英文字母，如果不够的用V替代）
     */
    @ApiModelProperty(value = "客户编码（当前默认4个英文字母，如果不够的用V替代）")
    @TableField("cm_no")
    private String cmNo;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField("cm_name")
    private String cmName;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "缩写名")
    @TableField("cm_shortname")
    private String cmShortname;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company")
    private String company;
    /**
     * 状态1起草2提交(待审批)3同意4不同意
     */
    @ApiModelProperty(value = "社会唯一编号")
    @TableField("unique_no")
    private String uniqueNo;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "所在地址")
    @TableField("addr")
    private String addr;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    private Date createAt;

    /**
     *客户类型数据字典(1常规丶2食品)
     */
    @ApiModelProperty(value = "客户类型数据字典(1常规丶2食品)")
    private Integer cmType;

    /**
     *客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private Integer grade;

}
