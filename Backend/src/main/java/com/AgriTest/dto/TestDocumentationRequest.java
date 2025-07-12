package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TestDocumentationRequest {
    @NotBlank(message = "Test name is required")
    private String testName;

    @NotBlank(message = "Test type is required")
    private String testType;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Test procedure is required")
    private String testProcedure;

    @NotBlank(message = "Expected results are required")
    private String expectedResults;

    private String actualResults;

    private String testStatus;
} 