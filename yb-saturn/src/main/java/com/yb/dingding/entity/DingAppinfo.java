package com.yb.dingding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_ding_appinfo")
@ApiModel(value = "钉钉应用接入管理表_yb_ding_appinfo")
public class DingAppinfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "app应用类型：1企业内部开发、2第三方企业应用、3第三方个人应用、4移动接入应用")
    private Integer apType;
    @ApiModelProperty(value = "程序模式：1小程序、2H5微应用、3机器人")
    private Integer apMode;
    @ApiModelProperty(value = "应用名称")
    private String apName;
    @ApiModelProperty(value = "应用凭证")
    private String agentid;
    @ApiModelProperty(value = "应用秘钥")
    private String appKey;
    @ApiModelProperty(value = "应用加密串")
    private String appSecret;
    @ApiModelProperty(value = "三方应用凭证")
    private String suiteId;
    @ApiModelProperty(value = "第三方应用id")
    private String appId;
    @ApiModelProperty(value = "应用秘钥")
    private String suiteKey;
    @ApiModelProperty(value = "应用加密串")
    private String suiteSecret;
    @ApiModelProperty(value = "应用首页地址")
    private String apUrl;
    @ApiModelProperty(value = "url地址二级域名")
    private String apDomain;
    @ApiModelProperty(value = "唯一标识编号：设定为8位字符串")
    private String apUnique;
    @ApiModelProperty(value = "状态：0停用1启用2作废")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "修改时间")
    private Date updateAt;

}
