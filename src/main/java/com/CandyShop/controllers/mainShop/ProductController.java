package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class ProductController {
    @FXML
    public ListView<Product> productListManager;
    @FXML
    public Label productNameLabel;
    @FXML
    public TextField productNameField;
    @FXML
    public Label priceLabel;
    @FXML
    public TextField priceField;
    @FXML
    public Label countryOfOriginLabel;
    @FXML
    public TextField countryOfOriginField;
    @FXML
    public Label weightLabel;
    @FXML
    public TextField weightField;
    @FXML
    public Label storageConditionsLabel;
    @FXML
    public TextField storageConditionsField;
    @FXML
    public ComboBox<ProductType> productType;
    @FXML
    public Label descriptionLabel;
    @FXML
    public TextArea descriptionField;
    @FXML
    public ImageView imagePreview;
    @FXML
    public Label ingredientsLabel;
    @FXML
    public TextArea ingredientsField;
    @FXML
    public Label nutritionalValueLabel;
    @FXML
    public TextArea nutritionalValueField;


    public String imagePath;
    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void loadData() {
        clearProductData();
        loadProductListManager();
        loadProductType();
    }


    public void changedProductType() {
        if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
            weightField.setPromptText("Neto weight ml.");
            weightLabel.setText("Neto weight ml.");
            nutritionalValueLabel.setText("Nutritional value (100 ml.)");
        } else {
            weightField.setPromptText("Neto weight g");
            weightLabel.setText("Neto weight g");
            nutritionalValueLabel.setText("Nutritional value (100 g.)");
        }
    }


    public void addNewProduct() throws NullPointerException {
        Product product = new Product(
                productNameField.getText(),
                Double.parseDouble(priceField.getText()),
                countryOfOriginField.getText(),
                Double.parseDouble(weightField.getText()),
                storageConditionsField.getText(),
                productType.getSelectionModel().getSelectedItem(),
                descriptionField.getText(),
                imagePath,
                ingredientsField.getText(),
                nutritionalValueField.getText()
        );

        customHib.create(product);
        loadProductListManager();
    }

    public void updateProduct() throws NullPointerException {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        Product product = customHib.getEntityById(Product.class, selectedProduct.getId());

        product.setName(productNameField.getText());
        product.setPrice(Double.parseDouble(priceField.getText()));
        product.setCountryOfOrigin(countryOfOriginField.getText());
        product.setWeight(Double.parseDouble(weightField.getText()));
        product.setStorageConditions(storageConditionsField.getText());
        product.setCategory(productType.getSelectionModel().getSelectedItem());
        product.setDescription(descriptionField.getText());
        product.setIngredients(ingredientsField.getText());
        product.setNutritionalValue(nutritionalValueField.getText());
        product.setImagePath(imagePath);

        product.setImagePath(imagePath);

        customHib.update(product);
        loadProductListManager();
    }

    public void uploadImageButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File productImageFile = fileChooser.showOpenDialog(productNameField.getScene().getWindow());
        imagePath = productImageFile.toURI().toString();
        imagePreview.setImage(new Image(imagePath));
    }

    public void deleteProduct() throws NullPointerException {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        customHib.deleteProduct(selectedProduct.getId());
        loadProductListManager();
    }

    public void loadProductType() {
        productType.getItems().clear();
        productType.getItems().addAll(ProductType.values());
    }

    private void loadProductListManager() {
        productListManager.getItems().clear();
        productListManager.getItems().addAll(customHib.getAllRecords(Product.class));
    }

    public void loadProductData() {
        try {
            Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();

            productNameField.setText(selectedProduct.getName());
            priceField.setText(Double.toString(selectedProduct.getPrice()));
            countryOfOriginField.setText(selectedProduct.getCountryOfOrigin());
            weightField.setText(Double.toString(selectedProduct.getWeight()));
            storageConditionsField.setText(selectedProduct.getStorageConditions());
            productType.getSelectionModel().select(selectedProduct.getCategory());
            descriptionField.setText(selectedProduct.getDescription());
            ingredientsField.setText(selectedProduct.getIngredients());
            nutritionalValueField.setText(selectedProduct.getNutritionalValue());
            imagePreview.setImage(new Image(selectedProduct.getImagePath()));

        } catch (NullPointerException ignored) {
        }
    }

    public void clearProductData() {
        productNameField.clear();
        priceField.clear();
        countryOfOriginField.clear();
        weightField.clear();
        storageConditionsField.clear();
        descriptionField.clear();
        ingredientsField.clear();
        nutritionalValueField.clear();
        imagePreview.setImage(null);
    }
}
