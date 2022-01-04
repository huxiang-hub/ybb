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
 * @date 2020-11-25
 */
@Data
@TableName("yb_process_classify")
@Accessors(chain = true)
@ApiModel(value = "工序分类表_yb_process_classifyProcessClassify实体")
public class ProcessClassify implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "分类名称")
    @TableField("py_name")
    private String pyName;


    @ApiModelProperty(value = "分类编号")
    @TableField("py_num")
    private String pyNum;


    @ApiModelProperty(value = "显示顺序")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "状态 1启用0停用")
    @TableField("status")
    private Integer status;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "erp的uuid")
    @TableField("erp_id")
    private String erpId;

}
