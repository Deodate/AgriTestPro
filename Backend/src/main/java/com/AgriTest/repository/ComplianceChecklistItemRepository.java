package com.AgriTest.repository;

import com.AgriTest.model.ComplianceChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ComplianceChecklistItemRepository extends JpaRepository<ComplianceChecklistItem, Long> {
    // You can add custom query methods here if needed later
    // e.g., List<ComplianceChecklistItem> findByComplianceChecklistId(Long complianceChecklistId);
} 