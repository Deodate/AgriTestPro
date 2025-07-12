// File: src/main/java/com/AgriTest/controller/ProductController.java
package com.AgriTest.controller;

import com.AgriTest.dto.ProductRequest;
import com.AgriTest.dto.ProductResponse;
import com.AgriTest.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import com.AgriTest.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<?> createProduct(
            @ModelAttribute @Valid ProductRequest productRequest,
            @RequestParam("productImage") MultipartFile productImage) {
        try {
            // Pass both the request DTO and the image file to the service
            ProductResponse createdProduct = productService.createProduct(productRequest, productImage);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            // Catch the exception for duplicate batch number or other illegal arguments
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions (e.g., file upload issues)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                            @ModelAttribute @Valid ProductRequest productRequest,
                                            @RequestParam(value = "productImage", required = false) MultipartFile productImage) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, productRequest, productImage);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public List<ProductResponse> getProductsByManufacturer(@PathVariable String manufacturer) {
        return productService.getProductsByManufacturer(manufacturer);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public List<ProductResponse> searchProductsByName(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    @GetMapping("/type")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<ProductResponse>> getProductsByType(@RequestParam String productType) {
        return ResponseEntity.ok(productService.getProductsByType(productType));
    }

    @GetMapping("/next-batch-number")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')") // Adjust roles as needed
    public ResponseEntity<String> getNextBatchNumber() {
        String nextBatchNumber = productService.getNextBatchNumber();
        return ResponseEntity.ok(nextBatchNumber);
    }

    @GetMapping("/images/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<Resource> getProductImage(@PathVariable String filename) {
        Resource file = productService.getImage(filename);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PutMapping("/{batchNumber}/tested")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> updateProductTestedStatus(@PathVariable String batchNumber, @RequestBody boolean tested) {
        productService.updateProductTestedStatus(batchNumber, tested);
        return ResponseEntity.ok().build();
    }
}