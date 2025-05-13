package com.example.productservice.service;


import com.example.productservice.model.*;
import com.example.productservice.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SeoRepository seoRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private CategoryAssignmentRepository categoryAssignmentRepository;
    @Autowired
    private SalesChannelRepository salesChannelRepository;

    // Process the uploaded JSON file
    public void processFile(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);

        // Extract product details
        String partNumber = rootNode.get("partNumber").asText();
        String name = rootNode.get("name").asText();
        String shortDescription = rootNode.get("shortDescription").asText();
        String longDescription = rootNode.get("longDescription").asText();
        String productType = rootNode.get("productType").asText();
        String parentCatalogGroupID = rootNode.get("parentCatalogGroupID").asText();
        String manufacturerPartNumber = rootNode.get("manufacturerPartNumber").asText();
        String manufacturerName = rootNode.get("manufacturerName").asText();
        String brandName = rootNode.get("brandName").asText();
        String thumbnail = rootNode.get("thumbnail").asText();
        String fullImage = rootNode.get("fullImage").asText();

        // Create Product entity
        Product product = new Product();
        product.setPartNumber(partNumber);
        product.setName(name);
        product.setShortDescription(shortDescription);
        product.setLongDescription(longDescription);
        product.setProductType(productType);
        product.setParentCatalogGroupID(parentCatalogGroupID);
        product.setManufacturerPartNumber(manufacturerPartNumber);
        product.setManufacturerName(manufacturerName);
        product.setBrandName(brandName);
        product.setThumbnail(thumbnail);
        product.setFullImage(fullImage);

        // Save Product into the database
        productRepository.save(product);

        // Extract SEO data
        JsonNode seoNode = rootNode.get("seo");
        Seo seo = new Seo();
        seo.setProduct(product);
        seo.setTitle(seoNode.get("title").asText());
        seo.setKeywords(seoNode.get("keywords").asText());
        seo.setDescription(seoNode.get("description").asText());
        seoRepository.save(seo);

        // Extract Prices
        JsonNode pricesNode = rootNode.get("price");
        for (JsonNode priceNode : pricesNode) {
            Price price = new Price();
            price.setProduct(product);
            price.setPrice(priceNode.get("price").asDouble());
            price.setCurrency(priceNode.get("currency").asText());
            price.setPriceMode(priceNode.get("priceMode").asText());
            price.setPriceType(priceNode.get("priceType").asText());
            price.setStoreID(priceNode.get("storeID").asInt());
            priceRepository.save(price);
        }

        // Extract Attributes
        JsonNode attributesNode = rootNode.get("attributes");
        for (JsonNode attributeNode : attributesNode) {
            Attribute attribute = new Attribute();
            attribute.setProduct(product);
            attribute.setAttributeName(attributeNode.get("attributeName").asText());
            attribute.setAttributeValue(attributeNode.get("attributeValue").asText());
            attributeRepository.save(attribute);
        }

        // Extract Availability
        JsonNode availabilityNode = rootNode.get("availability");
        product.setAvailableQuantity(availabilityNode.get("availableQuantity").asInt());
        product.setInventoryStatus(availabilityNode.get("inventoryStatus").asText());
        productRepository.save(product);

        // Extract Category Assignments
        JsonNode categoryAssignmentsNode = rootNode.get("categoryAssignments");
        for (JsonNode categoryNode : categoryAssignmentsNode) {
            CategoryAssignment categoryAssignment = new CategoryAssignment();
            categoryAssignment.setProduct(product);
            categoryAssignment.setCatalogGroupID(categoryNode.get("catalogGroupID").asText());
            categoryAssignment.setSequence(categoryNode.get("sequence").asInt());
            categoryAssignmentRepository.save(categoryAssignment);
        }

        // Extract Sales Channels
        JsonNode salesChannelsNode = rootNode.get("salesChannels");
        for (JsonNode salesChannelNode : salesChannelsNode) {
            SalesChannel salesChannel = new SalesChannel();
            salesChannel.setProduct(product);
            salesChannel.setSalesChannel(salesChannelNode.get("salesChannel").asText());
            salesChannel.setSequence(salesChannelNode.get("sequence").asInt());
            salesChannelRepository.save(salesChannel);
        }
    }

    // Archive the processed file
    public void archiveFile(File file) {
        // Move the processed file to an archive folder (optional implementation)
        File archiveDir = new File("archived_files/");
        if (!archiveDir.exists()) {
            archiveDir.mkdirs();
        }

        file.renameTo(new File(archiveDir, file.getName()));
    }
}