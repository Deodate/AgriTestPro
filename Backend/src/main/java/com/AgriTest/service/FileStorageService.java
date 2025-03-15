// File: src/main/java/com/AgriTest/service/FileStorageService.java
package com.AgriTest.service;

import com.AgriTest.dto.MediaFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    MediaFileResponse storeFile(MultipartFile file, Long testResultId, Long userId);
    MediaFileResponse storeFile(MultipartFile file, Long associatedId, Long userId, String associationType);
    Resource loadFileAsResource(String fileName);
    void deleteFile(Long id);
}