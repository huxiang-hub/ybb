package com.yb.panelapi.order.entity;

import com.yb.exeset.entity.ExesetFault;
import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/17.
 */
@Data
public class DownCacheEntity {
    //盒子的状态
    private String status;
    //设备停机的开始时间
    private Date startTime;
    //弹窗的标志位（是否弹窗）
    private Boolean flag;
    //执行表id
    private Integer es_id;
    //停机设置实体类
    private ExesetFault exesetFault;
}
