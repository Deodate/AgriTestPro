package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestScheduleResponse {
    private Long id;
    private Long testCaseId;
    private String testCaseTitle;
    private String scheduleName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;
    private Integer dayOfWeek;
    private Integer dayOfMonth;
    private LocalDate nextExecution;
    private Boolean isActive;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}