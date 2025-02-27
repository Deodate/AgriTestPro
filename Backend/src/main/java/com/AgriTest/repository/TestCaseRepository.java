package com.AgriTest.repository;

import com.AgriTest.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProductId(Long productId);
    
    List<TestCase> findByStatus(String status);
    
    List<TestCase> findByCreatedBy(Long userId);
}