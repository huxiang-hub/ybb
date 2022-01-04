package com.yb.base.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yb.base.entity.BaseStaffclass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BaseStaffclassVO对象", description = "人员班组临时调班_yb_base_staffclass")
public class BaseStaffclassVO extends BaseStaffclass implements Serializable {
    private static final long serialVersionUID = 1L;
//    临时部门
    private String newClass;
}
