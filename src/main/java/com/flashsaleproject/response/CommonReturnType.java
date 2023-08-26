package com.flashsaleproject.response;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public class CommonReturnType {
    private String status; // "success" or "fail"
    private Object data; // if status == "success", data is the return object in json format; if status == "fail", data is the error message

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }
    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
