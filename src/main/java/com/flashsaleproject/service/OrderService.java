package com.flashsaleproject.service;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.service.model.OrderModel;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;

}
