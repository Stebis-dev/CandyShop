package com.CandyShop.controllers.common;

import com.CandyShop.controllers.mainShop.CartHandler;
import com.CandyShop.controllers.mainShop.CatalogHandler;
import com.CandyShop.model.Cart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CartProductCardController {
    @FXML
    public HBox root;
    @FXML
    public ImageView cartProductImage;
    @FXML
    public Label cartProductName;
    @FXML
    public Label cartProductPrice;
    @FXML
    public Label productAmount;
    @FXML
    public Label totalPrice;
    private Cart cart;
    private CartHandler cartHandler;

    public void setCart(Cart cart) {
        this.cart = cart;
        loadCartData();
    }

    public void setCartHandler(CartHandler cartHandler) {
        this.cartHandler = cartHandler;
    }


    public void loadCartData() {
        cartProductName.setText(cart.getProduct().getName());
        cartProductPrice.setText(cart.getProduct().getPrice() + " €");
        if (cart.getProduct().getImagePath() != null) {
            cartProductImage.setImage(new Image(cart.getProduct().getImagePath()));
        }
        productAmount.setText(Integer.toString(cart.getAmount()));
        totalPrice.setText(String.format("%.2f", (cart.getAmount() * cart.getProduct().getPrice())) + " €");
    }


    public void depriveFromAmount() {
        if (cart.getAmount() > 0) {
            cart.setAmount(cart.getAmount() - 1);
        }
        loadCartData();
        cartHandler.updateCart(cart);
    }

    public void addToAmount() {
        if (cart.getAmount() < 500) {
            cart.setAmount(cart.getAmount() + 1);
        }
        loadCartData();
        cartHandler.updateCart(cart);
    }

    public void deleteProductFromCart() {
        cartHandler.deleteCart(cart);
    }
}
