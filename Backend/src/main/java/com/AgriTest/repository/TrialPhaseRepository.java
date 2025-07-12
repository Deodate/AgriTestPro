package com.AgriTest.repository;

import com.AgriTest.model.TrialPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrialPhaseRepository extends JpaRepository<TrialPhase, Long> {
    List<TrialPhase> findByTestCaseId(Long testCaseId);
} 