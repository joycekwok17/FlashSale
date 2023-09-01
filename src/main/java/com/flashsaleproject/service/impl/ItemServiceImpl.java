package com.flashsaleproject.service.impl;

import com.flashsaleproject.dao.ItemDOMapper;
import com.flashsaleproject.dao.ItemStockDOMapper;
import com.flashsaleproject.dao.PromoDOMapper;
import com.flashsaleproject.dataObject.ItemDO;
import com.flashsaleproject.dataObject.ItemStockDO;
import com.flashsaleproject.dataObject.PromoDO;
import com.flashsaleproject.error.BusinessException;
import com.flashsaleproject.error.EmBusinessError;
import com.flashsaleproject.service.ItemService;
import com.flashsaleproject.service.PromoService;
import com.flashsaleproject.service.model.ItemModel;
import com.flashsaleproject.service.model.PromoModel;
import com.flashsaleproject.validator.ValidationResult;
import com.flashsaleproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // validate the input
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        // convert itemModel to itemDO
        ItemDO itemDO = this.convertFromItemModel(itemModel);
        // write into database
        try {
            itemDOMapper.insertSelective(itemDO);
        } catch (Exception e) {
            System.out.println(e);
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "Failed to create item");
        }
        // get the id from database and set it to itemModel
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        return this.getItemById(itemModel.getId());
    }

    private ItemDO convertFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        // copy the properties from itemModel to itemDO
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue()); // convert BigDecimal to double
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    private ItemModel convertFromItemDO(ItemDO itemDO, ItemStockDO itemStockDO) {
        if (itemDO == null) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(BigDecimal.valueOf(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemModel> list = new ArrayList<>();

        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertFromItemDO(itemDO, itemStockDO);
            list.add(itemModel);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {

        // get the itemDO
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }
        // get the itemStockDO
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);

        // convert itemDO and itemStockDO to itemModel
        ItemModel itemModel = this.convertFromItemDO(itemDO, itemStockDO);

        // get the promoModel
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus() != 3) { // 3 means the promo is not started
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affectedRow > 0){
            // update the stock successfully
            return true;
        } else {
            // update the stock failed
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
    }

}
