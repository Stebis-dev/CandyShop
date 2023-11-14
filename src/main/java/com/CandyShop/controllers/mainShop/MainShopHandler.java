package com.CandyShop.controllers.mainShop;

import com.CandyShop.model.Product;

public interface MainShopHandler {
    void setProductCreationTab(Product product);

    void addToCart(Product product, int amount);

}
