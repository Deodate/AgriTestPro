package com.AgriTest.service.impl;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.dto.QualityIncidentReportDTO;
import com.AgriTest.mapper.MediaFileMapper;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.Product;
import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.QualityIncidentReportRepository;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.service.QualityIncidentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class QualityIncidentReportServiceImpl implements QualityIncidentReportService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QualityIncidentReportRepository reportRepository;

    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private MediaFileMapper mediaFileMapper;

    @Override
    @Transactional
    public QualityIncidentReport createQualityIncidentReport(QualityIncidentReportDTO reportDTO) {
        // Validate product exists
        Product product = productRepository.findById(reportDTO.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + reportDTO.getProductId()));

        // Create new report
        QualityIncidentReport incidentReport = new QualityIncidentReport();
        incidentReport.setProduct(product);
        incidentReport.setIncidentId(generateUniqueIncidentId());
        incidentReport.setIncidentDate(reportDTO.getIncidentDate());
        incidentReport.setDescription(reportDTO.getDescription());
        incidentReport.setCorrectiveActions(reportDTO.getCorrectiveActions());
        incidentReport.setStatus(
            reportDTO.getStatus() != null ? 
            reportDTO.getStatus() : 
            QualityIncidentReport.IncidentStatus.OPEN
        );

        // Validate before saving
        validateQualityIncidentReport(incidentReport);

        // Save the initial report
        QualityIncidentReport savedReport = reportRepository.save(incidentReport);

        // Handle file uploads if present
        if (reportDTO.getMediaFiles() != null && !reportDTO.getMediaFiles().isEmpty()) {
            List<MediaFile> mediaFiles = new ArrayList<>();
            for (MultipartFile file : reportDTO.getMediaFiles()) {
                // Added third parameter to specify INCIDENT_REPORT type
                MediaFileResponse mediaFileResponse = fileStorageService.storeFile(file, savedReport.getId(), null, "INCIDENT_REPORT");
                
                // Convert DTO to entity
                MediaFile mediaFile = new MediaFile();
                mediaFile.setFileName(mediaFileResponse.getFileName());
                mediaFile.setFilePath(mediaFileResponse.getFileDownloadUri());
                mediaFile.setFileType(mediaFileResponse.getFileType());
                mediaFile.setFileSize(file.getSize());
                mediaFile.setUploadedBy(null); // Set as needed
                mediaFile.setUploadedAt(LocalDateTime.now());
                mediaFile.setIncidentReport(savedReport);
                
                mediaFiles.add(mediaFile);
            }
            savedReport.setMediaFiles(mediaFiles);
        }

        return reportRepository.save(savedReport);
    }

    @Override
    @Transactional
    public QualityIncidentReport updateQualityIncidentReport(String incidentId, QualityIncidentReportDTO reportDTO) {
        // Find existing report
        QualityIncidentReport existingReport = getQualityIncidentReportByIncidentId(incidentId);

        // Update fields if provided
        if (reportDTO.getDescription() != null) {
            existingReport.setDescription(reportDTO.getDescription());
        }
        if (reportDTO.getCorrectiveActions() != null) {
            existingReport.setCorrectiveActions(reportDTO.getCorrectiveActions());
        }
        if (reportDTO.getStatus() != null) {
            existingReport.setStatus(reportDTO.getStatus());
        }

        // Handle file uploads
        if (reportDTO.getMediaFiles() != null && !reportDTO.getMediaFiles().isEmpty()) {
            // Clear existing media files
            existingReport.getMediaFiles().clear();

            // Add new media files
            List<MediaFile> mediaFiles = new ArrayList<>();
            for (MultipartFile file : reportDTO.getMediaFiles()) {
                // Added third parameter to specify INCIDENT_REPORT type
                MediaFileResponse mediaFileResponse = fileStorageService.storeFile(file, existingReport.getId(), null, "INCIDENT_REPORT");
                
                // Convert DTO to entity
                MediaFile mediaFile = new MediaFile();
                mediaFile.setFileName(mediaFileResponse.getFileName());
                mediaFile.setFilePath(mediaFileResponse.getFileDownloadUri());
                mediaFile.setFileType(mediaFileResponse.getFileType());
                mediaFile.setFileSize(file.getSize());
                mediaFile.setUploadedBy(null);
                mediaFile.setUploadedAt(LocalDateTime.now());
                mediaFile.setIncidentReport(existingReport);
                
                mediaFiles.add(mediaFile);
            }
            existingReport.setMediaFiles(mediaFiles);
        }

        // Validate and save
        validateQualityIncidentReport(existingReport);
        return reportRepository.save(existingReport);
    }

    // Remaining methods stay the same as in the previous implementation
    @Override
    public QualityIncidentReport getQualityIncidentReportById(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quality Incident Report not found with ID: " + id));
    }

    @Override
    public QualityIncidentReport getQualityIncidentReportByIncidentId(String incidentId) {
        return reportRepository.findByIncidentId(incidentId)
            .orElseThrow(() -> new RuntimeException("Quality Incident Report not found with Incident ID: " + incidentId));
    }

    @Override
    @Transactional
    public void deleteQualityIncidentReport(String incidentId) {
        QualityIncidentReport report = getQualityIncidentReportByIncidentId(incidentId);
        reportRepository.delete(report);
    }

    @Override
    public List<QualityIncidentReport> getAllQualityIncidentReports() {
        return reportRepository.findAll();
    }

    @Override
    public List<QualityIncidentReport> getQualityIncidentReportsByStatus(QualityIncidentReport.IncidentStatus status) {
        return reportRepository.findByStatus(status);
    }

    @Override
    public List<QualityIncidentReport> getQualityIncidentReportsByProduct(Long productId) {
        return reportRepository.findByProductId(productId);
    }

    // Unique ID generation method
    private String generateUniqueIncidentId() {
        String year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        
        // Find the last incident report for the current year and increment
        List<QualityIncidentReport> recentReports = reportRepository.findAll();
        
        int sequenceNumber = 1;
        for (QualityIncidentReport report : recentReports) {
            if (report.getIncidentId().startsWith("QIR-" + year)) {
                try {
                    int currentSeq = Integer.parseInt(report.getIncidentId().split("-")[2]);
                    sequenceNumber = Math.max(sequenceNumber, currentSeq + 1);
                } catch (Exception e) {
                    // If parsing fails, continue with default sequence
                }
            }
        }
        
        return String.format("QIR-%s-%03d", year, sequenceNumber);
    }

    // Additional validation method
    private void validateQualityIncidentReport(QualityIncidentReport report) {
        if (report.getProduct() == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (report.getIncidentDate() == null) {
            throw new IllegalArgumentException("Incident Date must not be null");
        }
        if (report.getDescription() == null || report.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }
    }
}