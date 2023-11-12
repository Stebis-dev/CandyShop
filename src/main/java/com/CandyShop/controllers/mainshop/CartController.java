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
import java.util.ArrayList;
import java.util.List;

public class CartController implements CartHandler {

    @FXML
    public ListView<Product> productList;

    @FXML
    public ListView<Cart> userCart;
    @FXML
    public ListView<Warehouse> orderWarehouseList;

    private static int NUM_CARDS_PER_ROW = 4;
    private static final double CARD_SPACING = 10;
    @FXML
    private ScrollPane productPane;
    private FlowPane productContainer;
    private List<ProductCardController> cardControllers = new ArrayList<>();

    public User currentUser;

    private CustomHib customHib;


    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void loadData() {
        loadCartList();
        loadWarehouseList();
        loadCards();
    }

    private void adjustCardSizes(double containerWidth) {
//        System.out.println(containerWidth);
        if (containerWidth < 800) {
            NUM_CARDS_PER_ROW = 3;
        } else {
            NUM_CARDS_PER_ROW = 4;
        }
        double cardWidth = (containerWidth - (CARD_SPACING * NUM_CARDS_PER_ROW)) / NUM_CARDS_PER_ROW;
        for (ProductCardController card : cardControllers) {
            card.setCardSize(cardWidth);
        }
    }

    private void addProductCard(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("ProductCard.fxml"));
            Node card = loader.load();
            ProductCardController controller = loader.getController();
            controller.setProduct(product);
            controller.setCartHandler(this);
            cardControllers.add(controller);

            productContainer.getChildren().add(card);
        } catch (IOException ignored) {
        }
    }

    public void addToCart(Product product) {
        try {
            Cart customersCartWithProduct = customHib.getUserCartProduct(currentUser.getId(), product.getId());

            if (customersCartWithProduct != null) {
                customersCartWithProduct.addProduct();
                customHib.update(customersCartWithProduct);
            }
        } catch (Exception ignored) {
            if (product != null) {
                Cart cart = new Cart(LocalDate.now(), ((Customer) currentUser), product);
                customHib.create(cart);
            }
        } finally {
            loadCartList();
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
            loadCartList();
        }
    }

    public void deleteFromCart() {
        try {
            Cart selectCart = userCart.getSelectionModel().getSelectedItem();
            customHib.delete(Cart.class, selectCart.getId());
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

    private void loadCards() {
        try {
            productContainer = new FlowPane(Orientation.HORIZONTAL, CARD_SPACING, CARD_SPACING);
            productContainer.setAlignment(Pos.TOP_LEFT);

            productPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
                double width = newValue.getWidth();
                productContainer.setPrefWrapLength(width);
                adjustCardSizes(productPane.getWidth());
            });

            for (Product product : customHib.getAllRecords(Product.class)) {
                addProductCard(product);
            }


            productPane.setContent(productContainer);
        } catch (NullPointerException ignored) {
        }
    }

    private void loadCartList() {
        try {
            userCart.getItems().clear();
            userCart.getItems().addAll(customHib.getUserCarts(currentUser.getId()));
        } catch (Exception ignored) {
        }
    }
}
