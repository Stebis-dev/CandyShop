package com.CandyShop.controllers.mainShop;

import com.CandyShop.model.Product;
import com.CandyShop.model.ProductType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProductController {
    @FXML
    public ImageView productImage;
    @FXML
    public Label productName;
    @FXML
    public Label productPrice;
    @FXML
    public Label productDescription;
    @FXML
    public Label productIngredients;
    @FXML
    public Label producNutritionalValue;
    @FXML
    public Label productWeight;
    @FXML
    public Label productCountryOfOrigin;
    @FXML
    public Label productStorageConditions;
    @FXML
    public Label amountLabel;
    private int amount;

    private Product product;
    private MainShopHandler mainShopHandler;

    void setData(Product product) {
        amount = 1;
        this.product = product;
        loadProductData();
    }

    public void setMainShopHandler(MainShopHandler mainShopHandler) {
        this.mainShopHandler = mainShopHandler;
    }

    public void loadProductData() {
        String measurment = "g.";
        if (product.getCategory() == ProductType.DRINK) {
            measurment = "ml.";
        }
        productImage.setImage(new Image(product.getImagePath()));
        productName.setText(product.getName() + ", " + Double.toString(product.getWeight()) + " " + measurment);
        productPrice.setText(Double.toString(product.getPrice()) + "€");
        amountLabel.setText(Integer.toString(amount));
        productDescription.setText(product.getDescription());
        productIngredients.setText(product.getIngredients());
        producNutritionalValue.setText(product.getNutritionalValue());
        productWeight.setText("Neto weight:\t\t\t" + Double.toString(product.getWeight()) + " " + measurment);
        productCountryOfOrigin.setText("Country of origin:\t\t" + product.getCountryOfOrigin());
        productStorageConditions.setText("Storage conditions:\t\t" + product.getStorageConditions());
    }

    public void decreaseAmount() {
        if (amount > 1) {
            amount = amount - 1;
        }
        amountLabel.setText(Integer.toString(amount));
    }

    public void AddAmount() {
        if (amount < 500) {
            amount = amount + 1;
        }
        amountLabel.setText(Integer.toString(amount));
    }

    public void addToCart() {
        mainShopHandler.addToCart(product, amount);
    }
}
