package com.yb.common.constant;

/**
 * @author :   my
 * date:     2020-07-25
 * description: 排产单按钮枚举
 */
public class DictEnum {

    //图片类型
    public enum IMGTYPE {
        //首检图片
        QCF("IMG-QCF", "首检图片"),
        //巡检图片
        QCP("IMG-QCP", "巡检图片"),
        //头像图片
        HEAD("IMG-HEAD", "头像图片");

        IMGTYPE(String code, String vale) {
            this.code = code;
            this.vale = vale;
        }

        String code;
        String vale;
        public String getCode() {
            return code;
        }
        public String getVale() {
            return vale;
        }
    }
}

