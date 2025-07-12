package com.AgriTest.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TestDocumentationResponse {
    private Long id;
    private String testName;
    private String testType;
    private String description;
    private String testProcedure;
    private String expectedResults;
    private String actualResults;
    private String testStatus;
    private String attachments;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 