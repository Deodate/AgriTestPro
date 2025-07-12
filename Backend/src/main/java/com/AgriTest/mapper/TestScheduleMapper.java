package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestSchedule;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestScheduleMapper {

    public TestScheduleResponse toDto(TestSchedule testSchedule) {
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
                .dayOfWeek(testSchedule.getDayOfWeek())
                .dayOfMonth(testSchedule.getDayOfMonth())
                .nextExecution(testSchedule.getNextExecution())
                .isActive(testSchedule.getIsActive())
                .createdBy(testSchedule.getCreatedBy())
                .createdAt(testSchedule.getCreatedAt())
                .updatedAt(testSchedule.getUpdatedAt())
                .build();
    }
    
    public List<TestScheduleResponse> toDtoList(List<TestSchedule> testSchedules) {
        if (testSchedules == null) {
            return List.of();
        }
        return testSchedules.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public TestSchedule toEntity(TestScheduleRequest request, TestCase testCase, Long userId) {
        if (request == null) {
            return null;
        }
        
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setTestCase(testCase);
        testSchedule.setTestName(testCase.getTestName());
        testSchedule.setScheduleName(request.getScheduleName());
        testSchedule.setStartDate(request.getStartDate());
        testSchedule.setEndDate(request.getEndDate());
        testSchedule.setFrequency(request.getFrequency());
        testSchedule.setDayOfWeek(request.getDayOfWeek());
        testSchedule.setDayOfMonth(request.getDayOfMonth());
        testSchedule.setCreatedBy(userId);

        // Map added fields from request to entity
        testSchedule.setTrialPhase(request.getTrialPhase());
        testSchedule.setAssignedPersonnel(request.getAssignedPersonnel());
        testSchedule.setLocation(request.getLocation());
        testSchedule.setTestObjective(request.getTestObjective());
        testSchedule.setEquipmentRequired(request.getEquipmentRequired());
        testSchedule.setNotificationPreference(request.getNotificationPreference());
        testSchedule.setNotes(request.getNotes());
        
        // Calculate next execution date
        testSchedule.setNextExecution(calculateNextExecution(
                request.getStartDate(),
                request.getFrequency(),
                request.getDayOfWeek(),
                request.getDayOfMonth()
        ));
        
        return testSchedule;
    }
    
    private LocalDate calculateNextExecution(
            LocalDate startDate,
            String frequency,
            Integer dayOfWeek,
            Integer dayOfMonth) {
        
        LocalDate today = LocalDate.now();
        if (startDate.isAfter(today)) {
            return startDate;
        }
        
        LocalDate nextDate;
        switch (frequency) {
            case "DAILY":
                nextDate = today;
                break;
            case "WEEKLY":
                if (dayOfWeek == null) {
                    dayOfWeek = startDate.getDayOfWeek().getValue();
                }
                nextDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
                break;
            case "BIWEEKLY":
                if (dayOfWeek == null) {
                    dayOfWeek = startDate.getDayOfWeek().getValue();
                }
                nextDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
                if (nextDate.equals(today)) {
                    nextDate = nextDate.plusWeeks(2);
                } else {
                    nextDate = nextDate.plusWeeks(1);
                }
                break;
            case "MONTHLY":
                if (dayOfMonth == null) {
                    dayOfMonth = startDate.getDayOfMonth();
                }
                nextDate = today.withDayOfMonth(1)
                        .plusMonths(today.getDayOfMonth() >= dayOfMonth ? 1 : 0)
                        .withDayOfMonth(Math.min(dayOfMonth, today.plusMonths(1).lengthOfMonth()));
                break;
            default:
                nextDate = today;
        }
        
        return nextDate;
    }
}