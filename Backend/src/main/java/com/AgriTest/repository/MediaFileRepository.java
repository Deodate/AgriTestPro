package com.AgriTest.repository;

import com.AgriTest.model.Announcement;
import com.AgriTest.model.Expense;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.model.TestResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    List<MediaFile> findByTestResultId(Long testResultId);
    
    List<MediaFile> findByFileType(String fileType);
    
    List<MediaFile> findByUploadedBy(Long userId);
     
    List<MediaFile> findByIncidentReport(QualityIncidentReport incidentReport);
    
    List<MediaFile> findByAnnouncement(Announcement announcement);
    
    List<MediaFile> findByTestResult(TestResult testResult);
    
    // Expense-related methods
    List<MediaFile> findByExpense(Expense expense);
    
    List<MediaFile> findByExpenseId(Long expenseId);
    
    List<MediaFile> findByAssociationType(String associationType);
    
    List<MediaFile> findByExpenseIdAndAssociationType(Long expenseId, String associationType);
    
    // This is the missing method
    List<MediaFile> findByExpenseAndAssociationType(Expense expense, String associationType);
}