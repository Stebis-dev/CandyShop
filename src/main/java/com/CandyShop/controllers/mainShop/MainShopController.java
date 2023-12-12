package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class MainShopController implements Initializable, MainShopHandler {

    @FXML
    public TabPane tabPane;
    @FXML
    public Tab selectedProductTab;
    @FXML
    public Tab catalogTab;
    @FXML
    public Tab cartTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab productCreationTab;
    @FXML
    public Tab commentTab;
    @FXML
    public Tab OrderTab;

    @FXML
    private ProductController productSectionController;
    @FXML
    private CatalogController catalogSectionController;
    @FXML
    private CartController cartSectionController;
    @FXML
    private ProductCreationController productCreationSectionController;
    @FXML
    private WarehouseController warehouseSectionController;
    @FXML
    private OrderController orderSectionController;
    @FXML
    private CommentController commentSectionController;
    @FXML
    private UserController userSectionController;
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;

    public MainShopController() {
    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        customHib = new CustomHib(entityManagerFactory);
        this.currentUser = currentUser;
        tabPane.getTabs().remove(selectedProductTab);

        loadTabValues();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void loadTabValues() {
        try {
            if (catalogTab.isSelected()) {
                catalogSectionController.setCurrentUser(currentUser);
                catalogSectionController.setEntityManagerFactory(entityManagerFactory);
                catalogSectionController.setMainShopHandler(this);
                catalogSectionController.loadData();
            } else if (cartTab.isSelected()) {
                cartSectionController.setCurrentUser(currentUser);
                cartSectionController.setEntityManagerFactory(entityManagerFactory);
                cartSectionController.loadData();
            } else if (productCreationTab.isSelected()) {
                productCreationSectionController.setEntityManagerFactory(entityManagerFactory);
                productCreationSectionController.loadData();
            } else if (usersTab.isSelected()) {
                userSectionController.setEntityManagerFactory(entityManagerFactory);
            } else if (warehouseTab.isSelected()) {
                warehouseSectionController.setEntityManagerFactory(entityManagerFactory);
                warehouseSectionController.loadData();
            } else if (commentTab.isSelected()) {
                commentSectionController.setEntityManagerFactory(entityManagerFactory);
            } else if (OrderTab.isSelected()) {
                orderSectionController.setEntityManagerFactory(entityManagerFactory);
                orderSectionController.setCurrentUser(currentUser);
                orderSectionController.loadData();
            }
        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public void setProductCreationTab(Product product) {
        tabPane.getTabs().add(0, selectedProductTab);
        tabPane.getSelectionModel().select(selectedProductTab);
        productSectionController.setData(product);
        productSectionController.setCurrentUser(currentUser);
        productSectionController.setEntityManagerFactory(entityManagerFactory);
        productSectionController.loadData();
    }
}
