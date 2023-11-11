package com.CandyShop.controllers.mainshop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ProductController {
    @FXML
    public ListView<Product> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea productDescriptionField;
    @FXML
    public ComboBox<ProductType> productType;
    @FXML
    public TextField weightField;
    @FXML
    public TextArea chemicalDescriptionField;
    @FXML
    public TextField productManufacturerField;

    private CustomHib customHib;


//    public ProductController() {
////        loadProductListManager();
////        List<Warehouse> record = customHib.getAllRecords(Warehouse.class);
////        productTitleField.clear();
////        productManufacturerField.clear();
////        weightField.clear();
////        productDescriptionField.clear();
////        chemicalDescriptionField.clear();
//    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    public void enableProductFields() {
        if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
            weightField.setPromptText("Weight g.");
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
            weightField.setPromptText("Content volume ml.");
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
            weightField.setPromptText("Weight g.");
        }
    }

    private void loadProductListManager() {
        productListManager.getItems().clear();
        productListManager.getItems().addAll(customHib.getAllRecords(Product.class));
    }

    public void addNewProduct() {

        Product product = new Product();
        if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
            product = new Candy(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText());
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
            product = new Drinks(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText());
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
            product = new Snacks(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText());
        }
        customHib.create(product);
        loadProductListManager();
    }

    public void updateProduct() {
        try {
            Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
            Product product = customHib.getEntityById(Product.class, selectedProduct.getId());

            product.setTitle(productTitleField.getText());
            product.setDescription(productDescriptionField.getText());
            product.setManufacturer(productManufacturerField.getText());

            if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
                ((Candy) product).setWeight(Double.parseDouble(weightField.getText()));
                ((Candy) product).setChemicalContents(chemicalDescriptionField.getText());
            } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
                ((Drinks) product).setContentVolume(Double.parseDouble(weightField.getText()));
                ((Drinks) product).setChemicalContents(chemicalDescriptionField.getText());
            } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
                ((Snacks) product).setWeight(Double.parseDouble(weightField.getText()));
                ((Snacks) product).setChemicalContents(chemicalDescriptionField.getText());
            }

            customHib.update(product);
            loadProductListManager();
        } catch (NullPointerException ignored) {

        }
    }

    public void deleteProduct() {
        try {
            Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
            customHib.deleteProduct(selectedProduct.getId());
            loadProductListManager();
        } catch (NullPointerException ignored) {

        }
    }

    public void loadProductData() {
        try {
            Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
            productTitleField.setText(selectedProduct.getTitle());
            productDescriptionField.setText(selectedProduct.getDescription());
            productManufacturerField.setText(selectedProduct.getManufacturer());

            String productClass = selectedProduct.getClass().getSimpleName();
            switch (productClass) {
                case "Candy" -> {
                    productType.getSelectionModel().select(ProductType.CANDY);
                    weightField.setText(String.valueOf(((Candy) selectedProduct).getWeight()));
                    chemicalDescriptionField.setText(((Candy) selectedProduct).getChemicalContents());
                }
                case "Drinks" -> {
                    productType.getSelectionModel().select(ProductType.DRINK);
                    weightField.setText(String.valueOf(((Drinks) selectedProduct).getContentVolume()));
                    chemicalDescriptionField.setText(((Drinks) selectedProduct).getChemicalContents());
                }
                case "Snacks" -> {
                    productType.getSelectionModel().select(ProductType.SNACKS);
                    weightField.setText(String.valueOf(((Snacks) selectedProduct).getWeight()));
                    chemicalDescriptionField.setText(((Snacks) selectedProduct).getChemicalContents());

                }
            }

//            warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());
        } catch (NullPointerException ignored) {

        }
    }
}
