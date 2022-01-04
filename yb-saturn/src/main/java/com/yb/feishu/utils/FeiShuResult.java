package com.yb.feishu.utils;

import lombok.Data;

import java.io.Serializable;

/**
 *service层返回对象列表封装
 * @param <T>
 */
@Data
public class FeiShuResult<T>  implements Serializable{

    private boolean success = false;

    private String code;

    private String msg;

    private T data;

    private FeiShuResult() {
    }

    public static <T> FeiShuResult<T> success(T result) {
        FeiShuResult<T> item = new FeiShuResult<T>();
        item.success = true;
        item.data = result;
        item.code = "0";
        item.msg = "success";
        return item;
    }

    public static <T> FeiShuResult<T> failure(String errorCode, String errorMessage) {
        FeiShuResult<T> item = new FeiShuResult<T>();
        item.success = false;
        item.code = errorCode;
        item.msg = errorMessage;
        return item;
    }

    public static <T> FeiShuResult<T> failure(String errorCode) {
        FeiShuResult<T> item = new FeiShuResult<T>();
        item.success = false;
        item.code = errorCode;
        item.msg = "failure";
        return item;
    }

}
