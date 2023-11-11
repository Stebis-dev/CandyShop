package com.CandyShop.controllers.registration;

import com.CandyShop.StartGui;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Customer;
import com.CandyShop.model.Manager;
import com.CandyShop.model.User;
import com.CandyShop.utils.JavaFxCustomUtils;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public RadioButton customerCheckbox;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton managerCheckbox;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    @FXML
    public TextField employeeIdField;
    @FXML
    public TextField medCertificateField;
    @FXML
    public DatePicker employmentDateField;
    @FXML
    public CheckBox isAdminCheck;

    private EntityManagerFactory entityManagerFactory;
    private CustomHib customHib;

    public void setData(EntityManagerFactory entityManagerFactory, boolean isAdministrator) {
        this.entityManagerFactory = entityManagerFactory;
        if (isAdministrator) {
            manegerSelected();
        } else {
            customerSelected();
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory, Manager manager) {
        this.entityManagerFactory = entityManagerFactory;
        toggleFields(customerCheckbox.isSelected(), manager);
    }

    private void toggleFields(boolean isManager, User manager) {
        if (isManager) {
            addressField.setDisable(true);
            cardNoField.setDisable(true);
            employeeIdField.setDisable(false);
            medCertificateField.setDisable(false);
            employmentDateField.setDisable(false);
            if (((Manager) manager).isAdmin()) isAdminCheck.setDisable(false);
        } else {
            addressField.setDisable(false);
            cardNoField.setDisable(false);
            employeeIdField.setDisable(true);
            medCertificateField.setDisable(true);
            employmentDateField.setDisable(true);
            isAdminCheck.setDisable(true);
        }
    }


    public void createUser() throws IOException {
        if (passwordField.getText().equals(repeatPasswordField.getText())) {
            if (!loginField.getText().isEmpty() &&
                    !passwordField.getText().isEmpty() &&
                    !repeatPasswordField.getText().isEmpty() &&
                    !nameField.getText().isEmpty() &&
                    !surnameField.getText().isEmpty() &&
                    !birthDateField.getValue().toString().isEmpty()) {

                customHib = new CustomHib(entityManagerFactory);
                if (customerCheckbox.isSelected()) {
                    if (!addressField.getText().isEmpty() &&
                            !cardNoField.getText().isEmpty()) {
                        User user = new Customer(
                                loginField.getText(),
                                passwordField.getText(),
                                birthDateField.getValue(),
                                nameField.getText(),
                                surnameField.getText(),
                                addressField.getText(),
                                cardNoField.getText());
                        customHib.createUser(user);
                        returnToLogin();
                    } else {
                        JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING,
                                "Register", "Missing data",
                                "Please check if all fields are filled");
                    }
                } else {
                    if (!employeeIdField.getText().isEmpty() &&
                            !medCertificateField.getText().isEmpty() &&
                            !employmentDateField.getValue().toString().isEmpty()) {
                        User user = new Manager(
                                loginField.getText(),
                                passwordField.getText(),
                                birthDateField.getValue(),
                                nameField.getText(),
                                surnameField.getText(),
                                employeeIdField.getId(),
                                medCertificateField.getText(),
                                employmentDateField.getValue(),
                                isAdminCheck.isSelected());
                        customHib.createUser(user);
                        returnToLogin();
                    } else {
                        JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING,
                                "Register", "Missing data",
                                "Please check if all fields are filled");
                    }
                }

            } else {
                JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Register", "Missing data", "Please check if all fields are filled");
            }
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Register", "Password", "Repeated password incorect");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Arba cia kazka su laukais darau
    }

    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("CandyShop");
        stage.setScene(scene);
        stage.show();
    }

    public void customerSelected() {
        addressField.setDisable(false);
        cardNoField.setDisable(false);
        employeeIdField.setDisable(true);
        medCertificateField.setDisable(true);
        employmentDateField.setDisable(true);
        isAdminCheck.setDisable(true);
    }

    public void manegerSelected() {
        addressField.setDisable(true);
        cardNoField.setDisable(true);
        employeeIdField.setDisable(false);
        medCertificateField.setDisable(false);
        employmentDateField.setDisable(false);
        isAdminCheck.setDisable(false);
    }
}
