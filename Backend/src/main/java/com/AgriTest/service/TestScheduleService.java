// File: src/main/java/com/AgriTest/service/TestScheduleService.java
package com.AgriTest.service;

import com.AgriTest.dto.TestPhaseRequest;
import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TestScheduleService {
    List<TestScheduleResponse> getAllTestSchedules();
    
    Optional<TestScheduleResponse> getTestScheduleById(Long id);
    
    TestScheduleResponse createTestSchedule(TestScheduleRequest testScheduleRequest, Long userId);
    
    TestScheduleResponse updateTestSchedule(Long id, TestScheduleRequest testScheduleRequest);
    
    void deleteTestSchedule(Long id);
    
    List<TestScheduleResponse> getTestSchedulesByTestCase(Long testCaseId);
    
    List<TestScheduleResponse> getActiveTestSchedules();
    
    List<TestScheduleResponse> getTestSchedulesForToday();
    
    TestScheduleResponse activateTestSchedule(Long id);
    
    TestScheduleResponse deactivateTestSchedule(Long id);
    
    void executeSchedule(Long scheduleId);
    
    void executeAllDueSchedules();
}