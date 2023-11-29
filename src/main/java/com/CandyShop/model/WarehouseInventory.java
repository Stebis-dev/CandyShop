package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Warehouse_Inventory")
public class WarehouseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Warehouse warehouse;

    @OneToOne
    private Product product;

    private int amount;

    public WarehouseInventory(Warehouse warehouse, Product product, int amount) {
        this.warehouse = warehouse;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return amount + "\t" + product.getName();
    }
}
