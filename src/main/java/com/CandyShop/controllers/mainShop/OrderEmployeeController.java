package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderEmployeeController {

    @FXML
    public ListView<Order> orderList;
    @FXML
    public ListView<OrderDetails> orderDetailList;
    @FXML
    public ListView<Comment> chatList;
    @FXML
    public TextArea chatInputField;
    @FXML
    public Button chatSubmit;
    @FXML
    public Label statusLabel;
    @FXML
    public ChoiceBox<OrderStatus> orderStatus;
    @FXML
    public TextField productAmount;
    @FXML
    public Label productName;
    @FXML
    public Label orderWarehouse;
    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public Button deleteOrderButton;

    private CustomHib customHib;

    public User currentUser;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void loadData() {
        loadOrderList();
        statusLabel.setText("Status: ");
        orderStatus.getItems().clear();
        orderStatus.getItems().addAll(OrderStatus.values());
        chatList.getItems().clear();
        warehouseList.getItems().clear();
        orderWarehouse.setText("Warehouse: ");
        productName.setText("Product: ");
        productAmount.setText("");
    }

    public void loadOrderList() {
        orderList.getItems().clear();
        orderList.getItems().addAll(customHib.getManagerOrder(currentUser.getId()));
        orderDetailList.getItems().clear();


    }

    public void loadOrderDetailList() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            statusLabel.setText("Status: " + selectedOrder.getStatus());
            orderStatus.getSelectionModel().select(selectedOrder.getStatus());
            orderDetailList.getItems().clear();
            orderDetailList.getItems().addAll(customHib.getOrderDetails(selectedOrder.getId()));

            orderWarehouse.setText("Warehouse: " + selectedOrder.getWarehouse());
            warehouseList.getItems().clear();
            warehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));
            loadComments();

            if (selectedOrder.getStatus().equals(OrderStatus.COMPLETE) ||
                    selectedOrder.getStatus().equals(OrderStatus.PROCESSING)) {
                deleteOrderButton.setDisable(true);
            } else {
                deleteOrderButton.setDisable(false);
            }

        } catch (Exception ignored) {

        }
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void submitMessage() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        int commentLevel = 0;

        if (!chatInputField.getText().isEmpty()) {
            Comment comment = new Comment(chatInputField.getText(), null, currentUser,
                    selectedOrder, LocalDateTime.now(), commentLevel);
            customHib.create(comment);
        }

        chatInputField.clear();
        loadComments();
    }

    private void loadComments() {
        chatList.getItems().clear();
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        List<Comment> orderComments = customHib.getOrderComments(selectedOrder.getId());
        chatList.getItems().addAll(orderComments);
    }

    public void deleteSelectedComment() {
        Comment selectedComment = chatList.getSelectionModel().getSelectedItem();

        if (selectedComment != null) {
            customHib.delete(Comment.class, selectedComment.getId());
            loadComments();
        }
    }

    public void deleteOrder() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        customHib.deleteOrder(selectedOrder.getId());
        loadData();
    }

    public void update() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        int selectedIndex = orderList.getSelectionModel().getSelectedIndex();
        selectedOrder.setStatus(orderStatus.getValue());
        try {
            selectedOrder.setWarehouse(warehouseList.getSelectionModel().getSelectedItem());
        } catch (Exception ignored) {

        }
        customHib.update(selectedOrder);

        loadOrderList();
        orderList.getSelectionModel().select(selectedIndex);
        loadOrderDetailList();

    }

    public void updateSelectedProduct() {
        try {
            OrderDetails selectedOrderDetails = orderDetailList.getSelectionModel().getSelectedItem();
            selectedOrderDetails.setAmount(Integer.parseInt(productAmount.getText()));
            productAmount.setText(Integer.toString(selectedOrderDetails.getAmount()));
            customHib.update(selectedOrderDetails);
            loadOrderDetailList();
        } catch (Exception ignored) {

        }
    }

    public void updateOrderWarehouse() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        selectedOrder.setWarehouse(warehouseList.getSelectionModel().getSelectedItem());
        orderWarehouse.setText("Warehouse: " + selectedOrder.getWarehouse().getTitle());
    }

    public void deleteSelectedProduct() {
        OrderDetails selectedOrderDetails = orderDetailList.getSelectionModel().getSelectedItem();
        customHib.delete(OrderDetails.class, selectedOrderDetails.getId());
        loadOrderDetailList();
        productName.setText("Product: ");
        productAmount.setText("");
    }

    public void loadProductDetails() {
        OrderDetails selectedOrderDetails = orderDetailList.getSelectionModel().getSelectedItem();
        productName.setText(selectedOrderDetails.getProduct().getName());
        productAmount.setText(Integer.toString(selectedOrderDetails.getAmount()));
    }
}
