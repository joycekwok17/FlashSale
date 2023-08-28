package com.flashsaleproject.controller.viewObject;

import java.math.BigDecimal;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
public class ItemVO {
    private Integer id;

    //商品名称

    private String title;

    //商品价格
    private BigDecimal price;

    //商品库存
    private Integer stock;

    //商品的描述
    private String description;

    //商品的销量
    private Integer sales;

    //商品描述图片的url
    private String imgUrl;

}
