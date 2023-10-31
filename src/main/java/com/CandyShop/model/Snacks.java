package com.CandyShop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Snacks extends Product {

    private double weight;
    private String chemicalContents;

    public Snacks(String title, String description, String manufacturer, double weight, String chemicalContents) {
        super(title, description, manufacturer);
        this.weight = weight;
        this.chemicalContents = chemicalContents;
    }

    public Snacks(String title, String description, String manufacturer, Warehouse warehouse, double weight, String chemicalContents) {
        super(title, description, manufacturer, warehouse);
        this.weight = weight;
        this.chemicalContents = chemicalContents;
    }
}
