package org.springblade.saturn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 印联盒总表
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_mixbox")
@ApiModel(value = "MachineMixbox对象", description = "印联盒（本租户的盒子），由总表分发出去")
public class MainMixbox implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 盒子编号
     */
    @ApiModelProperty(value = "盒子编号")
    private String uuid;
    /**
     * 硬件型号id
     */
    @ApiModelProperty(value = "硬件型号id")
    private Integer hdId;
    /**
     * 生产批次
     */
    @ApiModelProperty(value = "生产批次")
    private String batch;
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;
    /**
     * 是否激活
     */
    @ApiModelProperty(value = "是否激活")
    private Integer active;
    /**
     * 仓库状态
     */
    @ApiModelProperty(value = "仓库状态")
    private Integer depository;
    /**
     * mac地址
     */
    @ApiModelProperty(value = "mac地址")
    private String mac;
    /**
     * 创建时间 例如：2020-01-08
     */
    @ApiModelProperty(value = "创建时间 例如：2020-01-08")
    private String createAt;

    /**
     * 创建时间 例如：2020-01-08
     */
    @ApiModelProperty(value = "创建时间 例如：2020-01-08")
    private String tenantId;
}
