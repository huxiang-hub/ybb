package org.springblade.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.utils.UrlUtil;
import org.springblade.core.tool.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;


/**
 * Author:   my
 * Date:    2020/6/8
 * Description: CommonExceptionHandler
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(CommonException.class)
    public R handleCommonException(CommonException e) {
        return R.error(e.getCode(), e.getdesc());
    }

//    /**
//     * 处理所有不可知的异常
//     *
//     * @param e 异常
//     * @return 响应
//     */
//    @ExceptionHandler(Exception.class)
//    public R handleException(Exception e) {
//        log.error("发生异常", e);
//        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常");
//    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e 接口参数校验异常
     * @return 响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常", e);
        String[] str = e.getBindingResult()
                .getAllErrors().get(0).getCodes()[1].split("\\.");

        StringBuffer msg = new StringBuffer(str[1] + ":");
        msg.append(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return R.error(HttpStatus.BAD_REQUEST.value(), msg.toString());
    }

    /**
     * 处理所有接口数据绑定异常
     *
     * @param e 接口参数绑定异常
     * @return 响应
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public R handleHttpMessageNotReadableException(Exception e) {
        String str = e.toString();
        String msg = str.substring(str.indexOf(":"));
        log.warn("参数绑定异常[msg:{}]", msg);
        return R.error(HttpStatus.BAD_REQUEST.value(), msg.toString());
    }


    /**
     * ConnectException异常
     */
    @ExceptionHandler(ConnectException.class)
    public R handleConnectException(ConnectException e) {
        log.error("网络链接异常", e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常，请稍后重试！");
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleError(Throwable e) {
        log.error("服务器异常", e);
        ErrorLogPublisher.publishEvent(e, UrlUtil.getPath(WebUtil.getRequest().getRequestURI()));
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常！" );
    }
}
