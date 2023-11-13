package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import com.CandyShop.utils.JavaFxCustomUtils;
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
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab productsTab;
    @FXML
    public Tab primaryTab;
    @FXML
    public Tab commentTab;
    @FXML
    public Tab OrderTab;
    @FXML
    private ProductController productSectionController;
    @FXML
    private WarehouseController warehouseSectionController;
    @FXML
    private OrderController orderSectionController;
    @FXML
    private CartController cartSectionController;
    @FXML
    private CommentController commentSectionController;
    @FXML
    private UserController userSectionController;
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        limitAccess();
        customHib = new CustomHib(entityManagerFactory);

        loadTabValues();
    }

    private void limitAccess() {
        if (false) {
            if (currentUser.getClass() == Manager.class) {
                Manager manager = (Manager) currentUser;
                if (!manager.isAdmin()) {
//                    managerTable.setDisable(true);
                }
                tabPane.getTabs().remove(primaryTab);
                tabPane.getTabs().remove(commentTab);
            } else if (currentUser.getClass() == Customer.class) {
                Customer customer = (Customer) currentUser;
                tabPane.getTabs().remove(usersTab);
                tabPane.getTabs().remove(warehouseTab);
                tabPane.getTabs().remove(productsTab);

            } else {
                JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "WTF", "WTF", "WTF");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void loadTabValues() {
        try {
            if (primaryTab.isSelected()) {
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
}
