package com.AgriTest.service.impl;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.ScheduleFrequency;
import com.AgriTest.repository.TestScheduleRepository;
import com.AgriTest.service.TestScheduleService;
import com.AgriTest.mapper.TestScheduleMapper;
import com.AgriTest.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        try {
            log.debug("Creating new test schedule with name: {}", request.getScheduleName());
            TestSchedule testSchedule = testScheduleMapper.toEntity(request);
            testSchedule = testScheduleRepository.save(testSchedule);
            log.info("Created test schedule with ID: {}", testSchedule.getId());
            return testScheduleMapper.toResponse(testSchedule);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create test schedule due to data integrity violation", e);
            throw new IllegalArgumentException("Invalid test schedule data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to create test schedule", e);
            throw new RuntimeException("Failed to create test schedule: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestScheduleResponse> getAllTestSchedules() {
        try {
            log.debug("Fetching all test schedules");
            List<TestSchedule> schedules = testScheduleRepository.findAll();
            log.debug("Found {} test schedules", schedules.size());
            return testScheduleMapper.toResponseList(schedules);
        } catch (Exception e) {
            log.error("Failed to fetch test schedules", e);
            throw new RuntimeException("Failed to fetch test schedules: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TestScheduleResponse getTestScheduleById(Long id) {
        try {
            log.debug("Fetching test schedule with ID: {}", id);
            TestSchedule schedule = testScheduleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Test Schedule not found with id: " + id));
            return testScheduleMapper.toResponse(schedule);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch test schedule with ID: {}", id, e);
            throw new RuntimeException("Failed to fetch test schedule: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest request) {
        try {
            log.debug("Updating test schedule with ID: {}", id);
            TestSchedule existingSchedule = testScheduleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Test Schedule not found with id: " + id));
            
            TestSchedule updatedSchedule = testScheduleMapper.toEntity(request);
            updatedSchedule.setId(id);
            updatedSchedule.setCreatedAt(existingSchedule.getCreatedAt());
            
            updatedSchedule = testScheduleRepository.save(updatedSchedule);
            log.info("Updated test schedule with ID: {}", id);
            return testScheduleMapper.toResponse(updatedSchedule);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update test schedule due to data integrity violation", e);
            throw new IllegalArgumentException("Invalid test schedule data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to update test schedule with ID: {}", id, e);
            throw new RuntimeException("Failed to update test schedule: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteTestSchedule(Long id) {
        try {
            log.debug("Deleting test schedule with ID: {}", id);
            if (!testScheduleRepository.existsById(id)) {
                throw new ResourceNotFoundException("Test Schedule not found with id: " + id);
            }
            testScheduleRepository.deleteById(id);
            log.info("Deleted test schedule with ID: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete test schedule with ID: {}", id, e);
            throw new RuntimeException("Failed to delete test schedule: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestScheduleResponse> getTestSchedulesByIds(List<Long> scheduleIds) {
        try {
            if (CollectionUtils.isEmpty(scheduleIds)) {
                return List.of();
            }
            log.debug("Fetching test schedules with IDs: {}", scheduleIds);
            List<TestSchedule> schedules = testScheduleRepository.findAllById(scheduleIds);
            
            if (schedules.size() != scheduleIds.size()) {
                log.warn("Some requested schedules were not found. Requested: {}, Found: {}", 
                    scheduleIds.size(), schedules.size());
            }
            
            return testScheduleMapper.toResponseList(schedules);
        } catch (Exception e) {
            log.error("Failed to fetch test schedules by IDs: {}", scheduleIds, e);
            throw new RuntimeException("Failed to fetch test schedules: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void executeAllDueSchedules() {
        try {
            LocalDate today = LocalDate.now();
            log.debug("Executing all due schedules for date: {}", today);
            List<TestSchedule> dueSchedules = testScheduleRepository.findAllByNextExecutionAndIsActiveTrue(today);
            log.info("Found {} due schedules to execute", dueSchedules.size());
            
            for (TestSchedule schedule : dueSchedules) {
                try {
                    executeSchedule(schedule);
                    updateNextExecutionDate(schedule);
                    log.info("Successfully executed schedule: {}", schedule.getId());
                } catch (Exception e) {
                    log.error("Failed to execute schedule {}: {}", schedule.getId(), e.getMessage(), e);
                    // Mark schedule as failed but keep it active for retry
                    schedule.setStatus("EXECUTION_FAILED");
                    testScheduleRepository.save(schedule);
                }
            }
        } catch (Exception e) {
            log.error("Failed to execute due schedules", e);
            throw new RuntimeException("Failed to execute due schedules: " + e.getMessage());
        }
    }

    private void executeSchedule(TestSchedule schedule) {
        log.debug("Executing schedule: {}", schedule.getId());
        
        int maxRetries = 3;
        int currentTry = 0;
        boolean success = false;
        Exception lastException = null;

        while (!success && currentTry < maxRetries) {
            try {
                currentTry++;
                log.debug("Execution attempt {} for schedule {}", currentTry, schedule.getId());

                // Update status to in progress
                schedule.setStatus("IN_PROGRESS");
                testScheduleRepository.save(schedule);

                // Create test run
                createTestRun(schedule);

                // Send notifications
                sendNotifications(schedule);

                // Mark as completed
                schedule.setStatus("COMPLETED");
                testScheduleRepository.save(schedule);

                success = true;
                log.info("Schedule {} executed successfully on attempt {}", schedule.getId(), currentTry);
            } catch (Exception e) {
                lastException = e;
                log.warn("Execution attempt {} failed for schedule {}: {}", 
                    currentTry, schedule.getId(), e.getMessage());
                
                if (currentTry < maxRetries) {
                    // Wait before retry (exponential backoff)
                    try {
                        Thread.sleep(1000L * currentTry * currentTry);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Execution interrupted", ie);
                    }
                }
            }
        }

        if (!success) {
            log.error("All execution attempts failed for schedule {}", schedule.getId());
            throw new RuntimeException("Failed to execute schedule after " + maxRetries + " attempts", 
                lastException);
        }
    }

    private void createTestRun(TestSchedule schedule) {
        log.debug("Creating test run for schedule: {}", schedule.getId());
        // TODO: Implement test run creation
        // This would typically:
        // 1. Create a new test run record
        // 2. Copy test parameters from the schedule
        // 3. Set initial status
        // 4. Link to the test case
    }

    private void sendNotifications(TestSchedule schedule) {
        log.debug("Sending notifications for schedule: {}", schedule.getId());
        // TODO: Implement notification sending
        // This would typically:
        // 1. Check notification preferences
        // 2. Format notification message
        // 3. Send to assigned personnel
        // 4. Log notification status
    }

    private void updateNextExecutionDate(TestSchedule schedule) {
        LocalDate nextExecution = calculateNextExecutionDate(schedule);
        schedule.setNextExecution(nextExecution);
        
        if (schedule.getEndDate() != null && nextExecution.isAfter(schedule.getEndDate())) {
            log.info("Schedule {} has reached end date. Deactivating.", schedule.getId());
            schedule.setIsActive(false);
            schedule.setStatus("COMPLETED");
        }
        
        testScheduleRepository.save(schedule);
        log.debug("Updated next execution date for schedule {}: {}", schedule.getId(), nextExecution);
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