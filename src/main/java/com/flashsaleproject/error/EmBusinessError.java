package com.flashsaleproject.error;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public enum EmBusinessError implements CommonError {
    // 00001 is the general error type for parameter related error
    PARAMETER_VALIDATION_ERROR(10001, "Invalid parameter"),
    UNKNOWN_ERROR(10002, "Unknown error"),
    // 1xxxx is the general error type for user related error
    USER_NOT_EXIST(20001, "User does not exist"),
    USER_LOGIN_FAIL(20002, "User telphone or password is not correct"),
    ;

    private int errCode;
    private String errMsg;

    EmBusinessError(int errorCode, String errMsg) {
        this.errCode = errorCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
