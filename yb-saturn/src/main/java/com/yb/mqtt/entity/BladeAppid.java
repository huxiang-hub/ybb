package com.yb.mqtt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 租户连接app管理对应信息表实体类
 *
 * @author BladeX
 * @since 2021-04-12
 */
@Data
@ApiModel(value = "BladeAppid对象", description = "租户连接app管理对应信息表")
@TableName("blade_appid")
public class BladeAppid implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * app类型：数据字典默认1海王星
     */
    @ApiModelProperty(value = "app类型：数据字典默认1海王星")
    private Integer appType;
    /**
     * app名称
     */
    @ApiModelProperty(value = "app名称")
    private String appName;
    /**
     * appid
     */
    @ApiModelProperty(value = "appid")
    private String appId;
    /**
     * 租户秘钥
     */
    @ApiModelProperty(value = "租户秘钥")
    private String tenKey;
    /**
     * 数据加密秘钥
     */
    @ApiModelProperty(value = "数据加密秘钥")
    private String tenDeckey;

}
