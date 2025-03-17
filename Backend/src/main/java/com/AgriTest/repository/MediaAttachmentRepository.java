package com.AgriTest.repository;

import com.AgriTest.model.MediaAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaAttachmentRepository extends JpaRepository<MediaAttachment, Long> {
    
    List<MediaAttachment> findByIncidentReportId(Long incidentReportId);
    
    void deleteByIncidentReportId(Long incidentReportId);

    // Add expense-related methods
    List<MediaAttachment> findByExpenseId(Long expenseId);
    
    void deleteByExpenseId(Long expenseId);
    
    List<MediaAttachment> findByAttachmentType(String attachmentType);
    
    List<MediaAttachment> findByExpenseIdAndAttachmentType(Long expenseId, String attachmentType);
}