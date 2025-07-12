package com.AgriTest.dto;

import com.AgriTest.model.ScheduleFrequency;
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
    @NotBlank(message = "Test name is required")
    private String testName;

    @NotBlank(message = "Schedule name is required")
    private String scheduleName;

    private String trialPhase;

    @NotBlank(message = "Assigned personnel is required")
    private String assignedPersonnel;

    private String location;

    private String testObjective;

    private String equipmentRequired;

    private String notificationPreference;

    private String notes;

    @NotNull(message = "Frequency is required")
    private String frequency;

    private Integer dayOfMonth;

    private Integer dayOfWeek;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    @NotNull(message = "Test case ID is required")
    private Long testCaseId;

    private String description;

    private String priority;

    private String status;

    private Long createdBy;
}
