package com.anaysis.yLong;

import lombok.Data;

/**
 * @Description: yilong历史数据
 * @Author my
 */
@Data
public class YiLongHistoryData {

    /**
     * 设备类型
     */
    private String devType;

    /**
     * 设备组
     */
    private String devGroup;

    /**
     * 唯一Id
     */
    private String devID;

    /**
     * 上报时间
     */
    private String times;

    /**
     * 索引
     */
    private String index;

    private data data;

    @Data
    public class data {

        /**
         * type
         */
        private String type;
        /**
         * 预留
         */
        private String inc;

        /**
         * 收纸
         */
        private String outc;

        /**
         * 速度
         */
        private String speed;

        /**
         * "状态（0:停机，1：运行，2：空转）
         */
        private String state;

        /**
         * 运行时间
         */
        private String runt;

        /**
         * 空转时间
         */
        private String empty;

        /**
         * 设备本地时间
         */
        private String devTime;
    }
}
