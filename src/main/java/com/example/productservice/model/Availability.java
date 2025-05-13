package com.example.productservice.model;



import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Availability {

    private int availableQuantity;
    private String inventoryStatus;
}

