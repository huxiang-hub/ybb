package com.yb.workbatch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yb.mater.vo.MaterMtinfoVO;
import com.yb.workbatch.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 生产排产表yb_workbatch_ordlink视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbatchOrdlinkVO对象", description = "生产排产表yb_workbatch_ordlink")
public class WorkbatchOrdlinkVO extends WorkbatchOrdlink {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id集合")
    private List<Integer> maIdList;
    @ApiModelProperty(value = "班次id集合")
    private List<Integer> wsIdList;
    @ApiModelProperty(value = "排产开始日期")
    private String sdDateStartDate;
    @ApiModelProperty(value = "排产结束日期")
    private String sdDateEndDate;
    //停机时间
    private Double faltTime;

    //生产计数
    private Integer proNum;

    //生产计时
    private Double proTime;

    //生产计时
    private Integer wasteNum;

    //登录用户id
    private Integer usId;

    private Integer esId;

    private String usIds;

    private String indentor;

    private String execelOrderLinkTime;

    private String machineName;

    private String machineNo;

    private String excelCloseTime;

    private String ckName;

    private String ptName;//部件名称

    private Integer wasteTotal;

    private String excelShouldTime;

    private String exeStatus;//工单生产状态

    private String release;//放数

    /*批次编号*/
    private String batchNo;
    private String odName;  //订单名称

    private Integer icon;  //批次编号
    /*排产oee*/
    private WorkbatchOrdoee workbatchOrdoee;
    /*排产oee保养时间区间*/
    private List<WorkbatchordoeeMaintain> workbatchordoeeMaintainList;
    /*排产oee吃饭时间区间*/
    private List<WorkbatchordoeeMeal> workbatchordoeeMealList;
    /*排产oee换膜时间区间*/
    private List<WorkbatchordoeeMould> workbatchordoeeMouldList;

    /**
     * 具体的类别
     */
    private String asType;
    /**
     * 具体的那个流程类别
     */
    private String awType;
    /**
     * 车间名称
     */
    private String dpName;
    /**
     * 换膜时间/(分)
     */
    private Integer mouldStay;
    /**
     * 设备对应工序的转速(/h)
     */
    private Integer speed;
    /**
     * 用餐时间/(分)
     */
    private String mealStay;
    /**
     * 条形码
     */
    private byte[] img;

    /**
     * 生产执行上报信息id
     */
    private Integer bid;
    //班次Id
    private Integer wsId;
    //实际的班次开始时间
    private Date sfStartTime;
    //实际的班次结束时间
    private Date sfEndTime;
    /*产品编号*/
    private String pdNo;
    /*班次表的排产日期*/
    private String wsSdDate;
    /*设备计数*/
    private Integer machineNumber;
    /*操作人姓名*/
    private List<String> userList;


    /**
     * 查詢用，截止時間的范围
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTimeEnd;


    //排产单id
    private Integer sdId;

    /**
     * 实时表id
     */
    private Integer infoId;
    //------------------------------扩展表
    private String cutNo;

    private String versionClass;

    private Integer colorNum;

    private Integer maType;//设备类型

    private String paintColour;

    private String ctpNo;

    private Date ctpTime;

    private Date materialTime;

    //value = "是否校板0不校板1校板
    private Integer isCompare;
    //value = "看色描述
    private String colorDesc;
    //value = "同印工艺
    private String craftSame;
    //value = "推送日期
    private Date pushDate;
    //不装水油底纸1是0否
    private Integer basePaper;
    //同系列生产1是0否
    private Integer sameSeries;
    //含同印生产否1是0否
    private Integer includePrint;
    //印刷机台名称
    private String printStation;

    //------------------------------扩展表
    private Map<String, Object> map;

    //计划完成时间
    private Integer planTotalTime;
    //------------------------排序
    private String orderBy;
    //    ------------产品规格
    private String pdSize;
    //总数量
    private Integer allPlanNum;

    private List<MaterMtinfoVO> materMtinfo;
    //    ------------入库状态
    private Integer warehouseStatus;

    //    ------------辅料入库实际时间
    private Date inTime;

    //    ------------主料入库实际时间
    private Date mainInTime;

    //    ------------主料请求入库时间
    private String mainIngredientTime;

    //物料差额
    private Integer materialDifference;

    //已排产数
    private Integer sumPlanNum;
    private Integer sdCoutNum;//计划生产排产总数
    private Integer wsPlanNumber;//计划生产应交数
    private Integer shiftAllNum;//计划生产应交数

    //班次状态
    private Integer shiftStatus;

    private String wfStatus;//班次表中的status状态信息

    private Integer index;//序号

    private Integer wfId;

    /**
     * 班次完成数
     */
    private Integer finishNum;

    /**
     * 开始时间
     */
    private Date proBeginTime;

    /**
     * 结束时间
     */
    private Date proFinishTime;

    /**
     * 用于计算不返回
     */
    @JsonIgnore
    private Integer sumPlanNumber;

    /**
     * 用于判断类型 空为查全部 不返回前端
     */
    @JsonIgnore
    private Integer type;

    @JsonIgnore
    private List<Integer> sdIds;

    @ApiModelProperty(value = "是否自动接单  0否,1是")
    private Integer isAuto;

    @ApiModelProperty(value = "班次排产数")
    private Integer shiftNum;
    @ApiModelProperty(value = "是否锁定排产顺序 0不锁定 1锁定")
    private Integer wfsortIslock;

    @ApiModelProperty(value = "班次排产表排序")
    private String shiftSort;

    @ApiModelProperty(value = "未排完数量")
    private Integer noSchedulingNum;

    @ApiModelProperty(value = "托盘产品总数")
    private Integer trayTotalNum;
}
