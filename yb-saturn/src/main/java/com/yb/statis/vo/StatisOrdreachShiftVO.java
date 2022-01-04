package com.yb.statis.vo;

import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import lombok.Data;

import java.util.List;

@Data
public class StatisOrdreachShiftVO {

    //private WorkbatchOrdlinkVO workbatchOrdlinkVO;
    /* 休息时间点 */
    private List<PointType> restPoints;
    /* 工作时间点 */
    private List<PointType> workPoints;

}
