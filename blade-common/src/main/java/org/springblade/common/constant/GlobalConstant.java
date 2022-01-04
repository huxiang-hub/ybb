package org.springblade.common.constant;

/**
 * @author by SUMMER
 * @date 2020/4/27.
 */
public class GlobalConstant {

    public enum ProType {

        //人事A 生产前B 生产中C 生产后D
        //上班A1 下班A2
        //接收订单B1 保养B2 换模B3
        //正式生产C1 停机C2 质检C3 暂停C4
        //结束生产D1 生产上报D2 入库D3
        PERSONNEL_STATUS("A", "人事状态"),
        ONWORK_EVENT("A1", "上班A1"),
        OFFWORK_EVENT("A2", "下班A2"),
        BEFOREPRO_STATUS("B", "生产前准备B"),
        ACCEPT_EVENT("B1", "接收订单B1"),
        MAINTAIN_EVENT("B2", "保养B2"),
        CHANGE_EVENT("B3", "换膜B3"),
        INPRO_STATUS("C", "生产中C"),
        PRODUCT_EVENT("C1", "正式生产C1"),
        DOWNTIME_EVENT("C2", "停机C2"),
        QUALITY_EVENT("C3", "质检C3"),
        PAUSE_EVENT("C4", "暂停状态C4"),
        AFTERPRO_STATUS("D", "生产结束后D"),
        FINISH_EVENT("D1", "结束生产D1"),
        REPORT_EVENT("D2", "生产上报D2"),
        PUTSTORE_EVENT("D3", "入库D3");

        private String type;
        private String value;

        ProType(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }
}
