package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-11-25
 */
@Data
@TableName("yb_process_classlink")
@Accessors(chain = true)
@ApiModel(value="工序分类关联表yb_process_classlinkProcessClasslink实体")
public class ProcessClasslink implements Serializable {


@ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


@ApiModelProperty(value = "工序id")
    @TableField("pr_id")
    private Integer prId;


@ApiModelProperty(value = "分类id")
    @TableField("py_id")
    private Integer pyId;

}
