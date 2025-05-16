package com.example.productservice.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class AzureBlobService {

    private final BlobContainerClient uploadContainer;

    public AzureBlobService(
            @Value("${azure.storage.account-name}") String accountName,
            @Value("${azure.storage.account-key}") String accountKey,
            @Value("${azure.storage.blob-container-name}") String uploadContainerName
    ) {
        String connectionString = String.format(
                "DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net",
                accountName, accountKey
        );

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.uploadContainer = blobServiceClient.getBlobContainerClient(uploadContainerName);

        // Create container if it doesn't exist
        if (!uploadContainer.exists()) {
            uploadContainer.create();
            log.info("Created upload container: {}", uploadContainer.getBlobContainerName());
        }
    }

    public void uploadFile(String fileName, InputStream data, long size) {
        try {
            BlobClient blobClient = uploadContainer.getBlobClient(fileName);
            blobClient.upload(data, size, true);
            log.info("Uploaded file '{}' to container '{}'", fileName, uploadContainer.getBlobContainerName());
        } catch (Exception e) {
            log.error("Failed to upload file: {}", fileName, e);
            throw new RuntimeException("Upload failed", e);
        }
    }

    public InputStream downloadFile(String fileName) {
        try {
            BlobClient blobClient = uploadContainer.getBlobClient(fileName);
            log.info("Downloading file '{}' from container '{}'", fileName, uploadContainer.getBlobContainerName());
            return blobClient.openInputStream();
        } catch (Exception e) {
            log.error("Failed to download file: {}", fileName, e);
            throw new RuntimeException("Download failed", e);
        }
    }

    public void archiveFile(String fileName) {
        // No-op — skip archiving; file remains in upload container
        log.info("Archive skipped: '{}' retained in upload container '{}'", fileName, uploadContainer.getBlobContainerName());
    }
}
