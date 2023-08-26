package com.flashsaleproject.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/26/23
 */
@Data
public class ValidationResult {
    // check if there is any error
    private boolean hasErrors = false;

    // store the error message
    private Map<String, String> errorMsgMap = new HashMap<>();

    // public method to get the error message in the format of string
    public String getErrMsg() {
        return StringUtils.join(errorMsgMap.values().toArray(), ",");
    }

}
