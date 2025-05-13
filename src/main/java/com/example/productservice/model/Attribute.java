package com.example.productservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String attributeName;
    private String attributeValue;

    // Getters and Setters
}
