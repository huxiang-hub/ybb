package org.springblade.common.resubmit;
/**
 * @Description: 防重复提交枚举
 * @Author my
 * @Date Created in 2020/6/10
 */
public enum ReSubmitStatus {

    /**
     * 重复提交
     */
    RE_SUBMIT(511, "不允许重复提交，请稍后再试");


    private final int code;
    private final String desc;

    ReSubmitStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int value() {
        return this.code;
    }

    public String getReasonPhrase() {
        return this.desc;
    }
}
