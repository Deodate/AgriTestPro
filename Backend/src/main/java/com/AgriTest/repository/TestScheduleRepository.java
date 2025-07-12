// File: src/main/java/com/AgriTest/repository/TestScheduleRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.TestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestScheduleRepository extends JpaRepository<TestSchedule, Long> {
    List<TestSchedule> findByTestCaseId(Long testCaseId);
    
    List<TestSchedule> findByIsActiveTrue();
    
    List<TestSchedule> findByNextExecutionLessThanEqualAndIsActiveTrue(LocalDate date);
    
    List<TestSchedule> findByCreatedBy(Long userId);
}