package com.CandyShop.controllers.mainShop;

import com.CandyShop.StartGui;
import com.CandyShop.controllers.common.ProductCardController;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Cart;
import com.CandyShop.model.Customer;
import com.CandyShop.model.Product;
import com.CandyShop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CatalogController implements CatalogHandler {
    private static int NUM_CARDS_PER_ROW = 4;
    private static final double CARD_SPACING = 10;
    @FXML
    private ScrollPane productPane;
    private FlowPane productContainer;
    private List<ProductCardController> cardControllers = new ArrayList<>();
    private MainShopHandler mainShopHandler;
    public User currentUser;

    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setMainShopHandler(MainShopHandler mainShopHandler) {
        this.mainShopHandler = mainShopHandler;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void loadData() {
        loadCards();
    }


    private void adjustCardSizes(double containerWidth) {
        if (containerWidth > 0 && containerWidth < 300) {
            NUM_CARDS_PER_ROW = 1;
        } else if (containerWidth >= 300 && containerWidth < 600) {
            NUM_CARDS_PER_ROW = 2;
        } else if (containerWidth >= 600 && containerWidth < 1000) {
            NUM_CARDS_PER_ROW = 3;
        } else {
            NUM_CARDS_PER_ROW = 4;
        }
        double cardWidth = (containerWidth - (CARD_SPACING * (NUM_CARDS_PER_ROW - 1)) - 18) / NUM_CARDS_PER_ROW;
        for (ProductCardController card : cardControllers) {
            card.setCardSize(cardWidth);
        }
    }

    private void addProductCard(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("common/ProductCard.fxml"));
            Node card = loader.load();
            ProductCardController controller = loader.getController();
            controller.setProduct(product);
            controller.setCatalogHandler(this);
            controller.setMainShopHandler(mainShopHandler);
            cardControllers.add(controller);

            productContainer.getChildren().add(card);
        } catch (IOException ignored) {
        }
    }

    @Override
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
}
