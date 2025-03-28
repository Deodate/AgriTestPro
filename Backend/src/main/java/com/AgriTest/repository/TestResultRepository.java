package com.AgriTest.repository;

import com.AgriTest.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestPhase_TestCase_Id(Long testCaseId);
    List<TestResult> findByProductId(Long productId);
}