package com.CandyShop.controllers.login;

import com.CandyShop.StartGui;
import com.CandyShop.controllers.mainShop.MainShopController;
import com.CandyShop.controllers.registration.RegistrationController;
import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.hibernateControllers.GenericHib;
import com.CandyShop.model.User;
import com.CandyShop.utils.JavaFxCustomUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    private EntityManagerFactory entityManagerFactory;
    private CustomHib customHib;


    public void registerNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration/registration.fxml"));
        Parent parent = fxmlLoader.load();
        //Po sios dalies as galiu pasiekti kontrolerius
        RegistrationController registrationController = fxmlLoader.getController();
        GenericHib genericHib = new GenericHib(entityManagerFactory);

        boolean isAdministrator = genericHib.getRecordCount(User.class) == 0;

        registrationController.setData(entityManagerFactory, isAdministrator);

        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("CandyShop");
        stage.setScene(scene);
        stage.show();
    }

    public void validateAndConnect() throws IOException {
        customHib = new CustomHib(entityManagerFactory);
        User user = customHib.getUserByCredentials(loginField.getText(), passwordField.getText());
        //Cia galim optimizuoti, kol kas paliksiu kaip pvz su userHib
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("mainShop/main-shop.fxml"));
            Parent parent = fxmlLoader.load();
            MainShopController mainShopController = fxmlLoader.getController();
            mainShopController.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("CandyShop");
            stage.setScene(scene);
            stage.show();
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.INFORMATION, "login INFO", "Wrong data", "Please check credentials, no such user");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("coursework-shop");
        } catch (Exception e) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.INFORMATION, "Database", "No database found", "Please check your conection");
        }
    }
}
