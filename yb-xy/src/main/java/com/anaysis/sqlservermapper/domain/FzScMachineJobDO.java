package com.anaysis.sqlservermapper.domain;

import lombok.Data;

/****
 * 工序实体对象，从方正的SQLserver数据
 */
@Data
public class FzScMachineJobDO {
    Integer id;//排单id信息
    String iMachineJobId;//设备工单id
    String iProductJobId;//产品工单id
    String resId;//设备编号id
    String machineName;//设备名称
    String idRes;//设备编号id
    String resmc;//设备名称
    String resType;//设备类型
    String idGzzx;//设备
    String cProductCode;//产品编号
    String cProductName;//产品名称
    String cPartName;//部件名称
    String cJobCode;//工单编号(作业编号)
    String cJobName;//工单名称
    String cJobSize;//尺寸大小
    String dPlanStart;//计划开始时间
    String dFactStart;//实际开始时间
    String dFactEnd;//实际结束时间
    Integer iStatus;//工单状态0未发布、1、已下单、2已开始
    String nStdAmount;//总数
    String nConsumeAmount;//放数量
    String nDeliverAmount;//应交数量
    String nCompleteAmount;//完成数量
    String dCreate;//创建时间
    String dRemark;//排产备注
    String nPlanTime;//计划时间
    String cName_Unit;//数量单位
    String nStdAmountS;//工单数量
    String nPWaste;//废品数量
    String nBWaste;//放量废品
    String nPAmount;//正品产量
    String nPlanTime1;//计划生产用时
    String nCir_Roller;
    String iXh_Plan;//计划编号
    String iTotalAmount;//总数
    String cPlanRemark;//下单备注信息
    String cFeedbackViewRemark;//机台反馈备注信息
    String cGzzxmc;//生产中心名称//
    String cGzzxlx;//生产中心id
    String cWasteDesc;//废品说明原因
}
