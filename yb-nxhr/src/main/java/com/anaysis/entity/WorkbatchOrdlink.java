package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @author lzb
 * @date 2020-11-30
 */
@Data
@TableName("yb_workbatch_ordlink")
@Accessors(chain = true)
@ApiModel(value = "生产排产表yb_workbatch_ordlinkWorkbatchOrdlink实体")
public class WorkbatchOrdlink implements Serializable {


    @ApiModelProperty(value = "工单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "作业批次ID")
    @TableField("wb_id")
    private Integer wbId;


    @ApiModelProperty(value = "部件Id")
    @TableField("pt_id")
    private Integer ptId;


    @ApiModelProperty(value = "部件编号")
    @TableField("pt_no")
    private String ptNo;


    @ApiModelProperty(value = "部件名称")
    @TableField("part_name")
    private String partName;


    @ApiModelProperty(value = "ERP工单（待定）")
    @TableField("erp_wb_id")
    private String erpWbId;


    @ApiModelProperty(value = "流水号-设备id")
    @TableField("ma_id")
    private Integer maId;


    @ApiModelProperty(value = "订单编号")
    @TableField("od_no")
    private String odNo;


    @ApiModelProperty(value = "工序ID")
    @TableField("pr_id")
    private Integer prId;


    @ApiModelProperty(value = "工序名称")
    @TableField("pr_name")
    private String prName;


    @ApiModelProperty(value = "工艺说明")
    @TableField("pr_des")
    private String prDes;


    @ApiModelProperty(value = "工序排序")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "产品编号")
    @TableField("pd_code")
    private String pdCode;


    @ApiModelProperty(value = "产品名称")
    @TableField("pd_name")
    private String pdName;


    @ApiModelProperty(value = "产品图片")
    @TableField("pd_imageurl")
    private String pdImageurl;


    @ApiModelProperty(value = "产品类型")
    @TableField("pd_type")
    private String pdType;


    @ApiModelProperty(value = "计划数量")
    @TableField("plan_num")
    private Integer planNum;


    @ApiModelProperty(value = "应交数量（计划数量-冗余数量）")
    @TableField("plan_number")
    private Integer planNumber;


    @ApiModelProperty(value = "完成数量")
    @TableField("complete_num")
    private Integer completeNum;


    @ApiModelProperty(value = "未完成数量")
    @TableField("incomplete_num")
    private Integer incompleteNum;


    @ApiModelProperty(value = "冗余数")
    @TableField("extra_num")
    private Integer extraNum;


    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    @ApiModelProperty(value = "实际开始时间")
    @TableField("actually_starttime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actuallyStarttime;


    @ApiModelProperty(value = "截止日期(预交货期)")
    @TableField("close_time")
    private String closeTime;


    @ApiModelProperty(value = "计划开始时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;


    @ApiModelProperty(value = "计划用时")
    @TableField("plan_time")
    private Integer planTime;


    @ApiModelProperty(value = "废品")
    @TableField("waste")
    private Integer waste;


    @ApiModelProperty(value = "废品率-100百分数")
    @TableField("nbwaste")
    private Integer nbwaste;


    @ApiModelProperty(value = "备注1")
    @TableField("remarks")
    private String remarks;


    @ApiModelProperty(value = "0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）")
    @TableField("run_status")
    private Integer runStatus;


    @ApiModelProperty(value = "更新时间")
    @TableField("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;


    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完10强制结束")
    @TableField("status")
    private String status;


    @ApiModelProperty(value = "排产日期")
    @TableField("sd_date")
    private String sdDate;


    @ApiModelProperty(value = "班次ID")
    @TableField("ws_id")
    private Integer wsId;


    @ApiModelProperty(value = "应出勤人数")
    @TableField("duty_num")
    private Integer dutyNum;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "排产排序")
    @TableField("sd_sort")
    private String sdSort;


    @ApiModelProperty(value = "车间id")
    @TableField("dp_id")
    private Integer dpId;

    @ApiModelProperty(value = "产品id")
    @TableField("pd_id")
    private Integer pdId;

    @ApiModelProperty(value = "用户id")
    @TableField("us_id")
    private Integer usId;


    @ApiModelProperty(value = "订货厂家-客户名称")
    @TableField("cm_name")
    private String cmName;


    @ApiModelProperty(value = "erpUUID")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "最终截止日期(最终交期)")
    @TableField("final_time")
    private String finalTime;


    @ApiModelProperty(value = "用料名称")
    @TableField("material_name")
    private String materialName;


    @ApiModelProperty(value = "上工序名称")
    @TableField("up_porcess")
    private String upPorcess;


    @ApiModelProperty(value = "下工序名称")
    @TableField("down_porcess")
    private String downPorcess;


    @ApiModelProperty(value = "CTP号")
    @TableField("ctp_num")
    private String ctpNum;


    @ApiModelProperty(value = "备注2")
    @TableField("second_remark")
    private String secondRemark;


    @ApiModelProperty(value = "辅料名称")
    @TableField("ingredient_name")
    private String ingredientName;


    @ApiModelProperty(value = "辅料数量")
    @TableField("ingredient_num")
    private Integer ingredientNum;


    @ApiModelProperty(value = "辅料入库时间")
    @TableField("ingredient_time")
    private String ingredientTime;


    @ApiModelProperty(value = "上工序顺序")
    @TableField("upprocess_sort")
    private Integer upprocessSort;


    @ApiModelProperty(value = "部件尺寸 1900*1200(装订是 190*123*231)")
    @TableField("pt_size")
    private String ptSize;


    @ApiModelProperty(value = "工艺路线")
    @TableField("pr_route")
    private String prRoute;


    @ApiModelProperty(value = "批次编号")
    @TableField("wb_no")
    private String wbNo;


    @ApiModelProperty(value = "预排序")
    @TableField("preorder")
    private String preorder;


    @ApiModelProperty(value = "订单类型")
    @TableField("od_type")
    private String odType;


    @ApiModelProperty(value = "产品尺寸")
    @TableField("pd_size")
    private String pdSize;


    @ApiModelProperty(value = "上机尺寸")
    @TableField("operate_size")
    private String operateSize;


    @ApiModelProperty(value = "跟进信息1")
    @TableField("follow_up")
    private String followUp;


    @ApiModelProperty(value = "跟进信息2")
    @TableField("follow_upto")
    private String followUpto;


    @ApiModelProperty(value = "共单生产（一样订单）1是0否")
    @TableField("mutual")
    private Integer mutual;


    @ApiModelProperty(value = "补数")
    @TableField("patch_num")
    private Integer patchNum;


    @ApiModelProperty(value = "修订状态")
    @TableField("revision_status")
    private String revisionStatus;


    @ApiModelProperty(value = "客户产品批次")
    @TableField("product_batch")
    private String productBatch;


    @ApiModelProperty(value = "大货交期")
    @TableField("delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;


    @ApiModelProperty(value = "卷筒库存数量单位定义")
    @TableField("reel_stock")
    private Integer reelStock;


    @ApiModelProperty(value = "上工序生产状态")
    @TableField("upprocess_status")
    private String upprocessStatus;


    @ApiModelProperty(value = "上工序生产时间2020-08-19 12:00:15 生产开始时间")
    @TableField("upprocess_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime upprocessTime;


    @ApiModelProperty(value = "ERP的主施工单信息")
    @TableField("erp_wbid")
    private String erpWbid;

    @ApiModelProperty(value = "设备id")
    @TableField(exist = false)
    private String erpMaId;

    @ApiModelProperty(value = "产品长")
    @TableField(exist = false)
    private Double length;

    @ApiModelProperty(value = "产品宽")
    @TableField(exist = false)
    private Double width;

    @ApiModelProperty(value = "产品高")
    @TableField(exist = false)
    private Double height;

    @ApiModelProperty(value = "erp产品id")
    @TableField(exist = false)
    private String erpPdId;

    @ApiModelProperty(value = "erp的主是工单信息")
    @TableField("arrange_num")
    private Integer arrangeNum;
}
