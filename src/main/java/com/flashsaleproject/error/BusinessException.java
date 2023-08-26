package com.flashsaleproject.error;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public class BusinessException extends Exception implements CommonError{
    private CommonError commonError;

    // Directly receive EmBusinessError
    // so that we can use the error code and error message defined in EmBusinessError
    // to construct the BusinessException
    public BusinessException(CommonError commonError) {
        super(); // call the constructor of the parent class
        this.commonError = commonError;
    }

    // receive EmBusinessError and errMsg as parameters
    // to construct the BusinessException
    public BusinessException(CommonError commonError, String errMsg) {
        super(); // call the constructor of the parent class
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg); // overwrite the errMsg in commonError
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
