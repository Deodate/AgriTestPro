package com.AgriTest.service.impl;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.exception.FileNotFoundException;
import com.AgriTest.exception.FileStorageException;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.MediaFileMapper;
import com.AgriTest.model.*;
import com.AgriTest.repository.*;
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

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private QualityIncidentReportRepository qualityIncidentReportRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private FieldActivityRepository fieldActivityRepository;

    @Autowired
    private MediaFileMapper mediaFileMapper;

    @Autowired
    private TestCaseTrialPhaseRepository testCaseTrialPhaseRepository;

    @Autowired
    public FileStorageServiceImpl(
            @Value("${file.upload-dir}") String uploadDir) {
        
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
        // Validate file
        log.info("Storing file with associationType: {}", associationType);
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }

        // Sanitize filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Invalid file path: " + originalFilename);
        }

        // Generate unique filename
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        
        try {
            // Save file to storage location
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create MediaFile entity
            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(originalFilename);
            mediaFile.setFileType(file.getContentType());
            mediaFile.setFilePath(uniqueFilename);
            mediaFile.setFileSize(file.getSize());
            mediaFile.setUploadedBy(userId);
            mediaFile.setAssociationType(associationType);

            // Associate with appropriate entity
            switch (associationType) {
                case "TEST_RESULT":
                    TestResult testResult = testResultRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Test Result not found"));
                    mediaFile.setTestResult(testResult);
                    break;
                case "TEST_CASE":
                    TestResult testCase = testResultRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Test Case not found"));
                    mediaFile.setTestResult(testCase);
                    break;
                case "INCIDENT_REPORT":
                    QualityIncidentReport incidentReport = qualityIncidentReportRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Incident Report not found"));
                    mediaFile.setIncidentReport(incidentReport);
                    break;
                case "ANNOUNCEMENT":
                    Announcement announcement = announcementRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Announcement not found"));
                    mediaFile.setAnnouncement(announcement);
                    break;
                case "EXPENSE_RECEIPT":
                    Expense expense = expenseRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
                    mediaFile.setExpense(expense);
                    break;
                case "FIELD_ACTIVITY_ATTACHMENT":
                    FieldActivity fieldActivity = fieldActivityRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found"));
                    mediaFile.setFieldActivity(fieldActivity);
                    break;
                case "TRIAL_PHASE":
                    TestCaseTrialPhase trialPhase = testCaseTrialPhaseRepository.findById(associatedId)
                            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found"));
                    mediaFile.setTestCaseTrialPhase(trialPhase);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported association type: " + associationType);
            }

            // Save media file
            MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);

            // Generate download URI
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(savedMediaFile.getId().toString())
                    .toUriString();

            // Map to response DTO
            MediaFileResponse response = mediaFileMapper.toDto(savedMediaFile);
            response.setFileDownloadUri(fileDownloadUri);

            return response;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFilename, ex);
        }
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
        
        // Generate download URI
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(receipt.getId().toString())
                .toUriString();

        MediaFileResponse response = mediaFileMapper.toDto(receipt);
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
                log.error("Could not delete receipt: " + receipt.getFileName(), ex);
                throw new FileStorageException("Could not delete receipt: " + receipt.getFileName(), ex);
            }
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
    @Transactional
    public void deleteFile(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        try {
            // Delete physical file
            Path filePath = this.fileStorageLocation.resolve(mediaFile.getFilePath());
            Files.deleteIfExists(filePath);

            // Delete database record
            mediaFileRepository.delete(mediaFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file", ex);
        }
    }

    @Override
    @Transactional
    public void deleteFilesByEntityIdAndType(Long entityId, String entityType) {
        List<MediaFile> mediaFiles = getMediaFilesByEntityType(entityId, entityType);
        
        for (MediaFile mediaFile : mediaFiles) {
            try {
                // Delete physical file
                Path filePath = this.fileStorageLocation.resolve(mediaFile.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                log.warn("Could not delete physical file: {}", mediaFile.getFileName());
            }
        }

        // Bulk delete from database
        mediaFileRepository.deleteAll(mediaFiles);
    }

    private List<MediaFile> getMediaFilesByEntityType(Long entityId, String entityType) {
        switch (entityType) {
            case "TEST_RESULT":
                TestResult testResult = testResultRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Test Result not found"));
                return mediaFileRepository.findByTestResult(testResult);
            
            case "TEST_CASE":
                TestResult testCase = testResultRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Test Case not found"));
                return mediaFileRepository.findByTestResult(testCase);
            
            case "INCIDENT_REPORT":
                QualityIncidentReport incidentReport = qualityIncidentReportRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Incident Report not found"));
                return mediaFileRepository.findByIncidentReport(incidentReport);
            
            case "ANNOUNCEMENT":
                Announcement announcement = announcementRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Announcement not found"));
                return mediaFileRepository.findByAnnouncement(announcement);
            
            case "EXPENSE_RECEIPT":
                Expense expense = expenseRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
                return mediaFileRepository.findByExpense(expense);
            
            case "FIELD_ACTIVITY_ATTACHMENT":
                FieldActivity fieldActivity = fieldActivityRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found"));
                return mediaFileRepository.findByFieldActivity(fieldActivity);
            
            case "TRIAL_PHASE":
                TestCaseTrialPhase trialPhase = testCaseTrialPhaseRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found"));
                return mediaFileRepository.findByTestCaseTrialPhase(trialPhase);
            
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        }
    }

    @Override
    public MediaFileResponse getFileById(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));

        // Generate download URI
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(mediaFile.getId().toString())
                .toUriString();

        MediaFileResponse response = mediaFileMapper.toDto(mediaFile);
        response.setFileDownloadUri(fileDownloadUri);

        return response;
    }

    @Override
    public List<MediaFileResponse> getFilesByEntityIdAndType(Long entityId, String entityType) {
        List<MediaFile> mediaFiles = getMediaFilesByEntityType(entityId, entityType);

        return mediaFiles.stream()
                .map(mediaFile -> {
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/files/")
                            .path(mediaFile.getId().toString())
                            .toUriString();

                    MediaFileResponse response = mediaFileMapper.toDto(mediaFile);
                    response.setFileDownloadUri(fileDownloadUri);
                    return response;
                })
                .collect(Collectors.toList());
    }
}