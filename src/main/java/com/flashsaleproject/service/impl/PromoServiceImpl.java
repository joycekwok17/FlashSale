package com.flashsaleproject.service.impl;

import com.flashsaleproject.dao.PromoDOMapper;
import com.flashsaleproject.dataObject.PromoDO;
import com.flashsaleproject.service.PromoService;
import com.flashsaleproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 9/1/23
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        // convert promoDO to promoModel
        PromoModel promoModel = convertFromPromoDo(promoDO);
        // if current time is after the end date of the promo, then the promo is ended
        if (promoModel == null) {
            return null;
        }
        DateTime now = new DateTime();
        if (promoModel.getStartDate().isAfter(now)) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBefore(now)) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromPromoDo(PromoDO promoDO) {
        if (promoDO == null) return null;
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
