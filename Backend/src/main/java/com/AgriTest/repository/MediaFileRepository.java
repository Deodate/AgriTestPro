package com.AgriTest.repository;

import com.AgriTest.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    // Basic find methods
    List<MediaFile> findByFileType(String fileType);
    List<MediaFile> findByUploadedBy(Long userId);
    
    // Test Result related methods
    List<MediaFile> findByTestResultId(Long testResultId);
    List<MediaFile> findByTestResult(TestResult testResult);
    
    // Incident Report related methods
    List<MediaFile> findByIncidentReportId(Long incidentReportId);
    List<MediaFile> findByIncidentReport(QualityIncidentReport incidentReport);
    
    // Announcement related methods
    List<MediaFile> findByAnnouncementId(Long announcementId);
    List<MediaFile> findByAnnouncement(Announcement announcement);
    
    // Expense related methods
    List<MediaFile> findByExpenseId(Long expenseId);
    List<MediaFile> findByExpense(Expense expense);
    List<MediaFile> findByExpenseIdAndAssociationType(Long expenseId, String associationType);
    
    // Field Activity related methods
    List<MediaFile> findByFieldActivityId(Long fieldActivityId);
    List<MediaFile> findByFieldActivity(FieldActivity fieldActivity);
    
    // Association Type methods
    List<MediaFile> findByAssociationType(String associationType);
    
    // Custom query methods
    @Query("SELECT mf FROM MediaFile mf WHERE mf.uploadedAt BETWEEN :startDate AND :endDate")
    List<MediaFile> findByUploadDateRange(
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    @Query("SELECT mf FROM MediaFile mf WHERE mf.uploadedBy = :userId AND mf.associationType = :associationType")
    List<MediaFile> findByUserAndAssociationType(
        Long userId, 
        String associationType
    );
    
    // Find most recent file for a specific entity and association type
    @Query("SELECT mf FROM MediaFile mf " +
           "WHERE mf.expense.id = :expenseId AND mf.associationType = :associationType " +
           "ORDER BY mf.uploadedAt DESC")
    Optional<MediaFile> findMostRecentExpenseReceipt(
        Long expenseId, 
        String associationType
    );
    
    // Count methods
    long countByFileType(String fileType);
    long countByUploadedBy(Long userId);
    long countByAssociationType(String associationType);
    
    // Exists methods
    boolean existsByFileName(String fileName);
    boolean existsByFilePathAndUploadedBy(String filePath, Long uploadedBy);
    
    // Complex filtering method
    @Query("SELECT mf FROM MediaFile mf WHERE " +
           "(:fileType IS NULL OR mf.fileType = :fileType) AND " +
           "(:uploadedBy IS NULL OR mf.uploadedBy = :uploadedBy) AND " +
           "(:associationType IS NULL OR mf.associationType = :associationType)")
    List<MediaFile> findByFilter(
        String fileType,
        Long uploadedBy,
        String associationType
    );
}