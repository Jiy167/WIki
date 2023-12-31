package com.jiyuan.wiki.exception;

public class BusinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BusinessException (BusinessExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    /**
     * Do not write stack information to improve performance
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
