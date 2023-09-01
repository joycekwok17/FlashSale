package com.flashsaleproject.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 9/1/23
 */
public class PromoModel {
    private Integer id;
    // 1 means not start, 2 means ongoing, 3 means ended
    private Integer status;
    // the name of the promo
    private String promoName;
    // the start date of the promo
    private DateTime startDate;
    // the end date of the promo
    private DateTime endDate;
    // the item id of the promo
    private Integer itemId;
    // the promo item price
    private BigDecimal promoItemPrice;

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }
}
