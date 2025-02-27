package com.AgriTest.service;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    
    Optional<ProductResponse> getProductById(Long id);
    
    ProductResponse createProduct(ProductRequest productRequest);
    
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    
    void deleteProduct(Long id);
    
    List<ProductResponse> getProductsByCategory(String category);
    
    List<ProductResponse> getProductsByManufacturer(String manufacturer);
    
    List<ProductResponse> searchProductsByName(String name);
}