package com.CandyShop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Candy extends Product {

    private double weight;
    private String chemicalContents;

    public Candy(String title, String description, String manufacturer, double weight, String chemicalContents) {
        super(title, description, manufacturer);
        this.weight = weight;
    }


}
