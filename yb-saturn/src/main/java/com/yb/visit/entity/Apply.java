package com.yb.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel(value = "yb_visit_apply")
@TableName(value = "yb_visit_apply")
public class Apply implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "姓名")
    private String vaName;
    @ApiModelProperty(value = "身份证号")
    private String vaIdcard;
    @ApiModelProperty(value = "手机号")
    private String vaPhone;
    @ApiModelProperty(value = "健康码")
    private String healthCode;
    @ApiModelProperty(value = "单位名称（可选）")
    private String compName;
    @ApiModelProperty(value = "来访事由类型1、业务2、个人3、运输货物")
    private Integer vaType;
    @ApiModelProperty(value = "预约访问日期")
    private String vaDate;
    @ApiModelProperty(value = "时间范围9~10")
    private String vaTime;
    @ApiModelProperty(value = "车牌号码")
    private String carNum;
    @ApiModelProperty(value = "来自地址")
    private String fromAddr;
    @ApiModelProperty(value = "交通方式1、私家车2、货车、3、公共交通、4网约车/出租车\\5、火车")
    private Integer traffic;
    @ApiModelProperty(value = "乘车类型1、乘客2、司机")
    private Integer rideType;
    @ApiModelProperty(value = "拜访人")
    private String lookMan;
    @ApiModelProperty(value = "拜访人电话")
    private String lookPhone;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "管理人：姓名")
    private String manager;
    @ApiModelProperty(value = "管理人钉钉id信息")
    private String mgDdid;
    @ApiModelProperty(value = "状态0待审核1通过2不通过")
    private Integer status;
    @ApiModelProperty(value = "审核意见")
    private String examine;
    @ApiModelProperty(value = "审核时间")
    private Date exTime;
    @ApiModelProperty(value = "体温信息")
    private Double temperature;
    @ApiModelProperty(value = "门卫姓名")
    private String guard;
    @ApiModelProperty(value = "钉钉id信息")
    private String ddId;
    @ApiModelProperty(value = "测量输入体温时间")
    private Date tpTime;

}
