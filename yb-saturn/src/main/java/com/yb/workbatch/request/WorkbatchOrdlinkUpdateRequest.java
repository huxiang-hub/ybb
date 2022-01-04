package com.yb.workbatch.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 排产修改请求类
 * @Author my
 * @Date Created in 2020/7/24 16:27
 */
@ApiModel("排产修改请求类")
@Data
public class WorkbatchOrdlinkUpdateRequest {

    @ApiModelProperty(value = "工单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "作业批次ID")
    private Integer wbId;

    @ApiModelProperty(value = "作业批次编号")
    private String wbNo;

    @ApiModelProperty(value = "流水号-设备id")
    private Integer maId;

    @ApiModelProperty(value = "ERP工单（待定）")
    private Integer erpWbId;

    @ApiModelProperty(value = "订单编号")
    private String odNo;

    @ApiModelProperty(value = "部件id")
    private Integer ptId;

    @ApiModelProperty(value = "部件编号")
    private String ptNo;

    @ApiModelProperty(value = "部件名称")
    private String partName;

    @ApiModelProperty(value = "工序ID")
    private Integer prId;

    @ApiModelProperty(value = "工序名称")
    private String prName;

    @ApiModelProperty(value = "工艺说明")
    private String prDes;

    @ApiModelProperty(value = "工序排序")
    private Integer sort;

    @ApiModelProperty(value = "产品编号")
    private String pdCode;

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "产品图片")
    private String pdImageurl;

    @ApiModelProperty(value = "产品类型")
    private String pdType;

    @ApiModelProperty(value = "计划数量")
    private Integer planNum;

    @ApiModelProperty(value = "应交数")
    private Integer planNumber;

    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;

    @ApiModelProperty(value = "未完成数量")
    private Integer incompleteNum;

    @ApiModelProperty(value = "冗余数")
    private Integer extraNum;

    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃）6驳回 -1待排产ERP接入")
    private String status;

    @ApiModelProperty(value = "0:待接单，1：生产中，2：生产完成")
    private Integer runStatus;

    @ApiModelProperty(value = "计划开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "实际开始时间")
    private Date actuallyStarttime;

    @ApiModelProperty(value = "截止日期")
    private Date closeTime;

    @ApiModelProperty(value = "计划用时")
    private Integer planTime;

    @ApiModelProperty(value = "废品")
    private Integer waste;

    @ApiModelProperty(value = "废品率")
    private Integer nbwaste;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "排产排序")
    private Integer sdSort;

    @ApiModelProperty(value = "排产日期")
    private String sdDate;

    @ApiModelProperty(value = "创建时间")
    private Date updateAt;

    @ApiModelProperty(value = "计划出勤人数")
    private Integer dutyNum;

    @ApiModelProperty(value = "班次id")
    private Integer wsId;

    @ApiModelProperty(value = "车间id")
    private Integer dpId;

    @ApiModelProperty(value = "用户id")
    private Integer usId;

    @ApiModelProperty(value = "订货厂家名称")
    private String cmName;

    @ApiModelProperty(value = "UUID")
    private String erpId;

    @ApiModelProperty(value = "最终交货时间")
    private String finalTime;

    @ApiModelProperty(value = "上到工序名称")
    private String upPorcess;

    @ApiModelProperty(value = "下到工序名称")
    private String downPorcess;

    @ApiModelProperty(value = "备注2")
    private String secondRemark;

    @ApiModelProperty(value = "原料名称")
    private String materialName;

    @ApiModelProperty(value = "辅料名称")
    private String ingredientName;

    @ApiModelProperty(value = "辅料数量")
    private String ingredientNum;

    @ApiModelProperty(value = "请求入库时间")
    private String ingredientTime;

    @ApiModelProperty(value = "上工序排产顺序 07-18-01")
    private String upprocessSort;

    @ApiModelProperty(value = "CTP号")
    private String ctpNum;

    @ApiModelProperty(value = "部件尺寸 1900*1200(装订是 190*123*231)")
    private String ptSize;

    @ApiModelProperty(value = "工艺路线")
    private String prRoute;

    @ApiModelProperty(value = "预排序")
    private String preorder;

    @ApiModelProperty(value = "订单类型")
    private String odType;
}
