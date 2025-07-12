package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.ScheduleFrequency;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.exception.ValidationException;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
     * @throws ValidationException if request validation fails
     * @throws EntityNotFoundException if referenced TestCase is not found
     */
    public TestSchedule toEntity(TestScheduleRequest request) {
        validateRequest(request);
        
        TestSchedule testSchedule = new TestSchedule();
        mapBasicFields(testSchedule, request);
        mapFrequencyFields(testSchedule, request);
        mapDates(testSchedule, request);
        mapTestCase(testSchedule, request);
        calculateNextExecution(testSchedule);
        
        return testSchedule;
    }

    /**
     * Maps basic fields from request to entity
     */
    private void mapBasicFields(TestSchedule testSchedule, TestScheduleRequest request) {
        testSchedule.setTestName(request.getTestName().trim());
        testSchedule.setScheduleName(request.getScheduleName().trim());
        testSchedule.setTrialPhase(request.getTrialPhase());
        testSchedule.setAssignedPersonnel(request.getAssignedPersonnel());
        testSchedule.setLocation(request.getLocation());
        testSchedule.setTestObjective(request.getTestObjective());
        testSchedule.setEquipmentRequired(request.getEquipmentRequired());
        testSchedule.setNotificationPreference(request.getNotificationPreference());
        testSchedule.setNotes(request.getNotes());
        testSchedule.setDescription(request.getDescription());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setCreatedBy(request.getCreatedBy());
        testSchedule.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }

    /**
     * Maps and validates frequency-related fields
     * @throws ValidationException if frequency configuration is invalid
     */
    private void mapFrequencyFields(TestSchedule testSchedule, TestScheduleRequest request) {
        try {
            String frequencyStr = request.getFrequency().toUpperCase().trim();
            testSchedule.setFrequency(ScheduleFrequency.valueOf(frequencyStr));
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format(
                "Invalid frequency '%s'. Valid values are: %s",
                request.getFrequency(),
                String.join(", ", List.of(ScheduleFrequency.values()).stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()))
            ));
        }

        testSchedule.setDayOfMonth(request.getDayOfMonth());
        testSchedule.setDayOfWeek(request.getDayOfWeek());
        validateScheduleConfiguration(testSchedule);
    }

    /**
     * Maps and validates date fields
     * @throws ValidationException if date configuration is invalid
     */
    private void mapDates(TestSchedule testSchedule, TestScheduleRequest request) {
        validateDates(request);
        testSchedule.setStartDate(request.getStartDate());
        testSchedule.setEndDate(request.getEndDate());
    }

    /**
     * Maps and validates test case relationship
     * @throws EntityNotFoundException if test case is not found
     */
    private void mapTestCase(TestSchedule testSchedule, TestScheduleRequest request) {
        if (request.getTestCaseId() != null) {
            TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new EntityNotFoundException(
                    String.format("TestCase with ID %d not found", request.getTestCaseId())));
            testSchedule.setTestCase(testCase);
        }
    }

    /**
     * Validates the test schedule request
     * @throws ValidationException if validation fails
     */
    private void validateRequest(TestScheduleRequest request) {
        if (request == null) {
            throw new ValidationException("TestScheduleRequest cannot be null");
        }

        List<String> errors = new java.util.ArrayList<>();
        
        if (isBlank(request.getTestName())) {
            errors.add("Test name is required");
        }
        if (isBlank(request.getScheduleName())) {
            errors.add("Schedule name is required");
        }
        if (isBlank(request.getFrequency())) {
            errors.add("Frequency is required");
        }
        if (isBlank(request.getTrialPhase())) {
            errors.add("Trial phase is required");
        }
        if (isBlank(request.getAssignedPersonnel())) {
            errors.add("Assigned personnel is required");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: " + String.join(", ", errors));
        }
    }

    /**
     * Validates schedule configuration based on frequency
     * @throws ValidationException if configuration is invalid
     */
    private void validateScheduleConfiguration(TestSchedule testSchedule) {
        switch (testSchedule.getFrequency()) {
            case WEEKLY -> {
                if (testSchedule.getDayOfWeek() != null) {
                    if (testSchedule.getDayOfWeek() < 1 || testSchedule.getDayOfWeek() > 7) {
                        throw new ValidationException(
                            "Day of week must be between 1 (Monday) and 7 (Sunday)");
                    }
                }
            }
            case MONTHLY -> {
                if (testSchedule.getDayOfMonth() != null) {
                    if (testSchedule.getDayOfMonth() < 1 || testSchedule.getDayOfMonth() > 31) {
                        throw new ValidationException(
                            "Day of month must be between 1 and 31");
                    }
                }
            }
            case DAILY -> {
                if (testSchedule.getDayOfWeek() != null || testSchedule.getDayOfMonth() != null) {
                    log.warn("Day of week/month will be ignored for DAILY frequency");
                }
            }
        }
    }

    /**
     * Validates the dates in the request
     * @throws ValidationException if validation fails
     */
    private void validateDates(TestScheduleRequest request) {
        if (request.getStartDate() == null) {
            throw new ValidationException("Start date is required");
        }
        
        if (request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new ValidationException(
                    String.format("End date (%s) cannot be before start date (%s)",
                        request.getEndDate(), request.getStartDate()));
            }
            
            // Warn if end date is too far in the future (e.g., more than 2 years)
            if (request.getEndDate().isAfter(request.getStartDate().plusYears(2))) {
                log.warn("End date is more than 2 years after start date");
            }
        }
        
        if (request.getStartDate().isBefore(LocalDate.now())) {
            log.warn("Start date {} is in the past", request.getStartDate());
        }
    }

    /**
     * Converts a TestSchedule entity to a TestScheduleResponse DTO
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
            .frequency(testSchedule.getFrequency().name())
            .location(testSchedule.getLocation())
            .testObjective(testSchedule.getTestObjective())
            .equipmentRequired(testSchedule.getEquipmentRequired())
            .notificationPreference(testSchedule.getNotificationPreference())
            .notes(testSchedule.getNotes())
            .dayOfWeek(testSchedule.getDayOfWeek())
            .dayOfMonth(testSchedule.getDayOfMonth())
            .nextExecution(testSchedule.getNextExecution())
            .isActive(testSchedule.isActive())
            .description(testSchedule.getDescription())
            .priority(testSchedule.getPriority())
            .status(testSchedule.getStatus())
            .createdBy(testSchedule.getCreatedBy())
            .createdAt(testSchedule.getCreatedAt())
            .updatedAt(testSchedule.getUpdatedAt())
            .build();
    }

    /**
     * Converts a list of TestSchedule entities to TestScheduleResponse DTOs
     */
    public List<TestScheduleResponse> toResponseList(List<TestSchedule> schedules) {
        if (schedules == null) {
            return Collections.emptyList();
        }
        return schedules.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the next execution date based on schedule frequency and current date
     */
    private void calculateNextExecution(TestSchedule testSchedule) {
        if (!testSchedule.isActive() || testSchedule.getStartDate() == null) {
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate nextExecution = testSchedule.getStartDate();

        if (nextExecution.isBefore(currentDate)) {
            nextExecution = switch (testSchedule.getFrequency()) {
                case DAILY -> currentDate.plusDays(1);
                case WEEKLY -> {
                    if (testSchedule.getDayOfWeek() != null) {
                        LocalDate next = currentDate.plusDays(1);
                        while (next.getDayOfWeek().getValue() != testSchedule.getDayOfWeek()) {
                            next = next.plusDays(1);
                        }
                        yield next;
                    }
                    yield currentDate.plusWeeks(1);
                }
                case MONTHLY -> {
                    if (testSchedule.getDayOfMonth() != null) {
                        LocalDate next = currentDate.plusMonths(1).withDayOfMonth(1);
                        int maxDay = next.lengthOfMonth();
                        yield next.withDayOfMonth(Math.min(testSchedule.getDayOfMonth(), maxDay));
                    }
                    yield currentDate.plusMonths(1);
                }
            };
        }

        // Check if next execution exceeds end date
        if (testSchedule.getEndDate() != null && nextExecution.isAfter(testSchedule.getEndDate())) {
            testSchedule.setIsActive(false);
            nextExecution = testSchedule.getEndDate();
        }

        testSchedule.setNextExecution(nextExecution);
    }

    /**
     * Utility method to check if a string is null, empty, or only whitespace
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}