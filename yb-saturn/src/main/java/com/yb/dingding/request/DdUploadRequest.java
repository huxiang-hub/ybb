package com.yb.dingding.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author my
 * #Description
 */
@Data
public class DdUploadRequest {


    @ApiModelProperty(value = "id集合",required = true)
    @NotEmpty(message = "id不能为空")
    List<Integer> ids;

    @ApiModelProperty(value = "随机数绑定",required = true)
    @NotBlank(message = "随机数不能为空")
    String code;
}
