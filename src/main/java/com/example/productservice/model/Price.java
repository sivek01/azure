package com.example.productservice.model;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double price;
    private String currency;
    private String priceMode;
    private String priceType;
    private int storeID;

    // Getters and Setters
}

