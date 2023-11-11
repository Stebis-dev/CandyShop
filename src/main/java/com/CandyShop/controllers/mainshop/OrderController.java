package com.CandyShop.controllers.mainshop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Order;
import com.CandyShop.model.OrderDetails;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class OrderController {

    @FXML
    public ListView<Order> orderList;
    @FXML
    public ListView<OrderDetails> orderDetailList;

    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    public void loadOrderList() {
        orderList.getItems().clear();
        orderList.getItems().addAll(customHib.getAllRecords(Order.class));
        orderDetailList.getItems().clear();
    }

    public void loadOrderDetailList() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            orderDetailList.getItems().clear();
            orderDetailList.getItems().addAll(customHib.getOrderDetails(selectedOrder.getId()));
        } catch (Exception e) {

        }
    }
}
