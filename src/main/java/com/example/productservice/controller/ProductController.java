package com.example.productservice.controller;

import com.example.productservice.service.AzureBlobService;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AzureBlobService azureBlobService;

    @PostMapping("/upload")
    public String uploadProductFile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            // Upload to Azure Blob
            azureBlobService.uploadFile(fileName, file.getInputStream(), file.getSize());

            // Read back from Azure Blob and process
            InputStream blobStream = azureBlobService.downloadFile(fileName);
            productService.processFile(blobStream);

            // Archive file
            azureBlobService.archiveFile(fileName);

            return "File uploaded and processed successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while processing: " + e.getMessage();
        }
    }

}