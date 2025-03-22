package com.AgriTest.repository;

import com.AgriTest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    
    List<Product> findByManufacturer(String manufacturer);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
}