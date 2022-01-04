package com.yb.statis.vo;

import com.yb.statis.entity.StatisDayreach;
import lombok.Data;

@Data
public class StatisDayreachVO extends StatisDayreach {

    /*导出图片*/
    private byte[] image;
    /*设备类型*/
    private String maTypeValue;
    /*车间id*/
    private Integer dpId;
}
