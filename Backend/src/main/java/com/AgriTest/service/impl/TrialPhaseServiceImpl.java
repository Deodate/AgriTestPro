package com.AgriTest.service.impl;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.dto.TrialPhaseResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.FileAttachment;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TrialPhase;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TrialPhaseRepository;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.service.TrialPhaseService;
import com.AgriTest.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrialPhaseServiceImpl implements TrialPhaseService {
    private static final Logger logger = LoggerFactory.getLogger(TrialPhaseServiceImpl.class);

    @Autowired
    private TrialPhaseRepository trialPhaseRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    @Transactional
    public TrialPhaseResponse createTrialPhase(TrialPhaseRequest request) {
        logger.info("Creating new trial phase for test case: {}", request.getTestCaseId());
        
        TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + request.getTestCaseId()));

        TrialPhase trialPhase = new TrialPhase();
        updateTrialPhaseFromRequest(trialPhase, request);
        trialPhase.setTestCase(testCase);
        trialPhase.setCreatedBy(SecurityUtils.getCurrentUsername());
        
        // Handle file attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<FileAttachment> attachments = handleFileAttachments(request.getAttachments());
            trialPhase.setAttachments(attachments);
        }

        TrialPhase savedPhase = trialPhaseRepository.save(trialPhase);
        return mapToResponse(savedPhase);
    }

    @Override
    @Transactional
    public TrialPhaseResponse updateTrialPhase(Long id, TrialPhaseRequest request) {
        logger.info("Updating trial phase: {}", id);
        
        TrialPhase existingPhase = trialPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trial phase not found with id: " + id));

        updateTrialPhaseFromRequest(existingPhase, request);
        existingPhase.setUpdatedBy(SecurityUtils.getCurrentUsername());

        // Handle file attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<FileAttachment> newAttachments = handleFileAttachments(request.getAttachments());
            existingPhase.getAttachments().addAll(newAttachments);
        }

        TrialPhase updatedPhase = trialPhaseRepository.save(existingPhase);
        return mapToResponse(updatedPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public TrialPhaseResponse getTrialPhaseById(Long id) {
        logger.info("Fetching trial phase: {}", id);
        TrialPhase trialPhase = trialPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trial phase not found with id: " + id));
        return mapToResponse(trialPhase);
    }

    @Override
    @Transactional
    public void deleteTrialPhase(Long id) {
        logger.info("Deleting trial phase: {}", id);
        TrialPhase trialPhase = trialPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trial phase not found with id: " + id));
        
        // Delete associated files first
        if (!trialPhase.getAttachments().isEmpty()) {
            trialPhase.getAttachments().forEach(attachment -> 
                fileStorageService.deleteFile(attachment.getFilePath()));
        }
        
        trialPhaseRepository.delete(trialPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialPhaseResponse> getAllTrialPhases() {
        logger.info("Fetching all trial phases");
        return trialPhaseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrialPhaseResponse> getTrialPhasesPaginated(Pageable pageable) {
        return trialPhaseRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialPhaseResponse> getTrialPhasesByTestCase(Long testCaseId) {
        logger.info("Fetching trial phases for test case: {}", testCaseId);
        return trialPhaseRepository.findByTestCaseId(testCaseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialPhaseResponse> getTrialPhasesByStatus(String status) {
        logger.info("Fetching trial phases with status: {}", status);
        return trialPhaseRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrialPhaseResponse updateTrialPhaseStatus(Long id, String status) {
        logger.info("Updating status of trial phase {} to: {}", id, status);
        TrialPhase trialPhase = trialPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trial phase not found with id: " + id));
        
        trialPhase.setStatus(status);
        trialPhase.setUpdatedBy(SecurityUtils.getCurrentUsername());
        
        return mapToResponse(trialPhaseRepository.save(trialPhase));
    }

    @Override
    @Transactional
    public List<TrialPhaseResponse> createBulkTrialPhases(List<TrialPhaseRequest> requests) {
        logger.info("Creating {} trial phases in bulk", requests.size());
        return requests.stream()
                .map(this::createTrialPhase)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBulkTrialPhases(List<Long> ids) {
        logger.info("Deleting {} trial phases in bulk", ids.size());
        ids.forEach(this::deleteTrialPhase);
    }

    private void updateTrialPhaseFromRequest(TrialPhase trialPhase, TrialPhaseRequest request) {
        trialPhase.setPhaseName(request.getPhaseName());
        trialPhase.setDateOfPhase(request.getDateOfPhase());
        trialPhase.setObservations(request.getObservations());
        trialPhase.setTestDataEntry(request.getTestDataEntry());
        trialPhase.setWeatherTemperature(request.getWeatherTemperature());
        trialPhase.setWeatherHumidity(request.getWeatherHumidity());
        trialPhase.setWeatherRainfall(request.getWeatherRainfall());
        trialPhase.setAdditionalComments(request.getAdditionalComments());
        if (request.getStatus() != null) {
            trialPhase.setStatus(request.getStatus());
        }
    }

    private List<FileAttachment> handleFileAttachments(List<MultipartFile> files) {
        List<FileAttachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            String filePath = fileStorageService.storeFile(file);
            FileAttachment attachment = new FileAttachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(filePath);
            attachment.setFileType(file.getContentType());
            attachments.add(attachment);
        }
        return attachments;
    }

    private TrialPhaseResponse mapToResponse(TrialPhase trialPhase) {
        TrialPhaseResponse response = new TrialPhaseResponse();
        response.setId(trialPhase.getId());
        response.setTestCaseId(trialPhase.getTestCase().getId());
        response.setTestCaseName(trialPhase.getTestCase().getTestName());
        response.setPhaseName(trialPhase.getPhaseName());
        response.setDateOfPhase(trialPhase.getDateOfPhase());
        response.setObservations(trialPhase.getObservations());
        response.setTestDataEntry(trialPhase.getTestDataEntry());
        response.setWeatherTemperature(trialPhase.getWeatherTemperature());
        response.setWeatherHumidity(trialPhase.getWeatherHumidity());
        response.setWeatherRainfall(trialPhase.getWeatherRainfall());
        response.setAdditionalComments(trialPhase.getAdditionalComments());
        response.setStatus(trialPhase.getStatus());
        response.setCreatedAt(trialPhase.getCreatedAt());
        response.setUpdatedAt(trialPhase.getUpdatedAt());
        response.setCreatedBy(trialPhase.getCreatedBy());
        response.setUpdatedBy(trialPhase.getUpdatedBy());
        
        if (trialPhase.getAttachments() != null) {
            response.setAttachmentUrls(trialPhase.getAttachments().stream()
                    .map(FileAttachment::getFilePath)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }
} 