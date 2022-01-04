package com.sso.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "yb_base_factory")
@ApiModel(value = "厂区表_yb_base_factory")
public class BaseFactory implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "公司名称")
    private String fname;
    @ApiModelProperty(value = "'公司地址'")
    private String address;
    @ApiModelProperty(value = "'租户编码-tenant_id对应'")
    private String tenantId;
    @ApiModelProperty(value = "'联系电话'")
    private String phone;
    @ApiModelProperty(value = "'备注'")
    private String remarks;
    @ApiModelProperty(value = "'是否停用1启用0停用'")
    private Integer isUsed;
    @ApiModelProperty(value = "凌晨06:30__设定厂区设备的定时更新时间 默认")
    private String regularTime;
    @ApiModelProperty(value = "钉钉的公司唯一标识")
    private String corpId;
}
