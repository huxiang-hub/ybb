package com.yb.auth.utils;

import lombok.Data;

/**

 Title: XjxccException.java

 Description: 自定义异常

 Copyright: Copyright (c) 2018

 @email lgqxjxcc@163.com

 @author liguoqing

 @date 2018年10月21日

 @version 1.0
 */
@Data
public class XjxccException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public XjxccException(String msg) {
        super(msg);
        this.msg = msg;
    }
    public XjxccException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
    public XjxccException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public XjxccException(String msg, int code, Throwable e) {
        super(msg, e);
        this.code = code;
    }
}
