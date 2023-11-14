package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;

    @OneToOne
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int amount;

    public Cart(LocalDate dateCreated, Customer customer, Product product) {
        this.dateCreated = dateCreated;
        this.customer = customer;
        this.product = product;
        this.amount = 1;
    }

    public Cart(LocalDate dateCreated, Customer customer, Product product, int amount) {
        this.dateCreated = dateCreated;
        this.customer = customer;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return amount + "\t" + product.getName();
    }

    public void addProduct() {
        this.amount++;
    }
    
}
