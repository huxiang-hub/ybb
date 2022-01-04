package com.yb.statis.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Description: 达成率请求类
 * @Author my
 * @Date Created in 2020/6/19 14:27
 */
@Data
@ApiModel("当天达成率分页请求")
public class StatisOrdreachPageRequest {

    /**
     * 查询的日期
     */
    @ApiModelProperty("查询日期")
    private String targetDay;

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private String maId;

    @ApiModelProperty("设备id集合")
    private List<Integer> maIdList;

    /**
     * 班次id
     */
    @ApiModelProperty("班次id")
    private Integer wsId;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private String maType;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String dpId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("班次id")
    private Integer shift;

    /**
     * 下天日期
     */
    @ApiModelProperty("下天日期")
    @JsonIgnore
    private String nextDay;
}
