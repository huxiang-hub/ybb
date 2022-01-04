package com.sso.panelapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.saturn.entity.MachineMainfo;

import java.io.Serializable;

/**
 * 设备_yb_mach_mainfo视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MachineMainfoVO对象", description = "设备_yb_mach_mainfo")
public class MachineMainfoVO extends MachineMainfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer maId;
    private String mno;
    private String maName;
    private Integer maStatus;


    /**
     * 后台给前端组件的字段 对应
     */
    private Integer value; // maId
    private String label;// maName

    /**
     * 部门信息
     */
    private String dpName;
    private Integer dpId;
     /* 设备品牌
     */
    private String  brand;
    /**
     * 设备规格
     */
    private String  specs;
    /**
     * 设备型号
     */
    private String  model;
    /**
     * 设备图片
     */
    private String  image;
    /**
     *速度
     */
    private Integer speed;//标准时速

    private Integer prId;//工序id
    /**
     * 工序分类名称
     */
    private String pyName;
    /**
     * 主要工序名称
     */
    private String prName;

    /**
     * 换膜时间(分)
     */
    @ApiModelProperty(value = "换膜时间(分)")
    private Integer prepareTime;
}
