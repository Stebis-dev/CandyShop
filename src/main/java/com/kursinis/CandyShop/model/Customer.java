package com.kursinis.CandyShop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends User {
    private String address;
    private String cardNo;

    @OneToOne
    Cart cart;

    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo, Cart cart) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "address='" + address + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }


}
