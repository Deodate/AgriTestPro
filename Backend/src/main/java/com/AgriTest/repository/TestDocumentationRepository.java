package com.AgriTest.repository;

import com.AgriTest.model.TestDocumentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestDocumentationRepository extends JpaRepository<TestDocumentation, Long> {
    List<TestDocumentation> findByCreatedBy(String createdBy);
    List<TestDocumentation> findByTestStatus(String status);
    List<TestDocumentation> findByTestType(String testType);
} 