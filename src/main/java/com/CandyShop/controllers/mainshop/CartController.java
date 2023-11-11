package com.CandyShop.controllers.mainshop;

import com.CandyShop.StartGui;
import com.CandyShop.controllers.common.ProductCardController;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CartController {

    @FXML
    public ListView<Product> productList;

    @FXML
    public ListView<Cart> userCart;
    @FXML
    public ListView<Warehouse> orderWarehouseList;

    @FXML
    private ScrollPane productPane;
    private FlowPane productContainer;

    public User currentUser;

    private CustomHib customHib;


    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void loadData() {
        loadProductList();
        loadCartList();
        loadWarehouseList();

//        productcard
        productContainer = new FlowPane(Orientation.HORIZONTAL, 10, 10);
        productContainer.setAlignment(Pos.TOP_LEFT);
//        productContainer.setPrefWrapLength(productPane.getPrefWidth());

        productPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            // Adjust the preferred wrap length of the FlowPane based on the new width
            double width = newValue.getWidth();
            productContainer.setPrefWrapLength(width);
        });

        // Load products and create cards for each
        for (Product product : customHib.getAllRecords(Product.class)) {
            addProductCard(product);
        }

        productPane.setContent(productContainer);
    }

    private void addProductCard(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("ProductCard.fxml"));
            Node card = loader.load();
            ProductCardController controller = loader.getController();
            controller.setProduct(product);

            productContainer.getChildren().add(card);
        } catch (IOException ignored) {
        }
    }


    public void addToCart() {
        Product selectProduct = null;
        try {
            selectProduct = productList.getSelectionModel().getSelectedItem();
            Cart customersCartWithProduct = customHib.getUserCartProduct(currentUser.getId(), selectProduct.getId());

            if (customersCartWithProduct != null) {
                customersCartWithProduct.addProduct();
                customHib.update(customersCartWithProduct);
            }
        } catch (Exception ignored) {
            if (selectProduct != null) {
                Cart cart = new Cart(LocalDate.now(), ((Customer) currentUser), selectProduct);
                customHib.create(cart);
            }
        } finally {
            loadProductList();
            loadCartList();
        }
    }

    public void deleteFromCart() {
        try {
            Cart selectCart = userCart.getSelectionModel().getSelectedItem();
            customHib.delete(Cart.class, selectCart.getId());
            loadProductList();
            loadCartList();
        } catch (NullPointerException ignored) {
        }
    }

    public void createOrder() {
        try {
            Warehouse selectedWarehouse = orderWarehouseList.getSelectionModel().getSelectedItem();
            Order order = new Order((Customer) currentUser, selectedWarehouse, "Processing");
            customHib.create(order);
            List<Cart> userCartList = customHib.getUserCarts(currentUser.getId());
            OrderDetails newOrderDetails;

            for (Cart cart : userCartList) {
                newOrderDetails = new OrderDetails(order, cart.getProduct(), cart.getAmount());
                customHib.create(newOrderDetails);
                customHib.delete(Cart.class, cart.getId());
            }
            loadCartList();
            loadWarehouseList();
        } catch (Exception ignored) {

        }
    }

    public void loadWarehouseList() {
        try {
            orderWarehouseList.getItems().clear();
            orderWarehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));
        } catch (Exception ignored) {
        }
    }

    private void loadCartList() {
        try {
            userCart.getItems().clear();
            userCart.getItems().addAll(customHib.getUserCarts(currentUser.getId()));
        } catch (Exception ignored) {
        }
    }

    private void loadProductList() {
        try {
            productList.getItems().clear();
            productList.getItems().addAll(customHib.getAllRecords(Product.class));
        } catch (Exception ignored) {
        }
    }
}
