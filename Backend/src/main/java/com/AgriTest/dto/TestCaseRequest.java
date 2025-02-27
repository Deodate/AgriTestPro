package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String methodology;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @NotBlank(message = "Status is required")
    private String status;
}