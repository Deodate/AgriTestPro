
// File: src/main/java/com/AgriTest/mapper/ProductMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;
import com.AgriTest.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .manufacturer(product.getManufacturer())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    
    public List<ProductResponse> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Product toEntity(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }
        
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setManufacturer(productRequest.getManufacturer());
        
        return product;
    }
    
    public void updateEntityFromDto(ProductRequest productRequest, Product product) {
        if (productRequest == null) {
            return;
        }
        
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setManufacturer(productRequest.getManufacturer());
    }
}
