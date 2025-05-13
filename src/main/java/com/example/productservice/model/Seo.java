package com.example.productservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String title;
    private String keywords;
    private String description;

    // Getters and Setters
}



