package com.yb.system.dept.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.node.TreeNode;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/11/4 16:11
 */
@Data
public class DeptTreeNode extends TreeNode {

    @ApiModelProperty("全称")
    private String fullName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("类型")
    private Integer classify;
}
