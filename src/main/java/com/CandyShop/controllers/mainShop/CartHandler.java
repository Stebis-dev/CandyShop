package com.CandyShop.controllers.mainShop;

import com.CandyShop.model.Cart;

public interface CartHandler {
    void updateCart(Cart cart);

    void deleteCart(Cart cart);
}
