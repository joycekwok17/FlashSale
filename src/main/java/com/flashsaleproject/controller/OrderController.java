package com.flashsaleproject.controller;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.error.EmBusinessError;
import com.flashsaleproject.response.CommonReturnType;
import com.flashsaleproject.service.OrderService;
import com.flashsaleproject.service.model.OrderModel;
import com.flashsaleproject.service.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 9/1/23
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException {
        // get the user from the session
        Boolean isLogin = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");

        if (isLogin == null || !isLogin.booleanValue()) { // if not login
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "User not login, can not create order");
        }
        // get the user from the session
        UserModel loginUser = (UserModel) this.httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(loginUser.getId(), itemId, promoId, amount);

        return CommonReturnType.create(null);
    }

}
