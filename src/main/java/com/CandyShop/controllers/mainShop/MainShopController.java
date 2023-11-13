package com.CandyShop.controllers.mainShop;

import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class MainShopController implements Initializable {

    @FXML
    public TabPane tabPane;
    @FXML
    public Tab catalogTab;
    @FXML
    public Tab cartTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab productsTab;
    @FXML
    public Tab commentTab;
    @FXML
    public Tab OrderTab;

    @FXML
    private CatalogController catalogSectionController;
    @FXML
    private CartController cartSectionController;
    @FXML
    private ProductController productSectionController;
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

    public MainShopController() {
    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;

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
                catalogSectionController.loadData();
            } else if (cartTab.isSelected()) {
                cartSectionController.setCurrentUser(currentUser);
                cartSectionController.setEntityManagerFactory(entityManagerFactory);
                cartSectionController.loadData();
            } else if (productsTab.isSelected()) {
                productSectionController.setEntityManagerFactory(entityManagerFactory);
                productSectionController.loadData();
            } else if (usersTab.isSelected()) {
                userSectionController.setEntityManagerFactory(entityManagerFactory);
            } else if (warehouseTab.isSelected()) {
                warehouseSectionController.setEntityManagerFactory(entityManagerFactory);
            } else if (commentTab.isSelected()) {
                commentSectionController.setEntityManagerFactory(entityManagerFactory);
            } else if (OrderTab.isSelected()) {
                orderSectionController.setEntityManagerFactory(entityManagerFactory);
            }
        } catch (NullPointerException ignored) {

        }
    }

    public void openCartTab() {
        tabPane.getSelectionModel().select(cartTab);
    }
}
