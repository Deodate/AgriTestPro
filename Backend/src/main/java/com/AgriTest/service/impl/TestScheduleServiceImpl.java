package com.AgriTest.service.impl;

import com.AgriTest.dto.TestPhaseRequest;
import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.TestScheduleMapper;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TestScheduleRepository;
import com.AgriTest.service.TestCaseService;
import com.AgriTest.service.TestScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class TestScheduleServiceImpl implements TestScheduleService {

    @Autowired
    private TestScheduleRepository testScheduleRepository;
    
    @Autowired
    private TestCaseRepository testCaseRepository;
    
    @Autowired
    private TestScheduleMapper testScheduleMapper;
    
    @Autowired
    private TestCaseService testCaseService;

    @Override
    public List<TestScheduleResponse> getAllTestSchedules() {
        return testScheduleMapper.toDtoList(testScheduleRepository.findAll());
    }

    @Override
    public Optional<TestScheduleResponse> getTestScheduleById(Long id) {
        return testScheduleRepository.findById(id)
                .map(testScheduleMapper::toDto);
    }

    @Override
    @Transactional
    public TestScheduleResponse createTestSchedule(TestScheduleRequest testScheduleRequest, Long userId) {
        // Find the TestCase by ID
        TestCase testCase = testCaseRepository.findById(testScheduleRequest.getTestCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + testScheduleRequest.getTestCaseId()));
        
        // Log the test case name retrieved
        System.out.println("Fetched TestCase with ID: " + testCase.getId() + " and testName: " + testCase.getTestName());
        
        TestSchedule testSchedule = testScheduleMapper.toEntity(testScheduleRequest, testCase, userId);

        // Log the TestSchedule entity before saving
        System.out.println("Saving TestSchedule entity with testName: " + testSchedule.getTestName());

        TestSchedule savedTestSchedule = testScheduleRepository.save(testSchedule);
        
        return testScheduleMapper.toDto(savedTestSchedule);
    }

    @Override
    @Transactional
    public TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest testScheduleRequest) {
        TestSchedule existingSchedule = testScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test schedule not found with id: " + id));
        
        // Find the TestCase by ID
        TestCase testCase = testCaseRepository.findById(testScheduleRequest.getTestCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + testScheduleRequest.getTestCaseId()));
        
        // Log the test case name retrieved
        System.out.println("Fetched TestCase with ID: " + testCase.getId() + " and testName: " + testCase.getTestName());
        
        existingSchedule.setTestCase(testCase);
        existingSchedule.setTestName(testCase.getTestName()); // Ensure testName is updated on the existing entity
        existingSchedule.setScheduleName(testScheduleRequest.getScheduleName());
        existingSchedule.setStartDate(testScheduleRequest.getStartDate());
        existingSchedule.setEndDate(testScheduleRequest.getEndDate());
        existingSchedule.setFrequency(testScheduleRequest.getFrequency());
        existingSchedule.setDayOfWeek(testScheduleRequest.getDayOfWeek());
        existingSchedule.setDayOfMonth(testScheduleRequest.getDayOfMonth());
        
        // Recalculate next execution
        existingSchedule.setNextExecution(calculateNextExecution(
                testScheduleRequest.getStartDate(),
                testScheduleRequest.getFrequency(),
                testScheduleRequest.getDayOfWeek(),
                testScheduleRequest.getDayOfMonth()
        ));
        
        // Log the TestSchedule entity before saving
        System.out.println("Updating TestSchedule entity with testName: " + existingSchedule.getTestName());
        
        TestSchedule updatedSchedule = testScheduleRepository.save(existingSchedule);
        return testScheduleMapper.toDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteTestSchedule(Long id) {
        testScheduleRepository.deleteById(id);
    }

    @Override
    public List<TestScheduleResponse> getTestSchedulesByTestCase(Long testCaseId) {
        return testScheduleMapper.toDtoList(testScheduleRepository.findByTestCaseId(testCaseId));
    }

    @Override
    public List<TestScheduleResponse> getActiveTestSchedules() {
        return testScheduleMapper.toDtoList(testScheduleRepository.findByIsActiveTrue());
    }

    @Override
    public List<TestScheduleResponse> getTestSchedulesForToday() {
        LocalDate today = LocalDate.now();
        return testScheduleMapper.toDtoList(
                testScheduleRepository.findByNextExecutionLessThanEqualAndIsActiveTrue(today)
        );
    }

    @Override
    @Transactional
    public TestScheduleResponse activateTestSchedule(Long id) {
        TestSchedule testSchedule = testScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test schedule not found with id: " + id));
        
        testSchedule.setIsActive(true);
        TestSchedule updatedSchedule = testScheduleRepository.save(testSchedule);
        return testScheduleMapper.toDto(updatedSchedule);
    }

    @Override
    @Transactional
    public TestScheduleResponse deactivateTestSchedule(Long id) {
        TestSchedule testSchedule = testScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test schedule not found with id: " + id));
        
        testSchedule.setIsActive(false);
        TestSchedule updatedSchedule = testScheduleRepository.save(testSchedule);
        return testScheduleMapper.toDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void executeSchedule(Long scheduleId) {
        TestSchedule schedule = testScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Test schedule not found with id: " + scheduleId));
        
        if (!schedule.getIsActive()) {
            return;
        }
        
        // Create a test phase based on the schedule
        TestPhaseRequest phaseRequest = TestPhaseRequest.builder()
                .name("Scheduled: " + schedule.getScheduleName())
                .description("Automatically generated from schedule: " + schedule.getScheduleName())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7)) // Default one week for the phase
                .status("PLANNED")
                .build();
        
        testCaseService.addTestPhase(schedule.getTestCase().getId(), phaseRequest);
        
        // Update next execution date
        schedule.setNextExecution(calculateNextExecution(
                schedule.getStartDate(),
                schedule.getFrequency(),
                schedule.getDayOfWeek(),
                schedule.getDayOfMonth()
        ));
        
        // If there's an end date and we've passed it, deactivate the schedule
        if (schedule.getEndDate() != null && schedule.getNextExecution().isAfter(schedule.getEndDate())) {
            schedule.setIsActive(false);
        }
        
        testScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void executeAllDueSchedules() {
        List<TestSchedule> dueSchedules = testScheduleRepository.findByNextExecutionLessThanEqualAndIsActiveTrue(LocalDate.now());
        
        for (TestSchedule schedule : dueSchedules) {
            try {
                executeSchedule(schedule.getId());
            } catch (Exception e) {
                // Log the error but continue with other schedules
                System.err.println("Error executing schedule " + schedule.getId() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Get test schedules by specific IDs
     * @param scheduleIds List of schedule IDs to retrieve
     * @return List of test schedule responses
     */
    @Override
    public List<TestScheduleResponse> getTestSchedulesByIds(List<Long> scheduleIds) {
        List<TestSchedule> schedules = testScheduleRepository.findAllById(scheduleIds);
        return testScheduleMapper.toDtoList(schedules);
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
                nextDate = today.plusDays(1);
                break;
            case "WEEKLY":
                if (dayOfWeek == null) {
                    dayOfWeek = startDate.getDayOfWeek().getValue();
                }
                nextDate = today.with(TemporalAdjusters.next(DayOfWeek.of(dayOfWeek)));
                break;
            case "BIWEEKLY":
                if (dayOfWeek == null) {
                    dayOfWeek = startDate.getDayOfWeek().getValue();
                }
                nextDate = today.with(TemporalAdjusters.next(DayOfWeek.of(dayOfWeek))).plusWeeks(1);
                break;
            case "MONTHLY":
                if (dayOfMonth == null) {
                    dayOfMonth = startDate.getDayOfMonth();
                }
                nextDate = today.plusMonths(1)
                        .withDayOfMonth(Math.min(dayOfMonth, today.plusMonths(1).lengthOfMonth()));
                break;
            default:
                nextDate = today.plusDays(1);
        }
        
        return nextDate;
    }
}