package com.yb.execute.vo;

import lombok.Data;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *停机时间段
 */
@Data
public class equipmenttimingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //    itemStyle: { normal: { color: colors[0] } },
//    name: '正常',
//    value: [0, '2009/6/13 2:00', '2009/6/21 12:00']
    private ItemStyle itemStyle;
    private String name;//设备
    private List value;//时间


}

