package com.yb.workbatch.request;

import com.yb.common.constant.LocalEnum;
import com.yb.workbatch.vo.SdIdNumberVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description: 批量下发/挂起/废弃/驳回/排产 请求类
 * @Author my
 * @Date Created in 2020/7/25 9:26
 */
@ApiModel("批量下发/挂起/废弃/驳回/排产 请求类")
@Data
public class WorkbatchOrdlinkStatusUpdateRequest {

//    @ApiModelProperty(value = "排产ids(必传)", required = true)
//    @NotEmpty(message = "排产id不能为空")
    private List<Integer> sdIds;

    @ApiModelProperty(value = "排产ids(必传)", required = true)
    private List<SdIdNumberVo> sdIdNumberVos;

    private List<Integer> wfIds;
    @ApiModelProperty(value = "状态(下发" +
            "ISSUED," +
            "挂起" +
            "HANG," +
            "废弃" +
            "DISCARD," +
            "驳回" +
            "DOWN," +
            "排产" +
            "SCHEDULING)", required = true)
    @NotNull(message = "状态不能为空")
    private LocalEnum.WorkbatchOrdlinkStatus status;

    @ApiModelProperty("班次id")
    private Integer wsId;

    @ApiModelProperty("排产日期")
    private String sdDate;

    @ApiModelProperty("设备id")
    private Integer maId;

//    private Integer reachIslock; //小时达成率锁定系统统计 是否系统计算0不锁定1锁定(手工统计)
//
//    private Integer wfsortIslock; //是否锁定排产顺序 0不锁定 1锁定（需要进行授权调整排产顺序）
}
