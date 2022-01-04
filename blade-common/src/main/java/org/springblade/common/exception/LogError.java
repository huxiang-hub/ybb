package org.springblade.common.exception;


import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/25 11:22
 */
@Data
public class LogError  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String stackTrace;
    private String exceptionName;
    private String message;
    private String fileName;
    private String requestUri;
    private Integer lineNumber;
    private String method;
    private String methodClass;
    private String methodName;
    private String params;

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LogError)) {
            return false;
        } else {
            LogError other = (LogError)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label73: {
                    Object this$lineNumber = this.getLineNumber();
                    Object other$lineNumber = other.getLineNumber();
                    if (this$lineNumber == null) {
                        if (other$lineNumber == null) {
                            break label73;
                        }
                    } else if (this$lineNumber.equals(other$lineNumber)) {
                        break label73;
                    }

                    return false;
                }

                Object this$stackTrace = this.getStackTrace();
                Object other$stackTrace = other.getStackTrace();
                if (this$stackTrace == null) {
                    if (other$stackTrace != null) {
                        return false;
                    }
                } else if (!this$stackTrace.equals(other$stackTrace)) {
                    return false;
                }

                label59: {
                    Object this$exceptionName = this.getExceptionName();
                    Object other$exceptionName = other.getExceptionName();
                    if (this$exceptionName == null) {
                        if (other$exceptionName == null) {
                            break label59;
                        }
                    } else if (this$exceptionName.equals(other$exceptionName)) {
                        break label59;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$fileName = this.getFileName();
                Object other$fileName = other.getFileName();
                if (this$fileName == null) {
                    if (other$fileName != null) {
                        return false;
                    }
                } else if (!this$fileName.equals(other$fileName)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LogError;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        Object $lineNumber = this.getLineNumber();
        result = result * 59 + ($lineNumber == null ? 43 : $lineNumber.hashCode());
        Object $stackTrace = this.getStackTrace();
        result = result * 59 + ($stackTrace == null ? 43 : $stackTrace.hashCode());
        Object $exceptionName = this.getExceptionName();
        result = result * 59 + ($exceptionName == null ? 43 : $exceptionName.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $fileName = this.getFileName();
        result = result * 59 + ($fileName == null ? 43 : $fileName.hashCode());
        return result;
    }
}
