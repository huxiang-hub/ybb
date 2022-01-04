package com.yb.panelapi.order.entity;

import com.yb.exeset.entity.ExesetQuality;
import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/17.
 */
@Data
public class QualityCacheEntity {
    //生产计数
    private Integer number;
    //设备停机的开始时间
    private Date startTime;
    //弹窗的标志位（是否弹窗）
    private Boolean flag;
    //执行表id
    private Integer es_id;
    //质检设置实体类
    private ExesetQuality quality;
}
