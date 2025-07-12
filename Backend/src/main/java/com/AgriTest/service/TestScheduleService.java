// File: src/main/java/com/AgriTest/service/TestScheduleService.java
package com.AgriTest.service;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.model.TestSchedule;
import java.util.List;

public interface TestScheduleService {
    TestSchedule createTestSchedule(TestScheduleRequest request);
    List<TestSchedule> getAllTestSchedules();
    TestSchedule getTestScheduleById(Long id);
    TestSchedule updateTestSchedule(Long id, TestScheduleRequest request);
    void deleteTestSchedule(Long id);
}