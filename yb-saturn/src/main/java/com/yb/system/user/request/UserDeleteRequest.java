package com.yb.system.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/11/3 10:58
 */
@Data
public class UserDeleteRequest {

    @ApiModelProperty(required = true)
    @NotEmpty(message = "id 不能为空")
    List<Integer> ids;
}
