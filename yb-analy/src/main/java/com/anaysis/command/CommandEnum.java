package com.anaysis.command;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/21 16:07
 */
public class CommandEnum {

    public enum Order {
        DI1("0110000100010200016641"),
        DI2("0110000100010200022640"),
        DI3("011000010001020003E780"),
        DI4("011000010001020004A642"),
        UUID("01040000000CF00F");

        Order(String desc) {
            this.desc = desc;
        }

        String desc;

        public String getDesc() {
            return desc;
        }
    }
}
