package com.flashsaleproject.service;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.service.model.UserModel;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public interface UserService {

    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
}
