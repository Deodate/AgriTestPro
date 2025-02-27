// File: src/main/java/com/AgriTest/dto/TestResultRequest.java
package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultRequest {
    @NotBlank(message = "Parameter name is required")
    private String parameterName;
    
    @NotBlank(message = "Value is required")
    private String value;
    
    private String unit;
    private String notes;
}