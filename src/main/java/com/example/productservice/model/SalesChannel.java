package com.example.productservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SalesChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String salesChannel;
    private int sequence;

    // Getters and Setters
}
