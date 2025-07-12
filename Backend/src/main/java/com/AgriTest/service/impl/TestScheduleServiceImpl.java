package com.AgriTest.service.impl;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.ScheduleFrequency;
import com.AgriTest.repository.TestScheduleRepository;
import com.AgriTest.service.TestScheduleService;
import com.AgriTest.mapper.TestScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestScheduleServiceImpl implements TestScheduleService {

    private final TestScheduleRepository testScheduleRepository;
    private final TestScheduleMapper testScheduleMapper;

    @Override
    @Transactional
    public TestScheduleResponse createTestSchedule(TestScheduleRequest request) {
        TestSchedule testSchedule = testScheduleMapper.toEntity(request);
        testSchedule = testScheduleRepository.save(testSchedule);
        return testScheduleMapper.toResponse(testSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestScheduleResponse> getAllTestSchedules() {
        List<TestSchedule> schedules = testScheduleRepository.findAll();
        return testScheduleMapper.toResponseList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public TestScheduleResponse getTestScheduleById(Long id) {
        TestSchedule schedule = testScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test Schedule not found with id: " + id));
        return testScheduleMapper.toResponse(schedule);
    }

    @Override
    @Transactional
    public TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest request) {
        TestSchedule existingSchedule = testScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test Schedule not found with id: " + id));
        
        // Use mapper to update the entity
        TestSchedule updatedSchedule = testScheduleMapper.toEntity(request);
        updatedSchedule.setId(id);
        // Preserve creation timestamp
        updatedSchedule.setCreatedAt(existingSchedule.getCreatedAt());
        
        updatedSchedule = testScheduleRepository.save(updatedSchedule);
        return testScheduleMapper.toResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteTestSchedule(Long id) {
        if (!testScheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Test Schedule not found with id: " + id);
        }
        testScheduleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestScheduleResponse> getTestSchedulesByIds(List<Long> scheduleIds) {
        List<TestSchedule> schedules = testScheduleRepository.findAllById(scheduleIds);
        return testScheduleMapper.toResponseList(schedules);
    }

    @Override
    @Transactional
    public void executeAllDueSchedules() {
        LocalDate today = LocalDate.now();
        List<TestSchedule> dueSchedules = testScheduleRepository.findAllByNextExecutionAndIsActiveTrue(today);
        
        for (TestSchedule schedule : dueSchedules) {
            try {
                executeSchedule(schedule);
                updateNextExecutionDate(schedule);
            } catch (Exception e) {
                log.error("Failed to execute schedule {}: {}", schedule.getId(), e.getMessage(), e);
            }
        }
    }

    private void executeSchedule(TestSchedule schedule) {
        // TODO: Implement actual test execution logic
        // This might involve:
        // 1. Creating a new test run
        // 2. Notifying assigned personnel
        // 3. Updating status
        schedule.setStatus("IN_PROGRESS");
        testScheduleRepository.save(schedule);
    }

    private void updateNextExecutionDate(TestSchedule schedule) {
        LocalDate nextExecution = calculateNextExecutionDate(schedule);
        schedule.setNextExecution(nextExecution);
        
        // If end date is set and next execution would be after end date
        if (schedule.getEndDate() != null && nextExecution.isAfter(schedule.getEndDate())) {
            schedule.setIsActive(false);
        }
        
        testScheduleRepository.save(schedule);
    }

    private LocalDate calculateNextExecutionDate(TestSchedule schedule) {
        LocalDate currentDate = LocalDate.now();
        
        return switch (schedule.getFrequency()) {
            case DAILY -> currentDate.plusDays(1);
            case WEEKLY -> {
                if (schedule.getDayOfWeek() != null) {
                    LocalDate next = currentDate.plusDays(1);
                    while (next.getDayOfWeek().getValue() != schedule.getDayOfWeek()) {
                        next = next.plusDays(1);
                    }
                    yield next;
                }
                yield currentDate.plusWeeks(1);
            }
            case MONTHLY -> {
                if (schedule.getDayOfMonth() != null) {
                    LocalDate next = currentDate.plusMonths(1).withDayOfMonth(1);
                    int targetDay = Math.min(schedule.getDayOfMonth(), next.lengthOfMonth());
                    yield next.withDayOfMonth(targetDay);
                }
                yield currentDate.plusMonths(1);
            }
        };
    }
}