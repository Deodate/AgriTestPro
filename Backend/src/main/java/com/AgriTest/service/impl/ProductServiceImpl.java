package com.AgriTest.service.impl;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Product;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapProductToProductResponse);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapProductRequestToProduct(productRequest);
        Product savedProduct = productRepository.save(product);
        return mapProductToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setManufacturer(productRequest.getManufacturer());
        
        Product updatedProduct = productRepository.save(existingProduct);
        return mapProductToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }
    
    private Product mapProductRequestToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setManufacturer(productRequest.getManufacturer());
        return product;
    }
    
    private ProductResponse mapProductToProductResponse(Product product) {
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
}