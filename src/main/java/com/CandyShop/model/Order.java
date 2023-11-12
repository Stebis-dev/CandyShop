package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    Customer customer;

    @OneToOne
    Warehouse warehouse;

    String status;

    public Order(Customer customer, Warehouse warehouse, String status) {
        this.customer = customer;
        this.warehouse = warehouse;
        this.status = status;
    }
}