package com.flashsaleproject.service;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.service.model.ItemModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
public interface ItemService {
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    List<ItemModel> listItem();
    ItemModel getItemById(Integer id);

    // decrease the stock
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    @Transactional
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
