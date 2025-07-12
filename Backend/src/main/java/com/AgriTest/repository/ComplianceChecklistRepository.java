package com.AgriTest.repository;

import com.AgriTest.model.ComplianceChecklist;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplianceChecklistRepository extends JpaRepository<ComplianceChecklist, Long> {
    // Find checklists by product ID
    List<ComplianceChecklist> findByProductId(Long productId);
    
    // Find checklists by reviewer name
    List<ComplianceChecklist> findByReviewerNameContainingIgnoreCase(String reviewerName);
    
    // Find checklists by review date range
    List<ComplianceChecklist> findByReviewDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find checklists by compliance status
    List<ComplianceChecklist> findByOverallStatus(ComplianceChecklist.ComplianceStatus status);
    
    // Find most recent checklists
    @Query("SELECT c FROM ComplianceChecklist c ORDER BY c.reviewDate DESC")
    List<ComplianceChecklist> findMostRecentChecklists(Pageable pageable);
    
    // Add a custom query method to find the most recent checklists with limit
    default List<ComplianceChecklist> findMostRecentChecklists(int limit) {
        return findMostRecentChecklists(PageRequest.of(0, limit));
    }
}