package com.AgriTest.service.impl;

import com.AgriTest.dto.QualityIncidentReportRequest;
import com.AgriTest.dto.QualityIncidentReportResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.QualityIncidentReportMapper;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.Product;
import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import com.AgriTest.repository.MediaFileRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.QualityIncidentReportRepository;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.service.QualityIncidentReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QualityIncidentReportServiceImpl implements QualityIncidentReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(QualityIncidentReportServiceImpl.class);
    
    @Autowired
    private QualityIncidentReportRepository qualityIncidentReportRepository;
    
    @Autowired
    private MediaFileRepository mediaFileRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private QualityIncidentReportMapper qualityIncidentReportMapper;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Override
    @Transactional
    public QualityIncidentReportResponse createQualityIncidentReport(QualityIncidentReportRequest request, Long userId) {
        // Check if incident ID already exists
        if (qualityIncidentReportRepository.existsByIncidentId(request.getIncidentId())) {
            throw new DataIntegrityViolationException("Incident ID already exists: " + request.getIncidentId());
        }
        
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Create incident report entity
        QualityIncidentReport report = qualityIncidentReportMapper.toEntity(request, product);
        
        // Save the incident report
        QualityIncidentReport savedReport = qualityIncidentReportRepository.save(report);
        
        // Process media files if any
        if (request.getMediaFiles() != null && !request.getMediaFiles().isEmpty()) {
            for (MultipartFile file : request.getMediaFiles()) {
                try {
                    // The actual file will be stored using the FileStorageService
                    fileStorageService.storeFile(file, savedReport.getId(), userId, "INCIDENT_REPORT");
                } catch (Exception e) {
                    logger.error("Failed to process media file: {}", file.getOriginalFilename(), e);
                }
            }
        }
        
        // Retrieve the updated report with media files
        QualityIncidentReport updatedReport = qualityIncidentReportRepository.findById(savedReport.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Report not found after saving"));
        
        // Return the DTO
        return qualityIncidentReportMapper.toDto(updatedReport);
    }
    
    @Override
    @Transactional
    public QualityIncidentReportResponse addMediaFiles(Long reportId, List<MultipartFile> files, Long userId) {
        // Find the report
        QualityIncidentReport report = qualityIncidentReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with id: " + reportId));
        
        // Process media files
        for (MultipartFile file : files) {
            try {
                fileStorageService.storeFile(file, report.getId(), userId, "INCIDENT_REPORT");
            } catch (Exception e) {
                logger.error("Failed to process media file: {}", file.getOriginalFilename(), e);
            }
        }
        
        // Retrieve the updated report with media files
        QualityIncidentReport updatedReport = qualityIncidentReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found after updating"));
        
        // Return the DTO
        return qualityIncidentReportMapper.toDto(updatedReport);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<QualityIncidentReportResponse> getAllQualityIncidentReports(Pageable pageable) {
        return qualityIncidentReportRepository.findAll(pageable)
                .map(qualityIncidentReportMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QualityIncidentReportResponse> getAllQualityIncidentReports() {
        return qualityIncidentReportRepository.findAll().stream()
                .map(qualityIncidentReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public QualityIncidentReportResponse getQualityIncidentReportById(Long id) {
        QualityIncidentReport report = qualityIncidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with id: " + id));
        
        return qualityIncidentReportMapper.toDto(report);
    }
    
    @Override
    @Transactional(readOnly = true)
    public QualityIncidentReportResponse getQualityIncidentReportByIncidentId(String incidentId) {
        QualityIncidentReport report = qualityIncidentReportRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with incident id: " + incidentId));
        
        return qualityIncidentReportMapper.toDto(report);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QualityIncidentReportResponse> getQualityIncidentReportsByProductId(Long productId) {
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return qualityIncidentReportRepository.findByProductId(productId).stream()
                .map(qualityIncidentReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QualityIncidentReportResponse> getQualityIncidentReportsByStatus(IncidentStatus status) {
        return qualityIncidentReportRepository.findByStatus(status).stream()
                .map(qualityIncidentReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QualityIncidentReportResponse> getQualityIncidentReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Validate input dates
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start and end dates must be provided");
        }
        
        // Ensure start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        // Retrieve reports within the date range
        return qualityIncidentReportRepository.findByIncidentDateBetween(startDate, endDate).stream()
                .map(qualityIncidentReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<QualityIncidentReportResponse> searchQualityIncidentReports(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        
        return qualityIncidentReportRepository.searchByKeyword(keyword).stream()
                .map(qualityIncidentReportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public QualityIncidentReportResponse updateQualityIncidentReport(Long id, QualityIncidentReportRequest request) {
        // Find the existing report
        QualityIncidentReport existingReport = qualityIncidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with id: " + id));
        
        // Check if incident ID already exists and it's not the current one
        if (!existingReport.getIncidentId().equals(request.getIncidentId()) && 
            qualityIncidentReportRepository.existsByIncidentId(request.getIncidentId())) {
            throw new DataIntegrityViolationException("Incident ID already exists: " + request.getIncidentId());
        }
        
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Update the existing report
        qualityIncidentReportMapper.updateEntityFromRequest(existingReport, request, product);
        
        // Save the updated report
        QualityIncidentReport updatedReport = qualityIncidentReportRepository.save(existingReport);
        
        // Return the updated DTO
        return qualityIncidentReportMapper.toDto(updatedReport);
    }
    
    @Override
    @Transactional
    public QualityIncidentReportResponse updateQualityIncidentReportStatus(Long id, IncidentStatus status) {
        // Find the report
        QualityIncidentReport report = qualityIncidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with id: " + id));
        
        // Update status
        report.setStatus(status);
        
        // Save the updated report
        QualityIncidentReport updatedReport = qualityIncidentReportRepository.save(report);
        
        // Return the updated DTO
        return qualityIncidentReportMapper.toDto(updatedReport);
    }
    
    @Override
    @Transactional
    public void deleteQualityIncidentReport(Long id) {
        // Find the report
        QualityIncidentReport report = qualityIncidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality incident report not found with id: " + id));
        
        // Get all associated media files
        List<MediaFile> mediaFiles = report.getMediaFiles();
        
        // Delete all media files using the FileStorageService
        for (MediaFile mediaFile : mediaFiles) {
            try {
                fileStorageService.deleteFile(mediaFile.getId());
            } catch (Exception e) {
                logger.error("Error deleting file with ID: {}", mediaFile.getId(), e);
            }
        }
        
        // Delete the report
        qualityIncidentReportRepository.delete(report);
    }
    
    @Override
    public boolean existsByIncidentId(String incidentId) {
        return qualityIncidentReportRepository.existsByIncidentId(incidentId);
    }
}