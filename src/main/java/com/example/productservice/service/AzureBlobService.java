package com.example.productservice.service;


import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobService {

    private final BlobContainerClient uploadContainer;
    private final BlobContainerClient archiveContainer;

    public AzureBlobService(@Value("${azure.storage.connection-string}") String connectionString) {
        BlobServiceClient client = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.uploadContainer = client.getBlobContainerClient("uploads");
        this.archiveContainer = client.getBlobContainerClient("archive");
    }

    public void uploadFile(String fileName, InputStream data, long size) {
        BlobClient blobClient = uploadContainer.getBlobClient(fileName);
        blobClient.upload(data, size, true);
    }

    public InputStream downloadFile(String fileName) {
        return uploadContainer.getBlobClient(fileName).openInputStream();
    }

    public void archiveFile(String fileName) {
        BlobClient source = uploadContainer.getBlobClient(fileName);
        BlobClient target = archiveContainer.getBlobClient(fileName);
        target.beginCopy(source.getBlobUrl(), null);
        source.delete();
    }
}
