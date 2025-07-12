package com.AgriTest.repository;

import com.AgriTest.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProductId(Long productId);
    
    List<TestCase> findByStatus(String status);
    
    List<TestCase> findByCreatedBy(Long userId);

    Optional<TestCase> findByTestName(String testName);

    // Find Test Cases that have at least one Test Phase
}