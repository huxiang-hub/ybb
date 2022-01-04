package com.yb.workbatch.vo;

import com.yb.machine.vo.MachineMainfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkbatchOrdlinkExcelVO implements Serializable {

   private static final long serialVersionUID = 1L;
   private Integer prId;//工序id
   private String prName;//工序名称
   private String sdDate;//排产日期
   private String dpName;//生产车间
   private List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList;//排产信息
   List<MachineMainfoVO> machineMainfoVOList;//设备信息

}
