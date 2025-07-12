// File: src/main/java/com/AgriTest/repository/TestScheduleRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.TestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestScheduleRepository extends JpaRepository<TestSchedule, Long> {
    /**
     * Find all active schedules that are due for execution on a specific date
     * @param date The date to check for due schedules
     * @return List of test schedules due for execution
     */
    @Query("SELECT ts FROM TestSchedule ts WHERE ts.isActive = true AND ts.nextExecution = :date")
    List<TestSchedule> findAllByNextExecutionAndIsActiveTrue(@Param("date") LocalDate date);

    /**
     * Find all schedules for a specific test case
     * @param testCaseId The test case ID
     * @return List of test schedules for the test case
     */
    List<TestSchedule> findByTestCaseId(Long testCaseId);

    /**
     * Find all active schedules
     * @return List of active test schedules
     */
    List<TestSchedule> findByIsActiveTrue();

    /**
     * Find schedules by frequency
     * @param frequency The schedule frequency
     * @return List of test schedules with the specified frequency
     */
    List<TestSchedule> findByFrequency(String frequency);
}