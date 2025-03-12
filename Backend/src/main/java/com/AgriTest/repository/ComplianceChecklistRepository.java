// File: src/main/java/com/AgriTest/repository/ComplianceChecklistRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.ComplianceChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplianceChecklistRepository extends JpaRepository<ComplianceChecklist, Long> {
    List<ComplianceChecklist> findByProductId(Long productId);
    
    List<ComplianceChecklist> findByReviewerName(String reviewerName);
    
    List<ComplianceChecklist> findByReviewDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ComplianceChecklist> findByOverallStatus(ComplianceChecklist.ComplianceStatus status);
    
    @Query("SELECT c FROM ComplianceChecklist c WHERE c.productId = :productId AND c.reviewDate BETWEEN :startDate AND :endDate")
    List<ComplianceChecklist> findByProductIdAndDateRange(
        @Param("productId") Long productId, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
}
