package org.springblade.common.exception;

/**
 * Author:   my
 * Date:    2020/6/8
 * Description: 公共异常处理类
 */
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String desc;
    private int code = 500;
    private Object data;

    public CommonException(String desc) {
        this.desc = desc;
    }

    public CommonException(int code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    public CommonException(int code, String desc, Object data) {
        this.desc = desc;
        this.code = code;
        this.data = data;
    }

    public CommonException(int code, String desc, Throwable e) {
        super(desc, e);
        this.desc = desc;
        this.code = code;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getdesc() {
        return desc;
    }

    public void setdesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CommonException{" +
                "desc='" + desc + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
