package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @date 2020-11-26
 */
@Data
@TableName("yb_crm_customer")
@Accessors(chain = true)
@ApiModel(value = "CrmCustomer实体")
public class CrmCustomer implements Serializable {


    @ApiModelProperty(value = "流水id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "客户编码（当前默认4个英文字母，如果不够的用V替代）")
    @TableField("cm_no")
    private String cmNo;


    @ApiModelProperty(value = "客户名称")
    @TableField("cm_name")
    private String cmName;


    @ApiModelProperty(value = "缩写名")
    @TableField("cm_shortname")
    private String cmShortname;


    @ApiModelProperty(value = "客户类型数据字典(1常规丶2食品)")
    @TableField("cm_type")
    private Integer cmType;


    @ApiModelProperty(value = "公司名称")
    @TableField("company")
    private String company;


    @ApiModelProperty(value = "社会唯一编号")
    @TableField("unique_no")
    private String uniqueNo;


    @ApiModelProperty(value = "所在地址")
    @TableField("addr")
    private String addr;


    @ApiModelProperty(value = "客户等级")
    @TableField("grade")
    private Integer grade;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "erp的uuid")
    @TableField("erp_id")
    private String erpId;

}
