package com.CandyShop.controllers.common;

import com.CandyShop.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ProductCardController {
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productDescription;
    // Other fields and methods

    public void setProduct(Product product) {
        // Set product details to the UI elements
        productName.setText(product.getTitle());
        productDescription.setText(product.getDescription());
        // Set the image if available
    }
}
