package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String description;
    String manufacturer;
    byte[] image;
    int price;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Comment> commentList;

    public Product(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Product(String title, String description, String manufacturer) {
        this.title = title;
        this.description = description;
        this.manufacturer = manufacturer;
    }

//    public Product(String title, String description, String manufacturer, Warehouse warehouse) {
//        this.title = title;
//        this.description = description;
//        this.manufacturer = manufacturer;
//        this.warehouse = warehouse;
//    }

    @Override
    public String toString() {
        return title + " " + description;
    }
}
