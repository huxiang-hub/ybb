package com.sso.panelapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备_yb_mach_mainfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_mainfo")
@ApiModel(value = "MachineMainfo对象", description = "设备_yb_mach_mainfo")
public class MachineMainfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备型号id
     */
    @ApiModelProperty(value = "设备型号id")
    private Integer mtId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String mno;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "设备排序")
    private Integer sort;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备分类")
    private String maType;

    /**
     * 车间Id
     */
    @ApiModelProperty(value = "车间Id")
    private Integer dpId;
    /**
     * 工序分类---指定主要工序类型
     */
    @ApiModelProperty(value = "指定主要工序类型")
    private Integer proId;

    /****
     * 输入单位：maUnit数据字典
     */
    @ApiModelProperty(value = "输入单位：maUnit数据字典")
    private String inUnit;
    /****
     * 匹配输出单位：maUnit数据字典
     */
    @ApiModelProperty(value = "匹配输出单位：maUnit数据字典")
    private String outUnit;
    /****
     * 辅助单位：maUnit数据字典
     */
    @ApiModelProperty(value = "辅助单位：maUnit数据字典")
    private String auxiliaryUnit;
    /****
     * 设备名称ERP主键
     */
    @ApiModelProperty(value = "设备名称ERP主键")
    private String erpId;

    /**
     * 是否为工序接单操作0按设备接单1是按工序接单；
     */

    @ApiModelProperty(value = "是否为工序接单操作0按设备接单1是按工序接单；")
    private Integer isRecepro;




}
