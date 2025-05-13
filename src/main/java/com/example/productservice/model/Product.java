package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partNumber;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String productType;
    private String parentCatalogGroupID;
    private String manufacturerPartNumber;
    private String manufacturerName;
    private String brandName;
    private String thumbnail;
    private String fullImage;
    private int availableQuantity;
    private String inventoryStatus;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Seo seo;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Price> prices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CategoryAssignment> categoryAssignments;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SalesChannel> salesChannels;

    // Getters and Setters
}