package com.yb.statis.vo;

import com.yb.statis.entity.StatisShiftreach;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisShiftreachVO extends StatisShiftreach {
    /*设备类型*/
    private Integer maType;
    /*印数套数*/
    private Integer printNum;
    /*ps版数-模数*/
    private Integer psNum;
    /*工单数*/
    private Integer ordNum;
    /*校板数量*/
    private Integer compareNum;
    /*导出图片*/
    private byte[] image;
}
