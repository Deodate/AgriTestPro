package com.AgriTest.service.impl;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.exception.FileNotFoundException;
import com.AgriTest.exception.FileStorageException;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.MediaFileMapper;
import com.AgriTest.model.Announcement;
import com.AgriTest.model.Expense;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.model.TestResult;
import com.AgriTest.repository.AnnouncementRepository;
import com.AgriTest.repository.ExpenseRepository;
import com.AgriTest.repository.MediaFileRepository;
import com.AgriTest.repository.QualityIncidentReportRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.service.FileStorageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path fileStorageLocation;
    private final MediaFileRepository mediaFileRepository;
    private final TestResultRepository testResultRepository;
    private final QualityIncidentReportRepository qualityIncidentReportRepository;
    private final AnnouncementRepository announcementRepository;
    private final ExpenseRepository expenseRepository;
    private final MediaFileMapper mediaFileMapper;

    @Autowired
    public FileStorageServiceImpl(
            @Value("${file.upload-dir}") String uploadDir,
            MediaFileRepository mediaFileRepository,
            TestResultRepository testResultRepository,
            QualityIncidentReportRepository qualityIncidentReportRepository,
            AnnouncementRepository announcementRepository,
            ExpenseRepository expenseRepository,
            MediaFileMapper mediaFileMapper) {
        
        this.mediaFileRepository = mediaFileRepository;
        this.testResultRepository = testResultRepository;
        this.qualityIncidentReportRepository = qualityIncidentReportRepository;
        this.announcementRepository = announcementRepository;
        this.expenseRepository = expenseRepository;
        this.mediaFileMapper = mediaFileMapper;
        
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId) {
        return storeFile(file, associatedId, userId, "TEST_RESULT");
    }
    
    @Override
    public MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId, String associationType) {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }
        
        // Get the original file name and clean it
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Check if the filename contains invalid characters
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFilename);
        }
        
        // Generate a unique filename to prevent overwriting existing files
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        
        // File path relative to storage location
        String filePath = uniqueFilename;
        
        try {
            // Copy the file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(originalFilename);
            mediaFile.setFileType(file.getContentType());
            mediaFile.setFilePath(filePath);
            mediaFile.setFileSize(file.getSize());
            mediaFile.setUploadedBy(userId);
            mediaFile.setAssociationType(associationType);
            
            // Associate with different types based on association type
            switch (associationType) {
                case "TEST_RESULT":
                    TestResult testResult = testResultRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("TestResult not found with id: " + associatedId));
                    mediaFile.setTestResult(testResult);
                    mediaFile.setIncidentReport(null);
                    mediaFile.setAnnouncement(null);
                    mediaFile.setExpense(null);
                    break;
                case "INCIDENT_REPORT":
                    QualityIncidentReport incidentReport = qualityIncidentReportRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("QualityIncidentReport not found with id: " + associatedId));
                    mediaFile.setIncidentReport(incidentReport);
                    mediaFile.setTestResult(null);
                    mediaFile.setAnnouncement(null);
                    mediaFile.setExpense(null);
                    break;
                case "ANNOUNCEMENT":
                    Announcement announcement = announcementRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + associatedId));
                    mediaFile.setAnnouncement(announcement);
                    mediaFile.setTestResult(null);
                    mediaFile.setIncidentReport(null);
                    mediaFile.setExpense(null);
                    break;
                case "EXPENSE_RECEIPT":
                    if (associatedId != 0L) { // For existing expenses
                        Expense expense = expenseRepository.findById(associatedId)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + associatedId));
                        mediaFile.setExpense(expense);
                    }
                    mediaFile.setTestResult(null);
                    mediaFile.setIncidentReport(null);
                    mediaFile.setAnnouncement(null);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid association type: " + associationType);
            }
            
            MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);
            MediaFileResponse response = mediaFileMapper.toDto(savedMediaFile);
            
            // Add download URI to the response
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(savedMediaFile.getId().toString())
                    .toUriString();
            response.setFileDownloadUri(fileDownloadUri);
            
            return response;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFilename + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found: " + fileName, ex);
        }
    }

    @Override
    public MediaFileResponse getFileById(Long id) {
        // Find the MediaFile entity
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaFile not found with id: " + id));
        
        // Convert to DTO
        MediaFileResponse response = mediaFileMapper.toDto(mediaFile);
        
        // Add download URI to the response
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(mediaFile.getFilePath())
                .toUriString();
        response.setFileDownloadUri(fileDownloadUri);
        
        return response;
    }

    @Override
    public void deleteFile(Long id) {
        // Find the MediaFile entity
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaFile not found with id: " + id));
        
        try {
            // Delete the file from the filesystem
            Path filePath = this.fileStorageLocation.resolve(mediaFile.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
            
            // Delete the database record
            mediaFileRepository.delete(mediaFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + mediaFile.getFileName(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteFilesByEntityIdAndType(Long entityId, String entityType) {
        log.info("Executing deleteFilesByEntityIdAndType for {} with ID: {}", entityType, entityId);
        
        List<MediaFile> mediaFiles;
        
        switch (entityType) {
            case "ANNOUNCEMENT":
                Announcement announcement = announcementRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + entityId));
                mediaFiles = mediaFileRepository.findByAnnouncement(announcement);
                log.info("Found {} media files for announcement with ID: {}", mediaFiles.size(), entityId);
                break;
            case "TEST_RESULT":
                TestResult testResult = testResultRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("TestResult not found with id: " + entityId));
                mediaFiles = mediaFileRepository.findByTestResult(testResult);
                break;
            case "INCIDENT_REPORT":
                QualityIncidentReport incidentReport = qualityIncidentReportRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("QualityIncidentReport not found with id: " + entityId));
                mediaFiles = mediaFileRepository.findByIncidentReport(incidentReport);
                break;
            case "EXPENSE_RECEIPT":
                Expense expense = expenseRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + entityId));
                mediaFiles = mediaFileRepository.findByExpenseAndAssociationType(expense, "EXPENSE_RECEIPT");
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
        
        for (MediaFile mediaFile : mediaFiles) {
            log.info("Processing media file: {} (ID: {})", mediaFile.getFileName(), mediaFile.getId());
            
            // Set entity reference to null based on type to break the foreign key constraint
            if ("ANNOUNCEMENT".equals(entityType)) {
                mediaFile.setAnnouncement(null);
            } else if ("TEST_RESULT".equals(entityType)) {
                mediaFile.setTestResult(null);
            } else if ("INCIDENT_REPORT".equals(entityType)) {
                mediaFile.setIncidentReport(null);
            } else if ("EXPENSE_RECEIPT".equals(entityType)) {
                mediaFile.setExpense(null);
            }
            
            // Update the media file to remove association
            mediaFileRepository.save(mediaFile);
            log.info("Updated media file to remove association: {}", mediaFile.getId());
        }
        
        log.info("Finished processing media files for {} with ID: {}", entityType, entityId);
    }
    
    @Override
    public List<MediaFileResponse> getFilesByEntityIdAndType(Long entityId, String entityType) {
        List<MediaFile> mediaFiles;
        
        switch (entityType) {
            case "TEST_RESULT":
                mediaFiles = mediaFileRepository.findByTestResultId(entityId);
                break;
            case "INCIDENT_REPORT":
                mediaFiles = mediaFileRepository.findByIncidentReport(
                    qualityIncidentReportRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("QualityIncidentReport not found with id: " + entityId))
                );
                break;
            case "ANNOUNCEMENT":
                mediaFiles = mediaFileRepository.findByAnnouncement(
                    announcementRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + entityId))
                );
                break;
            case "EXPENSE_RECEIPT":
                mediaFiles = mediaFileRepository.findByExpenseIdAndAssociationType(entityId, "EXPENSE_RECEIPT");
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
        
        return mediaFiles.stream()
                .map(mediaFile -> {
                    MediaFileResponse response = mediaFileMapper.toDto(mediaFile);
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/files/")
                            .path(mediaFile.getId().toString())
                            .toUriString();
                    response.setFileDownloadUri(fileDownloadUri);
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public MediaFileResponse storeExpenseReceipt(MultipartFile file, Long expenseId, Long userId) {
        return storeFile(file, expenseId, userId, "EXPENSE_RECEIPT");
    }
    
    @Override
    public MediaFileResponse getExpenseReceipt(Long expenseId) {
        List<MediaFile> receipts = mediaFileRepository.findByExpenseIdAndAssociationType(expenseId, "EXPENSE_RECEIPT");
        
        if (receipts.isEmpty()) {
            throw new ResourceNotFoundException("Receipt not found for expense with id: " + expenseId);
        }
        
        // Get the most recent receipt
        MediaFile receipt = receipts.stream()
                .sorted((a, b) -> b.getUploadedAt().compareTo(a.getUploadedAt()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for expense with id: " + expenseId));
        
        MediaFileResponse response = mediaFileMapper.toDto(receipt);
        
        // Add download URI to the response
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(receipt.getId().toString())
                .toUriString();
        response.setFileDownloadUri(fileDownloadUri);
        
        return response;
    }
    
    @Override
    @Transactional
    public void deleteExpenseReceipt(Long expenseId) {
        List<MediaFile> receipts = mediaFileRepository.findByExpenseIdAndAssociationType(expenseId, "EXPENSE_RECEIPT");
        
        for (MediaFile receipt : receipts) {
            try {
                // Delete the file from the filesystem
                Path filePath = this.fileStorageLocation.resolve(receipt.getFilePath()).normalize();
                Files.deleteIfExists(filePath);
                
                // Delete the database record
                mediaFileRepository.delete(receipt);
            } catch (IOException ex) {
                throw new FileStorageException("Could not delete receipt: " + receipt.getFileName(), ex);
            }
        }
    }
}