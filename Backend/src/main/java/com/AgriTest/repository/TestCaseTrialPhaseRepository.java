// File: src/main/java/com/AgriTest/repository/TestCaseTrialPhaseRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.TestCaseTrialPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TestCaseTrialPhaseRepository extends JpaRepository<TestCaseTrialPhase, Long> {
    List<TestCaseTrialPhase> findByTestCaseId(Long testCaseId);

    // Add a method to delete trial phase observations by trial phase ID
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM trial_phase_observations WHERE trial_phase_id = :trialPhaseId", nativeQuery = true)
    void deleteTrialPhaseObservationsByTrialPhaseId(@Param("trialPhaseId") Long trialPhaseId);
}