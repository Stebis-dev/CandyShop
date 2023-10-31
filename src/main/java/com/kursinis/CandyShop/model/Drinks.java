package com.kursinis.CandyShop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Drinks extends Product {

    private double contentVolume;
    private String chemicalContents;


    public Drinks(String title, String description, double contentVolume, String chemicalContents) {
        super(title, description);
        this.contentVolume = contentVolume;
        this.chemicalContents = chemicalContents;

    }

    public Drinks(String title, String description, String manufacturer, Warehouse warehouse, double contentVolume, String chemicalContents) {
        super(title, description, manufacturer, warehouse);
        this.contentVolume = contentVolume;
        this.chemicalContents = chemicalContents;

    }
}
