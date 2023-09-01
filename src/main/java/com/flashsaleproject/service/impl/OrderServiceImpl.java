package com.flashsaleproject.service.impl;

import com.flashsaleproject.dao.OrderDOMapper;
import com.flashsaleproject.dao.SequenceDOMapper;
import com.flashsaleproject.dataObject.OrderDO;
import com.flashsaleproject.dataObject.SequenceDO;
import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.error.EmBusinessError;
import com.flashsaleproject.service.OrderService;
import com.flashsaleproject.service.model.ItemModel;
import com.flashsaleproject.service.model.OrderModel;
import com.flashsaleproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Transactional
    @Override
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        // validate the input parameters here        // check the item is available
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "Item does not exist");
        }
        UserModel userModel = userService.getUserById(userId);
        // check the user is valid
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "User does not exist");
        }
        // check the amount is valid
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "Amount is not valid");
        }

        // check the promo is valid
        if (promoId != null) {
            // check the promo is for this item
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "Promo is not valid");
            } else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "Promo has not started");
            }
        }

        // deduct the item stock
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        // create the order
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);

        // set the item price to the promo price if there is a promo
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            // set the item price to the original price if there is no promo
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new java.math.BigDecimal(amount)));

        // generate the order id and set it to orderModel
        orderModel.setId(generateOrderId());
        OrderDO orderDO = this.convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        itemService.increaseSales(itemId, amount);
        // return the orderModel to the controller
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderId() {
        StringBuilder stringBuilder = new StringBuilder();

        // generate the first 8 digits of the order id according to the current time
        LocalDateTime now = LocalDateTime.now(); // get the current time
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", ""); // convert the time to string
        stringBuilder.append(nowDate);

        // generate the middle 6 digits of the order id according to the sequence number of the order
        // get the current sequence number
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        // convert the sequence number to string
        String sequenceStr = String.valueOf(sequence);
        // fill the sequence number with 0 to make it 6 digits
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        //generate the last 2 digits of the order id
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue()); // convert BigDecimal to double
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue()); // convert BigDecimal to double
        return orderDO;
    }
}
