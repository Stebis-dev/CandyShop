package com.CandyShop.controllers.common;

import com.CandyShop.controllers.mainShop.CatalogHandler;
import com.CandyShop.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProductCardController {
    @FXML
    public VBox root;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    private Product product;
    private CatalogHandler catalogHandler;


    public void setProduct(Product product) {
        this.product = product;
        productName.setText(product.getName());
        productPrice.setText(Double.toString(product.getPrice()) + " €");
        if (product.getImagePath() != null) {
            productImage.setImage(new Image(product.getImagePath()));
        }
    }

    public void setCatalogHandler(CatalogHandler catalogHandler) {
        this.catalogHandler = catalogHandler;
    }

    public void setCardSize(double width) {
        // set min
        productImage.setFitWidth(width - 20);
        root.setPrefWidth(width);

    }

    public void addProductToCart() {
        catalogHandler.addToCart(product);
    }


}
