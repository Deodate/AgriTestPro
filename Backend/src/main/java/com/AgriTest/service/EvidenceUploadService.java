package com.AgriTest.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;

public interface EvidenceUploadService {
    void uploadEvidence(
        MultipartFile file,
        Long testCaseId,
        String mediaType,
        String description,
        String takenBy,
        Date dateCaptured
    ) throws Exception;

    // Add other service methods related to evidence if needed
    List<com.AgriTest.model.EvidenceUpload> getAllEvidence();

    void deleteEvidence(Long id);

    void updateEvidence(Long id,
                        MultipartFile file,
                        Long testCaseId,
                        String mediaType,
                        String description,
                        String takenBy,
                        Date dateCaptured) throws Exception;

    com.AgriTest.model.EvidenceUpload getEvidenceById(Long id);
} 