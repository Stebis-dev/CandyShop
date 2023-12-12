package com.CandyShop.controllers.mainShop;

import com.CandyShop.StartGui;
import com.CandyShop.controllers.common.CartProductCardController;
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

    private static int NUM_CARDS_PER_ROW = 1;
    private static final double CARD_SPACING = 10;
    @FXML
    private ScrollPane cartPane;
    private FlowPane cartContainer;
    private List<CartProductCardController> cartProductsControllers = new ArrayList<>();

    public User currentUser;

    private CustomHib customHib;


    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void loadData() {
        loadCards();
        loadCartList();
        loadWarehouseList();
    }

    private void addCartProductCard(Cart cart) {
        try {
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("common/CartProductCard.fxml"));
            Node card = loader.load();
            CartProductCardController controller = loader.getController();
            controller.setCart(cart);
            controller.setCartHandler(this);
            cartProductsControllers.add(controller);
            cartContainer.getChildren().add(card);
        } catch (IOException ignored) {
        }
    }

    private void adjustCardSizes(double containerWidth) {
        double scrollWidth = cartPane.getWidth() - cartPane.getViewportBounds().getWidth();
        double cardWidth = containerWidth - scrollWidth;
        System.out.println(scrollWidth + " " + containerWidth + " " + cartPane.getWidth() + " " + cartPane.getViewportBounds().getWidth());
        for (CartProductCardController cardController : cartProductsControllers) {
            cardController.setCardSize(cardWidth);
        }
    }

    private void loadCards() {
        try {
            cartContainer = new FlowPane(Orientation.VERTICAL, CARD_SPACING, CARD_SPACING);
            cartContainer.setAlignment(Pos.TOP_CENTER);


            for (Cart cart : customHib.getUserCarts(currentUser.getId())) {
                addCartProductCard(cart);
            }

            double productPaneWidth = cartPane.getWidth();

            if (productPaneWidth >= 0) {
                cartContainer.setPrefWrapLength(productPaneWidth);
                adjustCardSizes(cartPane.getWidth());
            }


            cartPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
                cartContainer.setPrefWrapLength(newValue.getWidth());
                double containerWidth = newValue.getWidth();
                adjustCardSizes(containerWidth);
            });

            cartPane.setContent(cartContainer);
        } catch (NullPointerException ignored) {
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
            loadCards();
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

    @Override
    public void updateCart(Cart cart) {
        customHib.update(cart);
    }

    @Override
    public void deleteCart(Cart cart) {
        customHib.delete(Cart.class, cart.getId());
        loadCards();
    }
}
