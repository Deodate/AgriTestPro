package com.AgriTest.service;

import com.AgriTest.dto.MediaFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    /**
     * Store a file with default association type (TEST_RESULT)
     */
    MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId);
    
    /**
     * Store a file with specified association type
     */
    MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId, String associationType);
    
    /**
     * Load a file as a resource
     */
    Resource loadFileAsResource(String fileName);
    
    /**
     * Delete a file by its ID
     */
    void deleteFile(Long id);
    
    /**
     * Delete or disassociate files linked to a specific entity
     */
    void deleteFilesByEntityIdAndType(Long entityId, String entityType);
    
    /**
     * Get a file by its ID
     */
    MediaFileResponse getFileById(Long id);
    
    /**
     * Get all files associated with an entity
     */
    List<MediaFileResponse> getFilesByEntityIdAndType(Long entityId, String entityType);
    
    /**
     * Store receipt for an expense
     */
    MediaFileResponse storeExpenseReceipt(MultipartFile file, Long expenseId, Long userId);
    
    /**
     * Get receipt for an expense
     */
    MediaFileResponse getExpenseReceipt(Long expenseId);
    
    /**
     * Delete receipt for an expense
     */
    void deleteExpenseReceipt(Long expenseId);
}