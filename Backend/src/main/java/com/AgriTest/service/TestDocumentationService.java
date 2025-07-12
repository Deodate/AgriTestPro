package com.AgriTest.service;

import com.AgriTest.dto.TestDocumentationRequest;
import com.AgriTest.dto.TestDocumentationResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface TestDocumentationService {
    TestDocumentationResponse createTestDocumentation(TestDocumentationRequest request, List<MultipartFile> attachments, String username);
    TestDocumentationResponse updateTestDocumentation(Long id, TestDocumentationRequest request);
    TestDocumentationResponse getTestDocumentationById(Long id);
    List<TestDocumentationResponse> getAllTestDocumentations();
    void deleteTestDocumentation(Long id);
} 