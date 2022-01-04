package com.anaysis.executSupervise.vo;

import com.anaysis.executSupervise.entity.WorkbatchShift;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 排产班次执行表_yb_workbatch_shiftinfo（日志表）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbatchShiftinfoVO对象", description = "排产班次执行表_yb_workbatch_shiftinfo（日志表）")
public class WorkbatchShiftVO extends WorkbatchShift {
    private static final long serialVersionUID = 1L;
    private Integer planNum;
    private Integer difficutlNum;
    private Date classStartTime;
    private Date classEndTime;
    private Integer mealStay;
}
