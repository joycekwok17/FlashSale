package com.flashsaleproject.service.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/28/23
 */
@Data
public class ItemModel {

    private Integer id;

    //商品名称
    @NotNull(message = "item name cannot be empty")
    private String title;

    //商品价格
    @NotNull(message = "item price cannot be empty")
    @Min(value = 0,message = "item price cannot be less than 0")
    private BigDecimal price;

    //商品库存
    @NotNull(message = "stock cannot be empty")
    private Integer stock;

    //商品的描述
    @NotBlank(message = "item description cannot be empty")
    private String description;

    //商品的销量
    private Integer sales;

    //商品描述图片的url
    @NotBlank(message = "item img url cannot be empty")
    private String imgUrl;

}