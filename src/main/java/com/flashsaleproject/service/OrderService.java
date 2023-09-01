package com.flashsaleproject.service;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.service.model.OrderModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
public interface OrderService {

    @Transactional
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
