package com.flashsaleproject.controller;

import com.flashsaleproject.controller.viewObject.UserVO;
import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.error.EmBusinessError;
import com.flashsaleproject.response.CommonReturnType;
import com.flashsaleproject.service.UserService;
import com.flashsaleproject.service.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/24/23
 */
@Controller("user")
@RequestMapping("/user") // http://localhost:8080/user -> UserController
@CrossOrigin(originPatterns = "*", allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="password") String password) throws BusinessException {
        // verify otp code is consistent with the telephone number
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "otp code is not correct");
        }
        // user register flow
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(Byte.parseByte(String.valueOf(gender)));
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getOtp", method = {RequestMethod.POST}, consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        // according to telephone generate otp code
        Random random = new Random();
        int i = random.nextInt(99999);
        i += 10000;
        String otpCode = String.valueOf(i);

        // according to telephone number and otp code bind them together, use httpsession to bind them
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        // send otp code to user (via sms)
        System.out.println("telphone = " + telphone + " & otpCode = " + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUserById(@RequestParam(name = "id") Integer id) throws BusinessException {
        // call service to get user by id and return to front end
        UserModel userModel = userService.getUserById(id);
//        userModel= null;
//        userModel.setEncrptPassword("123");
        if (userModel == null) {
//            userModel.setEncrptPassword("123");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        // convert userModel to userVO and return to front end (userVO is a view object),
        // ensure UI only get necessary data
        UserVO userVO = convertFromUserModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO); // copy userModel to userVO (same field name)
        return userVO;
    }

}
