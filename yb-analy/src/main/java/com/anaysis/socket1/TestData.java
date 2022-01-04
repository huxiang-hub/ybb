package com.anaysis.socket1;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/23 9:14
 */
@Data
public class TestData {

    private String da;
    private Integer ti;
    private String ts;
    private dl dl;

    public static class dl {
        private Integer mbs;
        private Integer epc;
        private Integer mc;
        private Integer speed;
        private Integer index;
        private Integer uuid;

        public Integer getMbs() {
            return mbs;
        }

        public void setMbs(Integer mbs) {
            this.mbs = mbs;
        }

        public Integer getEpc() {
            return epc;
        }

        public void setEpc(Integer epc) {
            this.epc = epc;
        }

        public Integer getMc() {
            return mc;
        }

        public void setMc(Integer mc) {
            this.mc = mc;
        }

        public Integer getSpeed() {
            return speed;
        }

        public void setSpeed(Integer speed) {
            this.speed = speed;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getUuid() {
            return uuid;
        }

        public void setUuid(Integer uuid) {
            this.uuid = uuid;
        }

        @Override
        public String toString() {
            return "dl{" +
                    "mbs=" + mbs +
                    ", epc=" + epc +
                    ", mc=" + mc +
                    ", speed=" + speed +
                    ", index=" + index +
                    ", uuid=" + uuid +
                    '}';
        }
    }


//    public static void main(String[] args) {
//        TestData testData = new TestData();
//        testData.setDa("1");
//        testData.setTi(11);
//        TestData.dl dl = new dl();
//        dl.setEpc(1);
//        testData.setDl(dl);
//        String str = JSONObject.toJSONString(testData);
//        TestData testData1 = JSONObject.parseObject(str, TestData.class);
//        System.out.println(testData1);
//        System.out.println(str);
//    }
}
