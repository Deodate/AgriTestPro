// File: src/main/java/com/AgriTest/service/TestScheduleService.java
package com.AgriTest.service;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import java.util.List;

public interface TestScheduleService {
    /**
     * Create a new test schedule
     * @param request The test schedule request
     * @return The created test schedule response
     */
    TestScheduleResponse createTestSchedule(TestScheduleRequest request);

    /**
     * Get all test schedules
     * @return List of test schedule responses
     */
    List<TestScheduleResponse> getAllTestSchedules();

    /**
     * Get a test schedule by ID
     * @param id The test schedule ID
     * @return The test schedule response
     */
    TestScheduleResponse getTestScheduleById(Long id);

    /**
     * Update an existing test schedule
     * @param id The test schedule ID
     * @param request The updated test schedule request
     * @return The updated test schedule response
     */
    TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest request);

    /**
     * Delete a test schedule
     * @param id The test schedule ID
     */
    void deleteTestSchedule(Long id);

    /**
     * Get test schedules by IDs
     * @param scheduleIds List of schedule IDs
     * @return List of test schedule responses
     */
    List<TestScheduleResponse> getTestSchedulesByIds(List<Long> scheduleIds);

    /**
     * Execute all due test schedules
     */
    void executeAllDueSchedules();
}