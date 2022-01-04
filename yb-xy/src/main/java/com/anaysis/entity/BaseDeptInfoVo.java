package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("yb_base_deptinfo")
@ApiModel(value = "BaseDeptinfo对象", description = "部门结构_yb_ba_dept")
public class BaseDeptInfoVo extends BaseDeptInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门对应车间，二级间
     */
    private List<BaseDeptInfoVo> children;
}
