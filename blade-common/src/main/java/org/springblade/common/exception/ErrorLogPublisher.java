
package org.springblade.common.exception;


import org.springblade.common.tool.CommonUtil;
import org.springblade.core.tool.utils.Exceptions;
import org.springblade.core.tool.utils.Func;

public class ErrorLogPublisher {
    public ErrorLogPublisher() {
    }

    public static LogError publishEvent(Throwable error, String requestUri) {
        LogError logError = new LogError();
        logError.setRequestUri(requestUri);
        if (error != null) {
            //logError.setStackTrace(Exceptions.getStackTraceAsString(error));
            logError.setExceptionName(error.getClass().getName());
            logError.setMessage(error.getMessage());
            StackTraceElement[] elements = error.getStackTrace();
            if (Func.isNotEmpty(elements)) {
                StackTraceElement element = elements[0];
                logError.setMethodName(element.getMethodName());
                logError.setMethodClass(element.getClassName());
                logError.setFileName(element.getFileName());
                logError.setLineNumber(element.getLineNumber());
            }
        }
        CommonUtil.send(logError.toString());
        return logError;
    }
}
