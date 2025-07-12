package com.AgriTest.service;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    
    Optional<ProductResponse> getProductById(Long id);
    
    ProductResponse createProduct(ProductRequest productRequest, MultipartFile productImage);
    
    ProductResponse updateProduct(Long id, ProductRequest productRequest, MultipartFile productImage);
    
    void deleteProduct(Long id);
    
    List<ProductResponse> getProductsByManufacturer(String manufacturer);
    
    List<ProductResponse> searchProductsByName(String name);

    List<ProductResponse> getProductsByType(String productType);

    String getNextBatchNumber();

    // Method to get image as a resource
    Resource getImage(String filename);

    void updateProductTestedStatus(String batchNumber, boolean tested);
}