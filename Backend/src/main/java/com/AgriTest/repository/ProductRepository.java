package com.AgriTest.repository;

import com.AgriTest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByManufacturer(String manufacturer);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    Optional<Product> findByBatchNumber(String batchNumber);

    List<Product> findByName(String name);

    Optional<Product> findTopByOrderByBatchNumberDesc();

    List<Product> findByBatchNumberNotNull();

    List<Product> findByProductType(String productType);
}