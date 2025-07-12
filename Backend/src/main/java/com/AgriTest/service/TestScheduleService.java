// File: src/main/java/com/AgriTest/service/TestScheduleService.java
package com.AgriTest.service;

import com.AgriTest.dto.TestPhaseRequest;
import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TestScheduleService {
    /**
     * Get all test schedules
     */
    List<TestScheduleResponse> getAllTestSchedules();
    
    /**
     * Get a test schedule by ID
     */
    Optional<TestScheduleResponse> getTestScheduleById(Long id);
    
    /**
     * Create a new test schedule
     */
    TestScheduleResponse createTestSchedule(TestScheduleRequest testScheduleRequest, Long userId);
    
    /**
     * Update an existing test schedule
     */
    TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest testScheduleRequest);
    
    /**
     * Delete a test schedule
     */
    void deleteTestSchedule(Long id);
    
    /**
     * Get test schedules by test case ID
     */
    List<TestScheduleResponse> getTestSchedulesByTestCase(Long testCaseId);
    
    /**
     * Get all active test schedules
     */
    List<TestScheduleResponse> getActiveTestSchedules();
    
    /**
     * Get test schedules scheduled for today
     */
    List<TestScheduleResponse> getTestSchedulesForToday();
    
    /**
     * Activate a test schedule
     */
    TestScheduleResponse activateTestSchedule(Long id);
    
    /**
     * Deactivate a test schedule
     */
    TestScheduleResponse deactivateTestSchedule(Long id);
    
    /**
     * Execute a specific test schedule
     */
    void executeSchedule(Long scheduleId);
    
    /**
     * Execute all due test schedules
     */
    void executeAllDueSchedules();
    
    /**
     * Get test schedules by specific IDs
     * @param scheduleIds List of schedule IDs to retrieve
     * @return List of test schedule responses
     */
    List<TestScheduleResponse> getTestSchedulesByIds(List<Long> scheduleIds);
}