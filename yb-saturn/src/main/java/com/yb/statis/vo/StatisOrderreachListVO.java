package com.yb.statis.vo;

import com.yb.statis.entity.StatisOrdreach;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/25 9:50
 */
@Data
public class StatisOrderreachListVO extends StatisOrdreach {

    private Long sumRelyCount;

    private Long sumPlanNUm;


    private BigDecimal rate;
}
