package com.flashsaleproject.service;

import com.flashsaleproject.service.model.PromoModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 9/1/23
 */
public interface PromoService {
    // get promo by item id
    PromoModel getPromoByItemId(@Param("id") Integer itemId);

}
