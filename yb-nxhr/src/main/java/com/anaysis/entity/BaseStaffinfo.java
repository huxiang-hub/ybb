package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lzb
 * @date 2020-11-25
 */
@Data
@TableName("yb_base_staffinfo")
@Accessors(chain = true)
@ApiModel(value = "人员表_yb_base_staffinfoBaseStaffinfo实体")
public class BaseStaffinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;


    @ApiModelProperty(value = "工号（系统生成-也可以手工修改）")
    @TableField("jobnum")
    private String jobnum;


    @ApiModelProperty(value = "联系电话")
    @TableField("phone")
    private String phone;


    @ApiModelProperty(value = "部门名称ID--车间")
    @TableField("dp_id")
    private Integer dpId;


    @ApiModelProperty(value = "类型：管理1/生产2/")
    @TableField("mold")
    private Integer mold;


    @ApiModelProperty(value = "岗位（角色）1.机长2.班长3.车间主管4排产员")
    @TableField("jobs")
    private Integer jobs;


    @ApiModelProperty(value = "劳动类型：1正式员工2临时员工3试用期4实习员工")
    @TableField("laborer")
    private Integer laborer;


    @ApiModelProperty(value = "工龄（入职时间）")
    @TableField("hire_time")
    private LocalDate hireTime;


    @ApiModelProperty(value = "生产型：工种|竖线分隔")
    @TableField("processes")
    private String processes;


    @ApiModelProperty(value = "班组id")
    @TableField("bc_id")
    private Integer bcId;


    @ApiModelProperty(value = "是否启用1、停用0")
    @TableField("is_used")
    private Integer isUsed;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "")
    @TableField("user_id")
    private Integer userId;


    @ApiModelProperty(value = "是否负责人1是0否")
    @TableField("is_manage")
    private Integer isManage;


    @ApiModelProperty(value = "erpUUID")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "erp的uuid--车间")
    @TableField("dp_erp")
    private String dpErp;


    @ApiModelProperty(value = "ID卡对应数字关联")
    @TableField("id_card")
    private String idCard;


    @ApiModelProperty(value = "钉钉主键id")
    @TableField("dd_id")
    private String ddId;

}
