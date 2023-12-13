package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Product;
import com.CandyShop.model.Warehouse;
import com.CandyShop.model.WarehouseInventory;
import com.CandyShop.utils.JavaFxCustomUtils;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class WarehouseController {
    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;
    @FXML
    public ListView<WarehouseInventory> assignedProducts;
    @FXML
    public ListView<Product> notAssignedProducts;
    @FXML
    public TextField productAmount;

    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    public void loadData() {
        loadWarehouseList();
        loadWarehouseData();
        loadWarehouseInventoryData();
    }

    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));


        notAssignedProducts.getItems().clear();
//        if (warehouseList.getSelectionModel().getSelectedItem() != null) {
//            notAssignedProducts.getItems().addAll(customHib.getNotAssignedProducts(warehouseList.getSelectionModel().getSelectedItem().getId()));
//        } else {
        notAssignedProducts.getItems().addAll(customHib.getAllRecords(Product.class));
//        }
    }

    public void addNewWarehouse() {
        if (!titleWarehouseField.getText().trim().isEmpty() && !addressWarehouseField.getText().trim().isEmpty()) {
            customHib.create(new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText()));
            loadWarehouseList();
            loadWarehouseData();
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "Empty fields", "Please fill required fields");
        }
    }

    public void addProductToInventory() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            Product selectedProduct = notAssignedProducts.getSelectionModel().getSelectedItem();
            WarehouseInventory warehouseInventory = new WarehouseInventory(selectedWarehouse, selectedProduct, Integer.parseInt(productAmount.getText()));
            customHib.create(warehouseInventory);
            loadWarehouseData();
        } catch (NullPointerException ignored) {

        }
    }

    public void removeProductFromInventory() {
        try {
            WarehouseInventory selectedWarehouseInventory = assignedProducts.getSelectionModel().getSelectedItem();
            customHib.delete(WarehouseInventory.class, selectedWarehouseInventory.getId());
            loadWarehouseData();
        } catch (Exception ignored) {

        }
    }

    public void updateInventory() {
        try {
            WarehouseInventory selectedWarehouseInventory = assignedProducts.getSelectionModel().getSelectedItem();
            selectedWarehouseInventory.setAmount(Integer.parseInt(productAmount.getText()));
            customHib.update(selectedWarehouseInventory);
            loadWarehouseData();
        } catch (Exception ignored) {

        }
    }

    public void updateWarehouse() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
            warehouse.setTitle(titleWarehouseField.getText());
            warehouse.setAddress(addressWarehouseField.getText());
            customHib.update(warehouse);
            loadWarehouseList();
        } catch (Exception e) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "None selected", "Please select value to continue");
        }
    }

    public void removeWarehouse() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            customHib.deleteWarehouse(selectedWarehouse.getId());
            loadWarehouseList();
            assignedProducts.getItems().clear();
            loadWarehouseInventoryData();

        } catch (Exception e) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "None selected", "Please select value to continue");
        }

    }

    public void loadWarehouseData() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            titleWarehouseField.setText(selectedWarehouse.getTitle());
            addressWarehouseField.setText(selectedWarehouse.getAddress());
            productAmount.setText("");

            assignedProducts.getItems().clear();
            List<WarehouseInventory> warehouseProducts = customHib.getWarehouseInventory(selectedWarehouse.getId());
            assignedProducts.getItems().addAll(warehouseProducts);

        } catch (Exception ignored) {
        }
    }

    public void loadWarehouseInventoryData() {
        WarehouseInventory selectedWarehouseInventory = assignedProducts.getSelectionModel().getSelectedItem();
        if (selectedWarehouseInventory != null) {
            productAmount.setText(String.valueOf(selectedWarehouseInventory.getAmount()));
        }
    }


}
