package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/23 11:18
 */
@ApiModel("班次排产单个数统计VO")
@Data
public class DeviceOrderNumProgressVO {
    List<DeviceOrderNumProgressListVO> deviceOrderNumProgressListVOS;

    @ApiModelProperty("总完成单数")
    private Integer totalFinishNum;

    @ApiModelProperty("总计划单数")
    private Integer totalNum;

    @ApiModelProperty("等待生产单数")
    private Integer waitProduceNum;

    @ApiModelProperty("正在生产单数")
    private Integer currentNum;

    @ApiModelProperty("暂停未下发数")
    private Integer stopNum;
}
