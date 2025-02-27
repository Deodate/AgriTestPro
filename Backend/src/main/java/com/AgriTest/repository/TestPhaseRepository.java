package com.AgriTest.repository;

import com.AgriTest.model.TestPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPhaseRepository extends JpaRepository<TestPhase, Long> {
    List<TestPhase> findByTestCaseId(Long testCaseId);
    
    List<TestPhase> findByStatus(String status);
}