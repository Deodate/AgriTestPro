package com.AgriTest.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String manufacturer;
    private String productType;
    private String activeIngredients;
    private String batchNumber;
    private String imageUrl;
    private String dateOfRegistration;
    private String intendedUse;
    private String cropTarget;
    private String comments;
    private String registeredBy;
    private Integer stockQuantity;
    private String location;
    private String expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}