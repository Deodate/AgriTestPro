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
                .manufacturer(product.getManufacturer())
                .productType(product.getProductType())
                .activeIngredients(product.getActiveIngredients())
                .batchNumber(product.getBatchNumber())
                .imageUrl(product.getImageUrl())
                .dateOfRegistration(product.getDateOfRegistration() != null ? product.getDateOfRegistration().toString() : null)
                .intendedUse(product.getIntendedUse())
                .cropTarget(product.getCropTarget())
                .comments(product.getComments())
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
        product.setManufacturer(productRequest.getManufacturer());
        product.setProductType(productRequest.getProductType());
        product.setActiveIngredients(productRequest.getActiveIngredients());
        product.setDateOfRegistration(productRequest.getDateOfRegistration());
        product.setIntendedUse(productRequest.getIntendedUse());
        product.setCropTarget(productRequest.getCropTarget());
        product.setComments(productRequest.getComments());
        product.setBatchNumber(productRequest.getBatchNumber());
        
        return product;
    }
    
    public void updateEntityFromDto(ProductRequest productRequest, Product product) {
        if (productRequest == null) {
            return;
        }
        
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setManufacturer(productRequest.getManufacturer());
        product.setProductType(productRequest.getProductType());
        product.setActiveIngredients(productRequest.getActiveIngredients());
        product.setDateOfRegistration(productRequest.getDateOfRegistration());
        product.setIntendedUse(productRequest.getIntendedUse());
        product.setCropTarget(productRequest.getCropTarget());
        product.setComments(productRequest.getComments());
        product.setBatchNumber(productRequest.getBatchNumber());
    }
}
