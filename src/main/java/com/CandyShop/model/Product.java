package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double price;
    String countryOfOrigin;
    double weight;
    String storageConditions;
    @Enumerated
    ProductType category;
    String description;
    String imagePath;
    String ingredients;
    String nutritionalValue;

    public Product(String name, double price, String countryOfOrigin, double weight, String storageConditions, ProductType category, String description, String imagePath, String ingredients, String nutritionalValue) {
        this.name = name;
        this.price = price;
        this.countryOfOrigin = countryOfOrigin;
        this.weight = weight;
        this.storageConditions = storageConditions;
        this.category = category;
        this.description = description;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
        this.nutritionalValue = nutritionalValue;
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Product(String name, String description, String countryOfOrigin) {
        this.name = name;
        this.description = description;
        this.countryOfOrigin = countryOfOrigin;
    }

    @Override
    public String toString() {
        return name;
    }
}
