package com.yb.execute.vo;


import lombok.Data;

@Data
public class ParpreVo {
    /**
     * 保养时间
     */
    private Integer MaintainMin;
    /**
     * 保养次数
     */
    private Integer MaintainTime;
    /**
     * 换膜时间
     */
    private Integer ExchangeMin;
    /**
     * 换膜次数
     */
    private Integer ExchangeTime;
}

