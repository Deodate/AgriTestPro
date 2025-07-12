package com.AgriTest.service.impl;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Product;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.Year;
import java.text.DecimalFormat;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

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
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile productImage) {
        logger.info("Attempting to create product with batch number: {}", productRequest.getBatchNumber());

        String batchNumber = productRequest.getBatchNumber();

        Product product = mapProductRequestToProduct(productRequest); // Map first

        // Now, handle the batch number logic based on the original request value
        if (batchNumber != null) {
            String trimmedBatchNumber = batchNumber.trim();

            if ("None".equals(trimmedBatchNumber)) {
                // User specifically chose "None", set the batch number to null in the database
                product.setBatchNumber(null);
                logger.info("Received 'None' for batch number, setting product batch number to null.");
                // No format validation or duplicate check needed for the literal string "None"
            } else if (trimmedBatchNumber.isEmpty()) {
                // Input is empty or only whitespace, treat as null
                product.setBatchNumber(null); // Set to null
                logger.info("Received empty string for batch number, setting product batch number to null.");
                // No format validation or duplicate check needed for null
            } else {
                // Received a non-empty, non-"None" string, perform validation and duplicate check
                // The batch number was already set by mapProductRequestToProduct, now validate and check duplicates
                if (!trimmedBatchNumber.matches("\\d{4}PRO\\d{4}")) {
                    logger.warn("Invalid batch number format received: {}", batchNumber);
                    throw new IllegalArgumentException("Invalid batch number format. Expected format: 0001PRO2025");
                }

                // Check if a product with the same batch number already exists
                Optional<Product> existingProduct = productRepository.findByBatchNumber(trimmedBatchNumber);
                logger.info("Checking for existing product with batch number {}. Found: {}", trimmedBatchNumber, existingProduct.isPresent());
                if (existingProduct.isPresent()) {
                    throw new IllegalArgumentException("Product with batch number " + trimmedBatchNumber + " already exists.");
                }

                // The batch number is valid and not a duplicate, keep the value set by mapProductRequestToProduct
                // product.setBatchNumber(trimmedBatchNumber); // This is already done by mapProductRequestToProduct
                 logger.info("Batch number {} is valid and not a duplicate.", trimmedBatchNumber);
            }
        } else {
             // Original batchNumber was null (shouldn't happen if frontend sends "None" or empty), ensure product batch number is null
             product.setBatchNumber(null);
             logger.info("Original batch number was null, setting product batch number to null.");
        }

        // Handle image upload
        if (productImage != null && !productImage.isEmpty()) {
            try {
                // Normalize file name
                String fileName = StringUtils.cleanPath(productImage.getOriginalFilename());
                // Generate unique file name to prevent overwriting
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(uniqueFileName);

                // Create the upload directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Copy file to the target location
                Files.copy(productImage.getInputStream(), filePath);

                // Set the image URL in the product entity
                product.setImageUrl("/uploads/agritestpro/" + uniqueFileName); // Assuming /uploads/agritestpro is the web-accessible path

            } catch (IOException ex) {
                throw new RuntimeException("Failed to store file", ex);
            }
        }

        Product savedProduct = productRepository.save(product);
        logger.info("Product successfully saved to database with ID: {}", savedProduct.getId());
        return mapProductToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest, MultipartFile productImage) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        // Update existing product fields
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setManufacturer(productRequest.getManufacturer());
        existingProduct.setProductType(productRequest.getProductType());
        existingProduct.setActiveIngredients(productRequest.getActiveIngredients());
        existingProduct.setDateOfRegistration(productRequest.getDateOfRegistration());
        existingProduct.setIntendedUse(productRequest.getIntendedUse());
        existingProduct.setCropTarget(productRequest.getCropTarget());
        existingProduct.setComments(productRequest.getComments());
        // Update new fields
        existingProduct.setStockQuantity(productRequest.getQuantity() != null ? productRequest.getQuantity() : existingProduct.getStockQuantity());
        existingProduct.setLocation(productRequest.getLocation());
        existingProduct.setExpiryDate(productRequest.getExpiryDate());

        // Update the createdBy (Registered By) field from the request
        if (productRequest.getRegisteredBy() != null && !productRequest.getRegisteredBy().isEmpty()) {
            try {
                Long registeredById = Long.valueOf(productRequest.getRegisteredBy());
                existingProduct.setCreatedBy(registeredById);
            } catch (NumberFormatException e) {
                // Log or handle the case where registeredBy is not a valid number
                logger.error("Invalid number format for Registered By ID: {}", productRequest.getRegisteredBy(), e);
                // Optionally throw an exception or set createdBy to null
                existingProduct.setCreatedBy(null); // Set to null if invalid
            }
        } else {
            existingProduct.setCreatedBy(null); // Set to null if registeredBy is null or empty
        }

        // Handle image update if a new image is provided
        if (productImage != null && !productImage.isEmpty()) {
            try {
                // Delete the old image if it exists
                if (existingProduct.getImageUrl() != null) {
                    Path oldImagePath = Paths.get(uploadDir).resolve(existingProduct.getImageUrl().replace("/uploads/agritestpro/", ""));
                    if (Files.exists(oldImagePath)) {
                        Files.delete(oldImagePath);
                        logger.info("Deleted old image file: {}", oldImagePath);
                    }
                }
                
                // Save the new image
                String fileName = StringUtils.cleanPath(productImage.getOriginalFilename());
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(uniqueFileName);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(productImage.getInputStream(), filePath);
                existingProduct.setImageUrl("/uploads/agritestpro/" + uniqueFileName);
                logger.info("Updated image URL to: {}", existingProduct.getImageUrl());

            } catch (IOException ex) {
                logger.error("Failed to store new image file for product ID: {}", id, ex);
                // Depending on requirements, you might want to throw an exception or continue without updating the image
                // For now, we log the error and proceed without updating the image URL
            }
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return mapProductToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
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

    @Override
    public String getNextBatchNumber() {
        logger.info("Attempting to get the next batch number...");

        // Fetch all products with non-null batch numbers
        List<Product> productsWithBatchNumbers = productRepository.findByBatchNumberNotNull();
        
        String latestFormattedBatchNumber = null;
        int nextNumber = 1;
        String currentYear = String.valueOf(Year.now().getValue());
        logger.info("Current year: {}", currentYear);

        if (productsWithBatchNumbers != null && !productsWithBatchNumbers.isEmpty()) {
            // Filter for batch numbers matching the NNNNPROYYYY format and find the latest
            latestFormattedBatchNumber = productsWithBatchNumbers.stream()
                .map(Product::getBatchNumber)
                .filter(batchNumber -> batchNumber != null && batchNumber.matches("\\d{4}PRO\\d{4}"))
                .max(String::compareTo) // Find the lexicographically largest (which works for this format)
                .orElse(null); // If no matching format found, latest is null
                
            logger.info("Latest formatted batch number found in DB: {}", latestFormattedBatchNumber);

            if (latestFormattedBatchNumber != null) {
                String[] parts = latestFormattedBatchNumber.split("PRO");
                if (parts.length == 2) {
                    try {
                        int lastNumber = Integer.parseInt(parts[0]);
                        String yearPart = parts[1];
                        logger.info("Parsed number part: {}, Parsed year part: {}", lastNumber, yearPart);

                        // Check if the year part matches the current year
                        if (yearPart.equals(currentYear)) {
                            nextNumber = lastNumber + 1;
                            logger.info("Year matches, next number calculated: {}", nextNumber);
                        } else {
                            // If year doesn't match, reset number to 1 for the new year
                            nextNumber = 1;
                            logger.info("Year mismatch, resetting next number to 1.");
                        }
                    } catch (NumberFormatException e) {
                        // If parsing fails, start from 1 (this case should be rare with regex filter)
                        nextNumber = 1;
                        logger.error("NumberFormatException while parsing batch number {}: {}", latestFormattedBatchNumber, e.getMessage());
                        logger.info("Parsing failed, resetting next number to 1.");
                    }
                }
            }
        } else {
            logger.info("No products with non-null batch numbers found in DB. Starting next number from 1.");
            nextNumber = 1;
        }

        // Format the number with leading zeros (e.g., 0001)
        DecimalFormat formatter = new DecimalFormat("0000");
        String formattedNumber = formatter.format(nextNumber);
        logger.info("Formatted next number: {}", formattedNumber);

        String nextBatchNumber = formattedNumber + "PRO" + currentYear;
        logger.info("Generated next batch number: {}", nextBatchNumber);
        return nextBatchNumber;
    }
    
    @Override
    public Resource getImage(String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    
    private Product mapProductRequestToProduct(ProductRequest productRequest) {
        // For creation, ID is not set here, it's generated by the database
        // For update, ID comes from the path variable in the controller
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .manufacturer(productRequest.getManufacturer())
                .productType(productRequest.getProductType())
                .activeIngredients(productRequest.getActiveIngredients())
                .dateOfRegistration(productRequest.getDateOfRegistration())
                .intendedUse(productRequest.getIntendedUse())
                .cropTarget(productRequest.getCropTarget())
                .comments(productRequest.getComments())
                .batchNumber(productRequest.getBatchNumber()) // Set batch number from request (or it will be generated)
                .createdBy(productRequest.getRegisteredBy() != null ? Long.valueOf(productRequest.getRegisteredBy()) : null) // Convert registeredBy (String) to createdBy (Long), handle null
                .stockQuantity(productRequest.getQuantity() != null ? productRequest.getQuantity() : 0) // Map quantity from request
                .location(productRequest.getLocation()) // Map location from request
                .expiryDate(productRequest.getExpiryDate()) // Map expiryDate from request
                // Image URL is set after file upload
                .build();
    }
    
    private ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .productType(product.getProductType())
                .activeIngredients(product.getActiveIngredients())
                .dateOfRegistration(product.getDateOfRegistration() != null ? product.getDateOfRegistration().toString() : null)
                .intendedUse(product.getIntendedUse())
                .cropTarget(product.getCropTarget())
                .comments(product.getComments())
                .imageUrl(product.getImageUrl())
                .batchNumber(product.getBatchNumber())
                .registeredBy(product.getCreatedBy() != null ? product.getCreatedBy().toString() : null) // Map createdBy (Long) to registeredBy (String) for response
                .stockQuantity(product.getStockQuantity()) // Include stock quantity
                .location(product.getLocation()) // Include location
                .expiryDate(product.getExpiryDate() != null ? product.getExpiryDate().toString() : null) // Include expiry date
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Override
    public List<ProductResponse> getProductsByType(String productType) {
        return productRepository.findByProductType(productType).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductTestedStatus(String batchNumber, boolean tested) {
        logger.info("Attempting to update tested status for product with batch number: {}", batchNumber);
        Optional<Product> productOptional = productRepository.findByBatchNumber(batchNumber);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            logger.info("Product found with ID: {} and current tested status: {}", product.getId(), product.isTested());
            product.setTested(tested);
            logger.info("Setting tested status to: {}", tested);
            productRepository.save(product);
            logger.info("Product with ID {} tested status updated successfully.", product.getId());
        } else {
            logger.warn("Product with batch number {} not found, cannot update tested status.", batchNumber);
        }
    }
}