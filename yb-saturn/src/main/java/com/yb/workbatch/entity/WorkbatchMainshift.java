package com.yb.workbatch.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lzb
 * @date 2020-09-28
 */
@Data
@TableName("yb_workbatch_mainshift")
@Accessors(chain = true)
@ApiModel(value = "排班名称yb_workbatch_mainshiftWorkbatchMainshift实体")
public class WorkbatchMainshift implements Serializable {


    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "排班名称 白班/夜班/晚班/")
    @TableField("ck_name")
    private String ckName;


    @ApiModelProperty(value = "是否启用1停用0")
    @TableField("is_used")
    private Integer isUsed;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;


    /**
     * 操作人
     */
    @TableField(exist = false)
    private String userName;

}
