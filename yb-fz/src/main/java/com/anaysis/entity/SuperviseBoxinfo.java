package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_supervise_boxinfo")
public class SuperviseBoxinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 盒子唯一标识
     */

    @ApiModelProperty(value = "盒子唯一标识uuid")
    private String uuid;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private Integer maId;
    /**
     * 状态1运行2停机3故障4离线
     */
    @ApiModelProperty(value = "状态1运行2停机3故障4离线")
    private String status;
    /**
     * 计数器数字
     */
    @ApiModelProperty(value = "计数器数字")
    private Integer number;
    /**
     * 当天数量
     */
    @ApiModelProperty(value = "当天数量")
    private Integer numberOfDay;
    /**
     * 当前时速
     */
    @ApiModelProperty(value = "当前时速")
    private Double dspeed;
//    /**
//     * 状态变化的开始时间
//     */
//    @ApiModelProperty(value = "状态变化的开始时间")
//    private Date startTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    /**
     * 盒子序号信息
     */
    @ApiModelProperty(value = "盒子序号信息")
    private Integer xlh;
    /**
     * 盒子ip地址
     */
    @ApiModelProperty(value = "盒子ip地址")
    private String sip;
    /**
     * 设备间隔记录id，记录最后的间隔数据
     */
    @ApiModelProperty(value = "设备间隔记录id，记录最后的间隔数据")
    private Integer slId;

}
