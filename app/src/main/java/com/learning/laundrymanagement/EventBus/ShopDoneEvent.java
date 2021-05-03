package com.learning.laundrymanagement.EventBus;

import com.learning.laundrymanagement.Model.Shop;

import java.util.List;

public class ShopDoneEvent {
    private List<Shop> shopList;

    public ShopDoneEvent(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
