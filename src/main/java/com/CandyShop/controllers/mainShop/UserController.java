package com.CandyShop.controllers.mainShop;

import com.CandyShop.controllers.mainShop.tableViewGuidelines.CustomerTableGuidelines;
import com.CandyShop.controllers.mainShop.tableViewGuidelines.ManagerTableGuidelines;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Customer;
import com.CandyShop.model.Manager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    @FXML
    public TableView<CustomerTableGuidelines> customerTable;
    @FXML
    public TableView<ManagerTableGuidelines> managerTable;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableName;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableSurname;
    @FXML
    public TableColumn<CustomerTableGuidelines, Date> customerTableBirthdate;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableAddress;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableCardNumber;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerEmployeeId;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerTableName;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerTableSurname;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerBirthdate;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerTableMedicalCertificate;
    @FXML
    public TableColumn<ManagerTableGuidelines, String> managerTableEmploymentDate;

    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    public void loadData() {
        loadUsers();
        loadManagers();
    }

    private void loadUsers() {
        List<Customer> customersList = customHib.getAllRecords(Customer.class);
        List<CustomerTableGuidelines> customerTableGuidelinesList = customersList.stream()
                .map(CustomerTableGuidelines::new)
                .collect(Collectors.toList());
        ObservableList<CustomerTableGuidelines> customers = FXCollections.observableArrayList(customerTableGuidelinesList);

        customerTable.setItems(customers);

        customerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerTableBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        customerTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTableCardNumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
    }

    private void loadManagers() {
        List<Manager> managerList = customHib.getAllRecords(Manager.class);
        List<ManagerTableGuidelines> managerTableGuidelinesList = managerList.stream()
                .map(ManagerTableGuidelines::new)
                .collect(Collectors.toList());
        ObservableList<ManagerTableGuidelines> managers = FXCollections.observableArrayList(managerTableGuidelinesList);

        managerTable.setItems(managers);

        managerEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        managerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        managerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        managerBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        managerTableMedicalCertificate.setCellValueFactory(new PropertyValueFactory<>("medicalCertificate"));
        managerTableEmploymentDate.setCellValueFactory(new PropertyValueFactory<>("employmentDate"));
    }
}
