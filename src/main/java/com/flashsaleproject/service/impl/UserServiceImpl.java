package com.flashsaleproject.service.impl;

import com.flashsaleproject.dao.UserDOMapper;
import com.flashsaleproject.dao.UserPasswordDOMapper;
import com.flashsaleproject.dataObject.UserDO;
import com.flashsaleproject.dataObject.UserPasswordDO;
import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.error.EmBusinessError;
import com.flashsaleproject.service.UserService;
import com.flashsaleproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

//    @Autowired
//    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null) {
            return null;
        } else {
            // get userPasswordDO by userId
            UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
            // convert userDO to userModel
            return convertFromDataObject(userDO, userPasswordDO);
        }
    }

    @Override
    @Transactional
    // add transactional annotation to make sure userDO and userPasswordDO are inserted into database at the same time
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

//        // validate userModel
//        ValidationResult result = validator.validate(userModel);
//        if (result.isHasErrors()) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
//        }

        if (StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelphone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // convert userModel to userDO
        UserDO userDO = convertFromUserModel(userModel);
        // insert userDO to database
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException exception) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "telphone number already registered");
        }

        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertFromUserPasswordModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel login(String telphone, String encryptPassword) throws BusinessException {
        // get userDO by telphone
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        // check if userDO exists
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        // get userPasswordDO by userId
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        // convert userDO to userModel
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        // compare password with userModel's password
        if (!StringUtils.equals(encryptPassword, userModel.getEncrptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserDO convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        // copy userModel to userDO (same field name)
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserPasswordDO convertFromUserPasswordModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel); // copy userDO to userModel (same field name)

        if (userPasswordDO != null) {

            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
