package com.CandyShop.controllers.mainshop.tableViewGuidelines;

import com.CandyShop.model.Customer;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerTableGuidelines {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty birthdate;
    private SimpleStringProperty address;
    private SimpleStringProperty cardNumber;

    public CustomerTableGuidelines(Customer customer) {
        this.name = new SimpleStringProperty(customer.getName() != null ? customer.getName() : "");
        this.surname = new SimpleStringProperty(customer.getSurname() != null ? customer.getSurname() : "");
        this.birthdate = new SimpleStringProperty(customer.getBirthDate() != null ? customer.getBirthDate().toString() : "");
        this.address = new SimpleStringProperty(customer.getAddress() != null ? customer.getAddress() : "");
        this.cardNumber = new SimpleStringProperty(customer.getCardNo() != null ? customer.getCardNo() : "");

    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name == null ? "" : name.get();
    }

    public String getSurname() {
        return surname == null ? "" : surname.get();
    }

    public String getBirthdate() {
        return birthdate == null ? "" : birthdate.get();
    }

    public String getAddress() {
        return address == null ? "" : address.get();
    }

    public String getCardNumber() {
        return cardNumber == null ? "" : cardNumber.get();
    }

}
