package com.AgriTest.service;

import com.AgriTest.dto.MediaFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    MediaFileResponse storeFile(MultipartFile file, Long testResultId, Long userId);
    MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId, String associationType);
    Resource loadFileAsResource(String fileName);
    void deleteFile(Long id);
    
    /**
     * Delete or disassociate files linked to a specific entity
     * 
     * @param entityId The ID of the entity
     * @param entityType The type of entity (e.g., "ANNOUNCEMENT", "TEST_RESULT", "INCIDENT_REPORT")
     */
    void deleteFilesByEntityIdAndType(Long entityId, String entityType);
}