package com.CandyShop.controllers.mainShop;

import com.CandyShop.StartGui;
import com.CandyShop.controllers.mainShop.tableViewGuidelines.CustomerTableGuidelines;
import com.CandyShop.controllers.mainShop.tableViewGuidelines.DatePickerTableCell;
import com.CandyShop.controllers.mainShop.tableViewGuidelines.ManagerTableGuidelines;
import com.CandyShop.controllers.registration.RegistrationController;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Customer;
import com.CandyShop.model.Manager;
import com.CandyShop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.hibernate.procedure.UnknownSqlResultSetMappingException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    @FXML
    public TableView<CustomerTableGuidelines> customerTable;
    @FXML
    public TableView<ManagerTableGuidelines> managerTable;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableId;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableName;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableSurname;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTableLogin;
    @FXML
    public TableColumn<CustomerTableGuidelines, String> customerTablePassword;
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
    @FXML
    public Button createUserButton;
    private EntityManagerFactory entityManagerFactory;
    private CustomHib customHib;
    private User currentUser;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        customHib = new CustomHib(this.entityManagerFactory);

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void loadData() {
        loadUsers();
        loadManagers();
        if (((Manager) currentUser).isAdmin()) {
            createUserButton.setVisible(true);
        } else {
            createUserButton.setVisible(false);
        }
    }

    private void loadUsers() {
        List<Customer> customersList = customHib.getAllRecords(Customer.class);
        List<CustomerTableGuidelines> customerTableGuidelinesList = customersList.stream()
                .map(CustomerTableGuidelines::new)
                .collect(Collectors.toList());
        ObservableList<CustomerTableGuidelines> customers = FXCollections.observableArrayList(customerTableGuidelinesList);

        customerTable.setItems(customers);
        if (((Manager) currentUser).isAdmin()) {
            customerTable.setEditable(true);
        }
        customerTable.setEditable(false);


        customerTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTableName.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTableName.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            customer.setName(t.getNewValue());
            customHib.update(customer.toCustomer(customHib));
        });
        customerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerTableSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTableSurname.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            customer.setSurname(t.getNewValue());
            customHib.update(customer.toCustomer(customHib));
        });
        customerTableLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        customerTableLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTableLogin.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            customer.setLogin(t.getNewValue());
            customHib.update(customer.toCustomer(customHib));
        });
        customerTablePassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        customerTablePassword.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTablePassword.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            String passw = BCrypt.hashpw(t.getNewValue(), BCrypt.gensalt());
            customer.setPassword(passw);
            customHib.update(customer.toCustomer(customHib));
        });
        customerTableBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
//        customerTableBirthdate.setCellFactory(column -> new DatePickerTableCell<CustomerTableGuidelines>());
//        customerTableBirthdate.setOnEditCommit(t -> {
//            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
//            customer.setBirthdate(t.getNewValue()); // Assuming setBirthdate now accepts LocalDate
//            customHib.update(customer.toCustomer());
//        });

        customerTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTableAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTableAddress.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            customer.setAddress(t.getNewValue());
            customHib.update(customer.toCustomer(customHib));
        });
        customerTableCardNumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        customerTableCardNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        customerTableCardNumber.setOnEditCommit(t -> {
            CustomerTableGuidelines customer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            customer.setCardNumber(t.getNewValue());
            customHib.update(customer.toCustomer(customHib));
        });
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

    public void createUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration/registration.fxml"));
        Parent parent = fxmlLoader.load();

        RegistrationController registrationController = fxmlLoader.getController();

        registrationController.setData(entityManagerFactory, currentUser);

        Scene scene = new Scene(parent);
        Stage stage = (Stage) customerTable.getScene().getWindow();
        stage.setTitle("CandyShop");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}
