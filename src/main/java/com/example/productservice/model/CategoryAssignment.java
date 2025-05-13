package com.example.productservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CategoryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String catalogGroupID;
    private int sequence;

    // Getters and Setters
}
