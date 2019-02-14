package com.zhihao.miao.user.exception;

/**
 * @author 程天亮
 * @Created
 */
public class BizException extends RuntimeException {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public BizException() {}

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
