package com.flashsaleproject.error;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public interface CommonError {
    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);


}
