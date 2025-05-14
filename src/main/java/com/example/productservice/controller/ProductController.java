package com.example.productservice.controller;

import com.example.productservice.service.AzureBlobService;
import com.example.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AzureBlobService azureBlobService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProductFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();

            azureBlobService.uploadFile(fileName, file.getInputStream(), file.getSize());

            InputStream blobStream = azureBlobService.downloadFile(fileName);
            productService.processFile(blobStream);

            azureBlobService.archiveFile(fileName);

            return ResponseEntity.ok("File uploaded and processed successfully!");
        } catch (Exception e) {
            log.error("Failed to upload/process file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while processing: " + e.getMessage());
        }
    }

}