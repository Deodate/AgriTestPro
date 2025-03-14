// File: src/main/java/com/AgriTest/service/impl/TestCaseTrialPhaseServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.TestCaseTrialPhaseRequest;
import com.AgriTest.dto.TestCaseTrialPhaseResponse;
import com.AgriTest.model.FileAttachment;
import com.AgriTest.model.TestCaseTrialPhase;
import com.AgriTest.repository.TestCaseTrialPhaseRepository;
import com.AgriTest.service.TestCaseTrialPhaseService;
import com.AgriTest.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestCaseTrialPhaseServiceImpl implements TestCaseTrialPhaseService {

    @Autowired
    private TestCaseTrialPhaseRepository repository;

    @Override
    @Transactional
    public TestCaseTrialPhaseResponse createTrialPhase(TestCaseTrialPhaseRequest request) {
        TestCaseTrialPhase trialPhase = new TestCaseTrialPhase();
        mapRequestToEntity(request, trialPhase);

        // Save and return
        TestCaseTrialPhase savedPhase = repository.save(trialPhase);
        return mapToResponse(savedPhase);
    }

    @Override
    @Transactional
    public TestCaseTrialPhaseResponse updateTrialPhase(Long id, TestCaseTrialPhaseRequest request) {
        // Find existing trial phase
        TestCaseTrialPhase existingPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));

        // Update existing phase
        mapRequestToEntity(request, existingPhase);

        // Save and return
        TestCaseTrialPhase updatedPhase = repository.save(existingPhase);
        return mapToResponse(updatedPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public TestCaseTrialPhaseResponse getTrialPhaseById(Long id) {
        TestCaseTrialPhase trialPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));
        
        return mapToResponse(trialPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseTrialPhaseResponse> getTrialPhasesByTestCase(Long testCaseId) {
        List<TestCaseTrialPhase> trialPhases = repository.findByTestCaseId(testCaseId);
        
        return trialPhases.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTrialPhase(Long id) {
        TestCaseTrialPhase trialPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));
        
        repository.delete(trialPhase);
    }

    // Utility method to map request to entity
    private void mapRequestToEntity(TestCaseTrialPhaseRequest request, TestCaseTrialPhase trialPhase) {
        trialPhase.setTestCaseId(request.getTestCaseId());
        trialPhase.setPhaseName(request.getPhaseName());
        trialPhase.setPhaseDate(request.getPhaseDate());
        trialPhase.setObservations(request.getObservations());
        trialPhase.setTestDataEntries(request.getTestDataEntries());
        trialPhase.setAdditionalComments(request.getAdditionalComments());

        // Set Weather Data
        if (request.getWeatherData() != null) {
            TestCaseTrialPhase.WeatherData weatherData = new TestCaseTrialPhase.WeatherData(
                request.getWeatherData().getTemperature(),
                request.getWeatherData().getHumidity(),
                request.getWeatherData().getRainfall()
            );
            trialPhase.setWeatherData(weatherData);
        }

        // Handle File Attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            // Clear existing attachments if updating
            if (trialPhase.getAttachments() != null) {
                trialPhase.getAttachments().clear();
            }

            List<FileAttachment> attachments = request.getAttachments().stream()
                .map(this::createFileAttachment)
                .collect(Collectors.toList());
            
            trialPhase.setAttachments(attachments);
        }
    }

    // File attachment creation method
    private FileAttachment createFileAttachment(MultipartFile file) {
        try {
            FileAttachment attachment = new FileAttachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFileData(file.getBytes());
            return attachment;
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file attachment", e);
        }
    }

    // Mapping method from entity to response
    private TestCaseTrialPhaseResponse mapToResponse(TestCaseTrialPhase trialPhase) {
        TestCaseTrialPhaseResponse response = new TestCaseTrialPhaseResponse();
        response.setId(trialPhase.getId());
        response.setTestCaseId(trialPhase.getTestCaseId());
        response.setPhaseName(trialPhase.getPhaseName());
        response.setPhaseDate(trialPhase.getPhaseDate());
        response.setObservations(trialPhase.getObservations());
        response.setTestDataEntries(trialPhase.getTestDataEntries());
        response.setAdditionalComments(trialPhase.getAdditionalComments());
        response.setCreatedAt(trialPhase.getCreatedAt());
        response.setUpdatedAt(trialPhase.getUpdatedAt());

        // Map Weather Data
        if (trialPhase.getWeatherData() != null) {
            TestCaseTrialPhaseResponse.WeatherDataDto weatherDto = 
                new TestCaseTrialPhaseResponse.WeatherDataDto();
            weatherDto.setTemperature(trialPhase.getWeatherData().getTemperature());
            weatherDto.setHumidity(trialPhase.getWeatherData().getHumidity());
            weatherDto.setRainfall(trialPhase.getWeatherData().getRainfall());
            response.setWeatherData(weatherDto);
        }

        // Map File Attachments
        if (trialPhase.getAttachments() != null) {
            List<TestCaseTrialPhaseResponse.FileAttachmentDto> attachmentDtos = 
                trialPhase.getAttachments().stream()
                .map(attachment -> {
                    TestCaseTrialPhaseResponse.FileAttachmentDto dto = 
                        new TestCaseTrialPhaseResponse.FileAttachmentDto();
                    dto.setId(attachment.getId());
                    dto.setFileName(attachment.getFileName());
                    dto.setFileType(attachment.getFileType());
                    return dto;
                })
                .collect(Collectors.toList());
            response.setAttachments(attachmentDtos);
        }

        return response;
    }
}