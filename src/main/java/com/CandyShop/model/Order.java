package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order implements Comparable<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    Customer customer;
    @OneToOne
    Manager manager;
    @OneToOne
    Warehouse warehouse;

    private LocalDate dateCreated;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;

    OrderStatus status;

    @Transient
    boolean showImportance = false;

    public Order(Customer customer, Warehouse warehouse, OrderStatus status) {
        this.customer = customer;
        this.warehouse = warehouse;
        this.status = status;
        dateCreated = LocalDate.now();
    }

    public Order(Manager manager, Warehouse warehouse, OrderStatus status) {
        this.manager = manager;
        this.warehouse = warehouse;
        this.status = status;
        dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        if (showImportance && dateCreated != null) {
            if (ChronoUnit.DAYS.between(dateCreated, LocalDate.now()) > 1) {
                return "Urgent!\n" +
                        "Order id: " + id +
                        " status: " + status;
            }
        }
        return "Order id: " + id +
                " status: " + status;
    }

    @Override
    public int compareTo(Order other) {
        // First, compare based on the manager being null (unassigned orders come first)
        if (this.getManager() == null && other.getManager() != null) {
            return -1;
        } else if (this.getManager() != null && other.getManager() == null) {
            return 1;
        }

        // If both have managers or both are unassigned, compare based on the order age
        boolean thisOldAndUnassigned = this.getManager() == null && ChronoUnit.DAYS.between(this.getDateCreated(), LocalDate.now()) >= 1;
        boolean otherOldAndUnassigned = other.getManager() == null && ChronoUnit.DAYS.between(other.getDateCreated(), LocalDate.now()) >= 1;

        if (thisOldAndUnassigned && !otherOldAndUnassigned) {
            return -1;
        } else if (!thisOldAndUnassigned && otherOldAndUnassigned) {
            return 1;
        }

        // If both or neither are old and unassigned, consider them equal in this context
        return 0;
    }
}
