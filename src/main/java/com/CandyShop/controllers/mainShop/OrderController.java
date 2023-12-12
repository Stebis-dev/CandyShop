package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.List;

public class OrderController {

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
    public Button cancelButton;
    @FXML
    public Button completeButton;

    private CustomHib customHib;

    public User currentUser;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    public void loadData() {
        loadOrderList();
        statusLabel.setText("Status: ");
    }

    public void loadOrderList() {
        orderList.getItems().clear();
        orderList.getItems().addAll(customHib.getUsersOrder(currentUser.getId()));
        orderDetailList.getItems().clear();
    }

    public void loadOrderDetailList() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            statusLabel.setText("Status: " + selectedOrder.getStatus());
            orderDetailList.getItems().clear();
            orderDetailList.getItems().addAll(customHib.getOrderDetails(selectedOrder.getId()));

            cancelButton.setDisable(false);
            completeButton.setDisable(false);

            if (selectedOrder.getStatus().equals(OrderStatus.CANCELED.toString()) ||
                    selectedOrder.getStatus().equals(OrderStatus.COMPLETE.toString())) {
                cancelButton.setDisable(true);
                completeButton.setDisable(true);
            } else if (selectedOrder.getStatus().equals(OrderStatus.PROCESSING.toString())) {
                completeButton.setDisable(true);
            } else {
                cancelButton.setDisable(false);
                completeButton.setDisable(false);
            }
            loadComments();

        } catch (Exception e) {

        }
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }


    public void submitMessage() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        int commentLevel = 0;
        Integer parentComment = null;

        if (!chatInputField.getText().isEmpty()) {
            Comment comment = new Comment(chatInputField.getText(), parentComment, currentUser,
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

    }

    public void cancelOrder() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        int selectedIndex = orderList.getSelectionModel().getSelectedIndex();
        selectedOrder.setStatus(OrderStatus.CANCELED.toString());
        customHib.update(selectedOrder);
        loadOrderList();
        orderList.getSelectionModel().select(selectedIndex);
        loadOrderDetailList();

    }

    public void completeOrder() {
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
        int selectedIndex = orderList.getSelectionModel().getSelectedIndex();
        selectedOrder.setStatus(OrderStatus.PROCESSING.toString());
        customHib.update(selectedOrder);
        loadOrderList();
        orderList.getSelectionModel().select(selectedIndex);
        loadOrderDetailList();
    }
}
