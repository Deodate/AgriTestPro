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
public class TestScheduleRequest {
    @NotNull(message = "Test Case ID is required")
    private Long testCaseId;
    
    @NotBlank(message = "Schedule name is required")
    private String scheduleName;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @NotBlank(message = "Frequency is required")
    private String frequency; // DAILY, WEEKLY, BIWEEKLY, MONTHLY
    
    private Integer dayOfWeek; // 1-7 (Monday to Sunday)
    
    private Integer dayOfMonth;

    // Added fields from frontend form
    private String trialPhase;
    private String assignedPersonnel;
    private String location;
    private String testObjective;
    private String equipmentRequired;
    private String notificationPreference;
    private String notes;
}
