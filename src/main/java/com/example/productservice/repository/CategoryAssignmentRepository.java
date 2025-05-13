package com.example.productservice.repository;


import com.example.productservice.model.CategoryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAssignmentRepository extends JpaRepository<CategoryAssignment, Long> {
}
