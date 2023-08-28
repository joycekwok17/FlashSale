package com.flashsaleproject.service;

import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.service.model.ItemModel;

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
}
