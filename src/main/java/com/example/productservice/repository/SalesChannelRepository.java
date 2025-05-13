package com.example.productservice.repository;


import com.example.productservice.model.SalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesChannelRepository extends JpaRepository<SalesChannel, Long> {
}

