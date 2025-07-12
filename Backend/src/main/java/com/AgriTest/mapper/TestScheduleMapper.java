package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.ScheduleFrequency;
import com.AgriTest.repository.TestCaseRepository;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between TestSchedule entities and DTOs.
 * Handles the conversion of test schedule data while maintaining relationships
 * and calculating next execution dates.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TestScheduleMapper {

    private final TestCaseRepository testCaseRepository;

    /**
     * Converts a TestScheduleRequest DTO to a TestSchedule entity.
     * @param request The DTO containing test schedule data
     * @return TestSchedule entity with populated data
     * @throws EntityNotFoundException if referenced TestCase is not found
     * @throws IllegalArgumentException if request is null or contains invalid data
     */
    public TestSchedule toEntity(TestScheduleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("TestScheduleRequest cannot be null");
        }
        
        validateRequest(request);
        
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setTestName(request.getTestName());
        testSchedule.setScheduleName(request.getScheduleName());
        testSchedule.setTrialPhase(request.getTrialPhase());
        testSchedule.setAssignedPersonnel(request.getAssignedPersonnel());
        testSchedule.setLocation(request.getLocation());
        testSchedule.setTestObjective(request.getTestObjective());
        testSchedule.setEquipmentRequired(request.getEquipmentRequired());
        testSchedule.setNotificationPreference(request.getNotificationPreference());
        testSchedule.setNotes(request.getNotes());
        
        // Handle frequency conversion with better error message
        try {
            testSchedule.setFrequency(ScheduleFrequency.valueOf(request.getFrequency().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                "Invalid frequency '%s'. Valid values are: %s",
                request.getFrequency(),
                String.join(", ", List.of(ScheduleFrequency.values()).stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()))
            ));
        }
        
        testSchedule.setDayOfMonth(request.getDayOfMonth());
        testSchedule.setDayOfWeek(request.getDayOfWeek());
        
        // Validate schedule configuration
        validateScheduleConfiguration(testSchedule);
        
        // Validate dates
        validateDates(request);
        
        testSchedule.setStartDate(request.getStartDate());
        testSchedule.setEndDate(request.getEndDate());
        testSchedule.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        testSchedule.setDescription(request.getDescription());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setCreatedBy(request.getCreatedBy());

        // Handle test case relationship with better error handling
        if (request.getTestCaseId() != null) {
            TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new EntityNotFoundException(
                    String.format("TestCase with ID %d not found", request.getTestCaseId())));
            testSchedule.setTestCase(testCase);
        }

        // Calculate next execution date
        calculateNextExecution(testSchedule);
        
        return testSchedule;
    }

    /**
     * Validates the test schedule request
     * @param request The request to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRequest(TestScheduleRequest request) {
        if (request.getTestName() == null || request.getTestName().trim().isEmpty()) {
            throw new IllegalArgumentException("Test name is required");
        }
        if (request.getScheduleName() == null || request.getScheduleName().trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule name is required");
        }
        if (request.getFrequency() == null || request.getFrequency().trim().isEmpty()) {
            throw new IllegalArgumentException("Frequency is required");
        }
        if (request.getPriority() != null && 
            !List.of("LOW", "MEDIUM", "HIGH", "CRITICAL").contains(request.getPriority().toUpperCase())) {
            throw new IllegalArgumentException("Invalid priority value. Must be one of: LOW, MEDIUM, HIGH, CRITICAL");
        }
        if (request.getStatus() != null && 
            !List.of("PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED").contains(request.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Invalid status value. Must be one of: PENDING, IN_PROGRESS, COMPLETED, CANCELLED");
        }
    }

    /**
     * Validates the dates in the request
     * @param request The request containing dates to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateDates(TestScheduleRequest request) {
        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                String.format("End date (%s) cannot be before start date (%s)",
                    request.getEndDate(), request.getStartDate()));
        }
        
        if (request.getStartDate().isBefore(LocalDate.now())) {
            log.warn("Start date {} is in the past", request.getStartDate());
        }
    }

    /**
     * Validates schedule configuration based on frequency.
     * @param testSchedule The schedule to validate
     * @throws IllegalArgumentException if configuration is invalid
     */
    private void validateScheduleConfiguration(TestSchedule testSchedule) {
        switch (testSchedule.getFrequency()) {
            case WEEKLY:
                if (testSchedule.getDayOfWeek() != null && 
                    (testSchedule.getDayOfWeek() < 1 || testSchedule.getDayOfWeek() > 7)) {
                    throw new IllegalArgumentException(
                        "Day of week must be between 1 (Monday) and 7 (Sunday)");
                }
                break;
            case MONTHLY:
                if (testSchedule.getDayOfMonth() != null && 
                    (testSchedule.getDayOfMonth() < 1 || testSchedule.getDayOfMonth() > 31)) {
                    throw new IllegalArgumentException(
                        "Day of month must be between 1 and 31");
                }
                break;
            default:
                // No validation needed for DAILY frequency
                break;
        }
    }

    /**
     * Converts a TestSchedule entity to a TestScheduleResponse DTO.
     * @param testSchedule The entity to convert
     * @return TestScheduleResponse DTO with populated data
     */
    public TestScheduleResponse toResponse(TestSchedule testSchedule) {
        if (testSchedule == null) {
            return null;
        }

        return TestScheduleResponse.builder()
            .id(testSchedule.getId())
            .testCaseId(testSchedule.getTestCase() != null ? testSchedule.getTestCase().getId() : null)
            .testCaseTitle(testSchedule.getTestCase() != null ? testSchedule.getTestCase().getTestName() : null)
            .testName(testSchedule.getTestName())
            .scheduleName(testSchedule.getScheduleName())
            .startDate(testSchedule.getStartDate())
            .trialPhase(testSchedule.getTrialPhase())
            .assignedPersonnel(testSchedule.getAssignedPersonnel())
            .endDate(testSchedule.getEndDate())
            .frequency(testSchedule.getFrequency() != null ? testSchedule.getFrequency().name() : null)
            .location(testSchedule.getLocation())
            .testObjective(testSchedule.getTestObjective())
            .equipmentRequired(testSchedule.getEquipmentRequired())
            .notificationPreference(testSchedule.getNotificationPreference())
            .notes(testSchedule.getNotes())
            .dayOfWeek(testSchedule.getDayOfWeek())
            .dayOfMonth(testSchedule.getDayOfMonth())
            .nextExecution(testSchedule.getNextExecution())
            .isActive(testSchedule.isActive())
            .createdBy(testSchedule.getCreatedBy())
            .createdAt(testSchedule.getCreatedAt())
            .updatedAt(testSchedule.getUpdatedAt())
            .priority(testSchedule.getPriority())
            .status(testSchedule.getStatus())
            .build();
    }

    /**
     * Converts a list of TestScheduleRequest DTOs to TestSchedule entities.
     * @param requests List of DTOs to convert
     * @return List of converted TestSchedule entities
     */
    public List<TestSchedule> toEntityList(List<TestScheduleRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of TestSchedule entities to TestScheduleResponse DTOs.
     * @param schedules List of entities to convert
     * @return List of converted TestScheduleResponse DTOs
     */
    public List<TestScheduleResponse> toResponseList(List<TestSchedule> schedules) {
        if (schedules == null) {
            return List.of();
        }
        return schedules.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the next execution date based on schedule frequency and current date.
     * @param testSchedule The test schedule to calculate next execution for
     */
    private void calculateNextExecution(TestSchedule testSchedule) {
        if (!testSchedule.isActive() || testSchedule.getStartDate() == null) {
            testSchedule.setNextExecution(null);
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate nextExecution = testSchedule.getStartDate();

        // If start date is in the future, use it as next execution
        if (nextExecution.isAfter(currentDate)) {
            testSchedule.setNextExecution(nextExecution);
            return;
        }

        // Calculate next execution based on frequency
        switch (testSchedule.getFrequency()) {
            case DAILY -> {
                nextExecution = currentDate.plusDays(1);
            }
            case WEEKLY -> {
                if (testSchedule.getDayOfWeek() != null) {
                    nextExecution = currentDate;
                    // Find next occurrence of the specified day of week
                    while (nextExecution.getDayOfWeek().getValue() != testSchedule.getDayOfWeek() || 
                           !nextExecution.isAfter(currentDate)) {
                        nextExecution = nextExecution.plusDays(1);
                    }
                } else {
                    nextExecution = currentDate.plusWeeks(1);
                }
            }
            case MONTHLY -> {
                if (testSchedule.getDayOfMonth() != null) {
                    // Start from next month
                    nextExecution = currentDate.plusMonths(1).withDayOfMonth(1);
                    // Adjust to specified day of month, considering month length
                    int targetDay = Math.min(testSchedule.getDayOfMonth(), nextExecution.lengthOfMonth());
                    nextExecution = nextExecution.withDayOfMonth(targetDay);
                    
                    // If the calculated date is not after current date, move to next month
                    if (!nextExecution.isAfter(currentDate)) {
                        nextExecution = nextExecution.plusMonths(1);
                        targetDay = Math.min(testSchedule.getDayOfMonth(), nextExecution.lengthOfMonth());
                        nextExecution = nextExecution.withDayOfMonth(targetDay);
                    }
                } else {
                    nextExecution = currentDate.plusMonths(1);
                }
            }
        }

        // Check if next execution exceeds end date
        if (testSchedule.getEndDate() != null) {
            if (nextExecution.isAfter(testSchedule.getEndDate())) {
                testSchedule.setIsActive(false);
                nextExecution = testSchedule.getEndDate();
            }
        }

        testSchedule.setNextExecution(nextExecution);
    }
}