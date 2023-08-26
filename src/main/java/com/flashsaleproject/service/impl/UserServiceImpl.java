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
    @Transactional // add transactional annotation to make sure userDO and userPasswordDO are inserted into database at the same time
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelphone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // convert userModel to userDO
        UserDO userDO = convertFromUserModel(userModel);
        // insert userDO to database
        userDOMapper.insertSelective(userDO);

        UserPasswordDO userPasswordDO = convertFromUserPasswordModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

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
