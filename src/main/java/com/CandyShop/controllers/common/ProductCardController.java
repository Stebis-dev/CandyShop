package com.CandyShop.controllers.common;

import com.CandyShop.controllers.mainShop.CartHandler;
import com.CandyShop.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProductCardController {
    public VBox root;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    private Product product;
    private CartHandler cartHandler;


    public void setProduct(Product product) {
        this.product = product;
        productName.setText(product.getName());
        productPrice.setText(Double.toString(product.getPrice()) + " €");
        if (product.getImagePath() != null) {
            productImage.setImage(new Image(product.getImagePath()));
        }
    }

    public void setCartHandler(CartHandler cartHandler) {
        this.cartHandler = cartHandler;
    }

    public void setCardSize(double width) {
        // set min
        productImage.setFitWidth(width - 20);
        root.setPrefWidth(width);

    }

    public void addProductToCart() {
        cartHandler.addToCart(product);
    }


}
