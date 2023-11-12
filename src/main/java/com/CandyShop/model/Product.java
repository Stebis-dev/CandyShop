package com.CandyShop.model;

import jakarta.persistence.*;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.file.Files;
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
    int price;

    String imagePath;

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
    
    @Override
    public String toString() {
        return title + " " + description;
    }
}
