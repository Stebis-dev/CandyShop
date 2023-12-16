package com.CandyShop.controllers.mainShop.tableViewGuidelines;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Customer;
import com.CandyShop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CustomerTableGuidelines {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleStringProperty birthdate;
    private SimpleStringProperty address;
    private SimpleStringProperty cardNumber;

    private CustomHib customHib;

    public CustomerTableGuidelines(Customer customer) {
        this.id = new SimpleStringProperty(Integer.toString(customer.getId()) != null ? Integer.toString(customer.getId()) : "");
        this.name = new SimpleStringProperty(customer.getName() != null ? customer.getName() : "");
        this.surname = new SimpleStringProperty(customer.getSurname() != null ? customer.getSurname() : "");
        this.login = new SimpleStringProperty(customer.getLogin() != null ? customer.getLogin() : "");
        this.password = new SimpleStringProperty(customer.getPassword() != null ? customer.getPassword() : "");
        this.birthdate = new SimpleStringProperty(customer.getBirthDate() != null ? customer.getBirthDate().toString() : "");
        this.address = new SimpleStringProperty(customer.getAddress() != null ? customer.getAddress() : "");
        this.cardNumber = new SimpleStringProperty(customer.getCardNo() != null ? customer.getCardNo() : "");
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getLogin() {
        return login.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getBirthdate() {
        return birthdate.get();
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate.set(birthdate.toString());
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCardNumber() {
        return cardNumber.get();
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public Customer toCustomer(CustomHib customHib) {

        Customer customer = customHib.getEntityById(Customer.class, Integer.parseInt(getId()));
        customer.setName(getName());
        customer.setSurname(getSurname());
        customer.setLogin(getLogin());
        customer.setPassword(getPassword());
        customer.setBirthDate(LocalDate.parse(getBirthdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        customer.setAddress(getAddress());
        customer.setCardNo(getCardNumber());
        return customer;
    }
}
