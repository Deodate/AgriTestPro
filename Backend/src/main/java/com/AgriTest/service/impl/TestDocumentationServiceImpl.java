package com.AgriTest.service.impl;

import com.AgriTest.dto.TestDocumentationRequest;
import com.AgriTest.dto.TestDocumentationResponse;
import com.AgriTest.model.TestDocumentation;
import com.AgriTest.repository.TestDocumentationRepository;
import com.AgriTest.service.TestDocumentationService;
import com.AgriTest.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestDocumentationServiceImpl implements TestDocumentationService {

    private final TestDocumentationRepository testDocumentationRepository;

    public TestDocumentationServiceImpl(TestDocumentationRepository testDocumentationRepository) {
        this.testDocumentationRepository = testDocumentationRepository;
    }

    @Override
    public TestDocumentationResponse createTestDocumentation(
            TestDocumentationRequest request,
            List<MultipartFile> attachments,
            String username) {
        
        TestDocumentation testDocumentation = new TestDocumentation();
        BeanUtils.copyProperties(request, testDocumentation);
        
        // Set metadata
        testDocumentation.setCreatedBy(username);
        testDocumentation.setCreatedAt(LocalDateTime.now());
        testDocumentation.setUpdatedAt(LocalDateTime.now());
        
        // Handle attachments if present
        if (attachments != null && !attachments.isEmpty()) {
            // Store file paths or process attachments as needed
            String attachmentPaths = attachments.stream()
                .map(file -> saveAttachment(file))
                .collect(Collectors.joining(","));
            testDocumentation.setAttachments(attachmentPaths);
        }
        
        TestDocumentation savedDoc = testDocumentationRepository.save(testDocumentation);
        return convertToResponse(savedDoc);
    }

    @Override
    public TestDocumentationResponse updateTestDocumentation(Long id, TestDocumentationRequest request) {
        TestDocumentation existingDoc = testDocumentationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Test Documentation not found with id: " + id));
        
        BeanUtils.copyProperties(request, existingDoc);
        existingDoc.setUpdatedAt(LocalDateTime.now());
        
        TestDocumentation updatedDoc = testDocumentationRepository.save(existingDoc);
        return convertToResponse(updatedDoc);
    }

    @Override
    public TestDocumentationResponse getTestDocumentationById(Long id) {
        TestDocumentation doc = testDocumentationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Test Documentation not found with id: " + id));
        return convertToResponse(doc);
    }

    @Override
    public List<TestDocumentationResponse> getAllTestDocumentations() {
        return testDocumentationRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteTestDocumentation(Long id) {
        if (!testDocumentationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Test Documentation not found with id: " + id);
        }
        testDocumentationRepository.deleteById(id);
    }

    private TestDocumentationResponse convertToResponse(TestDocumentation doc) {
        TestDocumentationResponse response = new TestDocumentationResponse();
        BeanUtils.copyProperties(doc, response);
        return response;
    }

    private String saveAttachment(MultipartFile file) {
        // Implement file saving logic here
        // For now, just return the filename
        return file.getOriginalFilename();
    }
} 