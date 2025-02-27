package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponse {
    private Long id;
    private ProductResponse product;
    private String title;
    private String description;
    private String methodology;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TestPhaseResponse> phases;
}