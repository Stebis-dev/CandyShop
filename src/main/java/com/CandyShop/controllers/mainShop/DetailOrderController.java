package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DetailOrderController {

    @FXML
    public ListView<Order> orderList;
    @FXML
    public ListView<Manager> employeeList;
    @FXML
    public Label orderAssignedStatus;
    @FXML
    public ListView<Order> filteredOrderList;
    @FXML
    public DatePicker intervalStart;
    @FXML
    public DatePicker intervalEnd;
    @FXML
    public ChoiceBox<OrderStatus> orderStatus;
    @FXML
    public ChoiceBox<Manager> employeeChoice;
    @FXML
    public PieChart pieChart1;
    @FXML
    public Label sum;

    private CustomHib customHib;

    public User currentUser;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }


    public List<Order> sortedOrders() {

        List<Order> orders = customHib.getAllRecords(Order.class);
        for (Order order : orders) {
            if (order.getManager() == null)
                order.setShowImportance(true);
        }
        Collections.sort(orders);
        return orders;
    }


    public void loadData() {
        orderList.getItems().clear();
        orderList.getItems().addAll(sortedOrders());
        employeeList.getItems().clear();
        employeeList.getItems().addAll(customHib.getAllRecords(Manager.class));
        orderAssignedStatus.setText("Assigned to: ");

        orderStatus.getItems().clear();
        orderStatus.getItems().addAll(OrderStatus.values());
        orderStatus.getItems().add(null);

        employeeChoice.getItems().clear();
        employeeChoice.getItems().addAll(customHib.getAllRecords(Manager.class));
        employeeChoice.getItems().add(null);

        filteredOrderList.getItems().clear();
        pieChart1.setData(null);
        sum.setText("The sum: ");
    }

    public void loadAssignedEmployee() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            if (selectedOrder.getManager() != null) {
                orderAssignedStatus.setText("Assigned to: " + selectedOrder.getManager());
            } else {
                orderAssignedStatus.setText("Assigned to: none");
            }
        } catch (Exception ignored) {

        }
    }

    public void takeOrder() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            selectedOrder.setManager((Manager) currentUser);
            customHib.update(selectedOrder);
            loadData();
        } catch (Exception ignored) {

        }
    }

    public void assignOrder() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            Manager selectedManager = employeeList.getSelectionModel().getSelectedItem();
            selectedOrder.setManager(selectedManager);
            customHib.update(selectedOrder);
            loadData();

        } catch (Exception ignored) {

        }
    }

    public void clearAssigned() {
        try {
            Order selectedOrder = orderList.getSelectionModel().getSelectedItem();
            selectedOrder.setManager(null);
            customHib.update(selectedOrder);
            loadData();
        } catch (Exception ignored) {

        }
    }

    public void filterOrders() {
        try {
            List<Order> filteredOrders = customHib.getFilteredOrders(
                    intervalStart.getValue(),
                    intervalEnd.getValue(),
                    orderStatus.getValue(),
                    employeeChoice.getValue()
            );
            filteredOrderList.getItems().clear();
            filteredOrderList.getItems().addAll(filteredOrders);

            Map<String, Double> productAmount = new HashMap<>();

            for (Order order : filteredOrders) {
                List<OrderDetails> orderDetailsList = customHib.getOrderDetails(order.getId());
                for (OrderDetails orderDetails : orderDetailsList) {
                    String product = orderDetails.getProduct().getName();
                    double amount = orderDetails.getAmount();

                    if (productAmount.containsKey(product)) {
                        productAmount.put(product, productAmount.get(product) + amount);
                    } else {
                        productAmount.put(product, amount);
                    }
                }
            }
            double sumOfProducts = 0;
            for (Product product : customHib.getAllRecords(Product.class)) {
                if (productAmount.containsKey(product.getName())) {
                    productAmount.put(product.getName(), productAmount.get(product.getName()) * product.getPrice());
                    sumOfProducts += productAmount.get(product.getName()) * product.getPrice();
                }
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();


            for (Map.Entry<String, Double> entry : productAmount.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            pieChart1.setData(pieChartData);
            pieChart1.setTitle("Product Sales Data");
            sum.setText("The sum: " + String.format("%.2f", sumOfProducts) + " €");
        } catch (Exception ignored) {

        }
    }
}
