package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    private String manufacturer;

    private String productType;

    private String activeIngredients;

    private LocalDate dateOfRegistration;

    private String intendedUse;

    private String cropTarget;

    private String comments;

    private String batchNumber;

    private String registeredBy;

    private Integer quantity;
    private String location;
    private LocalDate expiryDate;

    // Note: Image upload is handled separately as MultipartFile in the controller, not part of this JSON body
}
