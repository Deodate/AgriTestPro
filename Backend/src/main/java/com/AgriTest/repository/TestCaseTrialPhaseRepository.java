// File: src/main/java/com/AgriTest/repository/TestCaseTrialPhaseRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.TestCaseTrialPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseTrialPhaseRepository extends JpaRepository<TestCaseTrialPhase, Long> {
    List<TestCaseTrialPhase> findByTestCaseId(Long testCaseId);
}