package com.yb.system.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/21
 */
@ApiModel("系统管理用户list请求类")
@Data
public class UserListRequest extends Query {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("用户名")
    private String account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("部门id集合")
    private List<Integer> deptIds;
}
