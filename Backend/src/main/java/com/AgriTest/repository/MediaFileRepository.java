package com.AgriTest.repository;

import com.AgriTest.model.Announcement;
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
    
    // Add this method to find by Announcement
    List<MediaFile> findByAnnouncement(Announcement announcement);
    
    // Add this method if needed
    List<MediaFile> findByTestResult(TestResult testResult);
}