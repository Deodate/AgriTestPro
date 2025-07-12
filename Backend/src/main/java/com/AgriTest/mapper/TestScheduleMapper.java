package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.TestCase;
import com.AgriTest.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     */
    public TestSchedule toEntity(TestScheduleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("TestScheduleRequest cannot be null");
        }
        
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
        testSchedule.setFrequency(request.getFrequency());
        testSchedule.setDayOfMonth(request.getDayOfMonth());
        testSchedule.setDayOfWeek(request.getDayOfWeek());
        testSchedule.setStartDate(request.getStartDate());
        testSchedule.setEndDate(request.getEndDate());
        testSchedule.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        testSchedule.setDescription(request.getDescription());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setCreatedBy(request.getCreatedBy());

        // Handle test case relationship
        if (request.getTestCaseId() != null) {
            TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new EntityNotFoundException("TestCase not found with id: " + request.getTestCaseId()));
            testSchedule.setTestCase(testCase);
        }

        // Calculate next execution date based on frequency
        calculateNextExecution(testSchedule);
        
        return testSchedule;
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
            .scheduleName(testSchedule.getScheduleName())
            .startDate(testSchedule.getStartDate())
            .trialPhase(testSchedule.getTrialPhase())
            .assignedPersonnel(testSchedule.getAssignedPersonnel())
            .endDate(testSchedule.getEndDate())
            .frequency(testSchedule.getFrequency())
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
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate nextExecution = testSchedule.getStartDate();

        if (nextExecution.isBefore(currentDate)) {
            switch (testSchedule.getFrequency().toUpperCase()) {
                case "DAILY":
                    nextExecution = currentDate.plusDays(1);
                    break;
                case "WEEKLY":
                    if (testSchedule.getDayOfWeek() != null) {
                        nextExecution = currentDate.plusDays(1);
                        while (nextExecution.getDayOfWeek().getValue() != testSchedule.getDayOfWeek()) {
                            nextExecution = nextExecution.plusDays(1);
                        }
                    } else {
                        nextExecution = currentDate.plusWeeks(1);
                    }
                    break;
                case "MONTHLY":
                    if (testSchedule.getDayOfMonth() != null) {
                        nextExecution = currentDate.plusMonths(1).withDayOfMonth(1);
                        int maxDay = nextExecution.lengthOfMonth();
                        nextExecution = nextExecution.withDayOfMonth(
                            Math.min(testSchedule.getDayOfMonth(), maxDay));
                    } else {
                        nextExecution = currentDate.plusMonths(1);
                    }
                    break;
                default:
                    log.warn("Unknown frequency: {}. Defaulting to daily schedule.", 
                        testSchedule.getFrequency());
                    nextExecution = currentDate.plusDays(1);
            }
        }

        // Check if next execution exceeds end date
        if (testSchedule.getEndDate() != null && nextExecution.isAfter(testSchedule.getEndDate())) {
            testSchedule.setIsActive(false);
            nextExecution = testSchedule.getEndDate();
        }

        testSchedule.setNextExecution(nextExecution);
    }
}