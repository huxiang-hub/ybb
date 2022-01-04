package com.yb.supervise.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备当前状态表boxinfo-视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_supervise_boxinfo")
@ApiModel(value = "SuperviseBoxinfo对象", description = "设备当前状态表boxinfo-视图")
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
    /**
     * 状态变化的开始时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "状态变化的开始时间")
    private Date startTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
   /* @DateTimeFormat(pattern="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")*/
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

    private String usIds;

    private Integer clearNum;

    private Date clearTime;

    /**
     * 是否接单
     */
    @ApiModelProperty(value = "是否接单")
    private Integer blnAccept;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String wbNo;

}
