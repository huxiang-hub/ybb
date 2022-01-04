package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/27 13:38
 */
@Data
@ApiModel("设备工序分页请求类")
public class ProcessFlowPageRequest extends Query {

    @ApiModelProperty("工单号")
    private String wbNo;

    @ApiModelProperty("产品名称")
    private String cmName;

    @ApiModelProperty("生产状态")
    private String status;

    @ApiModelProperty("产品名称")
    private String pdType;
}
