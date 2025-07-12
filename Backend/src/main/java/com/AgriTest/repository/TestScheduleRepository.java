// File: src/main/java/com/AgriTest/repository/TestScheduleRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.TestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestScheduleRepository extends JpaRepository<TestSchedule, Long> {
}