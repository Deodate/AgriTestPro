// File: src/main/java/com/AgriTest/service/impl/TestCaseTrialPhaseServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.TestCaseTrialPhaseRequest;
import com.AgriTest.dto.TestCaseTrialPhaseResponse;
import com.AgriTest.model.FileAttachment;
import com.AgriTest.model.TestCaseTrialPhase;
import com.AgriTest.repository.TestCaseTrialPhaseRepository;
import com.AgriTest.service.TestCaseTrialPhaseService;
import com.AgriTest.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// Import TestCase and TestCaseRepository
import com.AgriTest.model.TestCase;
import com.AgriTest.repository.TestCaseRepository;

@Service
public class TestCaseTrialPhaseServiceImpl implements TestCaseTrialPhaseService {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseTrialPhaseServiceImpl.class);

    @Autowired
    private TestCaseTrialPhaseRepository repository;

    // Inject FileStorageService
    @Autowired
    private com.AgriTest.service.FileStorageService fileStorageService;

    // Inject TestCaseRepository
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public TestCaseTrialPhaseResponse createTrialPhase(TestCaseTrialPhaseRequest request) {
        logger.info("Creating new Trial Phase.");
        TestCaseTrialPhase trialPhase = new TestCaseTrialPhase();
        
        // Map basic fields first, excluding attachments for now
        mapRequestToEntity(request, trialPhase);

        // Fetch TestCase and set testName before saving
        if (request.getTestCaseId() != null) {
            TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Test Case not found with id: " + request.getTestCaseId()));
            trialPhase.setTestName(testCase.getTestName());
             logger.info("Fetched Test Case {} and set testName: {}", request.getTestCaseId(), testCase.getTestName());
        } else {
            logger.warn("TestCaseId is null for new Trial Phase.");
             trialPhase.setTestName(null); // Ensure testName is null if testCaseId is null
        }

        // Save the trial phase first to get the generated ID
        // This save now includes the fetched testName
        TestCaseTrialPhase savedPhase = repository.save(trialPhase);
        logger.info("Trial Phase saved initially with ID: {}", savedPhase.getId());

        // Handle File Attachments AFTER saving the trial phase
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            logger.info("Processing {} attachments for Trial Phase ID: {}", request.getAttachments().size(), savedPhase.getId());
            List<String> filePaths = request.getAttachments().stream()
                .map(file -> {
                    try {
                        // Call storeFile with file, trialPhaseId, and testCaseId
                        // Note: We use savedPhase.getTestCaseId() here for consistency, which should match request.getTestCaseId()
                        com.AgriTest.dto.MediaFileResponse mediaFileResponse = fileStorageService.storeFile(file, savedPhase.getId(), savedPhase.getTestCaseId(), "TRIAL_PHASE");
                        String filePath = mediaFileResponse.getFileName(); // Get the saved file path/name
                        logger.info("File stored: {} -> {}", file.getOriginalFilename(), filePath);
                        return filePath; // Return the String file path
                    } catch (Exception e) { // Catch generic Exception for storeFile issues
                         logger.error("Error storing file with FileStorageService: {}", file.getOriginalFilename(), e);
                         throw new RuntimeException("Error storing file with FileStorageService", e);
                    }
                })
                .collect(Collectors.toList());

            logger.info("Collected file paths: {}", filePaths);

            // Join file paths into a single string and update the saved trial phase entity
            savedPhase.setImageVideo(String.join(",", filePaths));
            logger.info("Setting imageVideo field to: {}", savedPhase.getImageVideo());

            // Save the trial phase again to persist the imageVideo paths
            logger.info("Saving trial phase again to persist imageVideo paths.");
            repository.save(savedPhase);
            logger.info("Trial Phase saved again with ID: {}", savedPhase.getId());
        } else {
            logger.info("No attachments provided for Trial Phase ID: {}", savedPhase.getId());
            // If no attachments are provided, set imageVideo to null or empty string
            savedPhase.setImageVideo(null); // Or ""
        }

        return mapToResponse(savedPhase);
    }

    @Override
    @Transactional
    public TestCaseTrialPhaseResponse updateTrialPhase(Long id, TestCaseTrialPhaseRequest request) {
        logger.info("Updating Trial Phase with ID: {}", id);
        // Find existing trial phase
        TestCaseTrialPhase existingPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));

        // Get the original testCaseId before mapping request to entity
        Long originalTestCaseId = existingPhase.getTestCaseId();

        // Map updated fields, excluding attachments for now
        mapRequestToEntity(request, existingPhase);

        // If testCaseId has changed or is now provided, fetch and update testName
        if (request.getTestCaseId() != null && !request.getTestCaseId().equals(originalTestCaseId)) {
             TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Test Case not found with id: " + request.getTestCaseId()));
            existingPhase.setTestName(testCase.getTestName());
            logger.info("Fetched new Test Case {} and updated testName to: {}", request.getTestCaseId(), testCase.getTestName());
        } else if (existingPhase.getTestCaseId() != null && existingPhase.getTestName() == null) {
             // If testCaseId is present but testName is null (e.g., migrated data or previous save failed to set it)
             TestCase testCase = testCaseRepository.findById(existingPhase.getTestCaseId())
                 .orElse(null); // Use orElse(null) to avoid throwing if TestCase was deleted
             if (testCase != null) {
                  existingPhase.setTestName(testCase.getTestName());
                  logger.info("Fetched Test Case {} and set missing testName: {}", existingPhase.getTestCaseId(), testCase.getTestName());
             } else {
                  logger.warn("TestCase {} not found for existing Trial Phase. Setting testName to null.", existingPhase.getTestCaseId());
                  existingPhase.setTestName(null);
             }
        } else if (existingPhase.getTestCaseId() == null) {
             // If testCaseId is set to null in the request
             existingPhase.setTestName(null);
             logger.warn("TestCaseId is set to null in the update request. Setting testName to null.");
        }


        // Handle File Attachments for update
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            logger.info("Processing {} attachments for Trial Phase ID: {} during update.", request.getAttachments().size(), existingPhase.getId());
            // Clear existing imageVideo paths if you want to replace them on update
            existingPhase.setImageVideo(null); // Or clear existing FileAttachment entities if that approach is also used

            List<String> filePaths = request.getAttachments().stream()
                .map(file -> {
                    try {
                        // Call storeFile with file, existingPhase.getId(), and existingPhase.getTestCaseId()
                         com.AgriTest.dto.MediaFileResponse mediaFileResponse = fileStorageService.storeFile(file, existingPhase.getId(), existingPhase.getTestCaseId(), "TRIAL_PHASE");
                         String filePath = mediaFileResponse.getFileName(); // Get the saved file path/name
                         logger.info("File stored during update: {} -> {}", file.getOriginalFilename(), filePath);
                         return filePath; // Return the String file path
                    } catch (Exception e) { // Catch generic Exception for storeFile issues
                         logger.error("Error storing file with FileStorageService during update: {}", file.getOriginalFilename(), e);
                         throw new RuntimeException("Error storing file with FileStorageService", e);
                    }
                })
                .collect(Collectors.toList());

             logger.info("Collected file paths during update: {}", filePaths);

            // Join file paths into a single string and update the existing trial phase entity
            existingPhase.setImageVideo(String.join(",", filePaths));
            logger.info("Setting imageVideo field to: {} during update.", existingPhase.getImageVideo());

        } else {
             logger.info("No new attachments provided for Trial Phase ID: {} during update.", existingPhase.getId());
             // If no attachments provided on update, decide whether to clear existing paths
             // For now, we will keep existing paths if no new files are uploaded.
             // If you want to clear them, uncomment the line below:
             // existingPhase.setImageVideo(null);
        }

        // Save and return
        logger.info("Saving trial phase again to persist imageVideo paths during update.");
        TestCaseTrialPhase updatedPhase = repository.save(existingPhase);
        logger.info("Trial Phase updated and saved with ID: {}", updatedPhase.getId());

        return mapToResponse(updatedPhase);
    }

    @Override
    @Transactional(readOnly = true)
    public TestCaseTrialPhaseResponse getTrialPhaseById(Long id) {
        TestCaseTrialPhase trialPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));

        // Ensure testName is set on the entity before mapping to response if it was null
         if (trialPhase.getTestCaseId() != null && trialPhase.getTestName() == null) {
              TestCase testCase = testCaseRepository.findById(trialPhase.getTestCaseId())
                  .orElse(null); // Use orElse(null) to avoid throwing if TestCase was deleted
              if (testCase != null) {
                   trialPhase.setTestName(testCase.getTestName());
                   logger.info("Fetched TestCase {} and set missing testName for trial phase {}: {}", trialPhase.getTestCaseId(), trialPhase.getId(), testCase.getTestName());
              } else {
                   logger.warn("TestCase {} not found for trial phase {}. Cannot set testName.", trialPhase.getTestCaseId(), trialPhase.getId());
                   trialPhase.setTestName(null);
              }
         }

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
    @Transactional(readOnly = true)
    public List<TestCaseTrialPhaseResponse> getAllTrialPhases() {
        logger.info("Fetching all Trial Phases.");
        List<TestCaseTrialPhase> trialPhases = repository.findAll();

         // Fetch and set testName for each trial phase if it's missing
         trialPhases.forEach(trialPhase -> {
              if (trialPhase.getTestCaseId() != null && trialPhase.getTestName() == null) {
                   TestCase testCase = testCaseRepository.findById(trialPhase.getTestCaseId())
                       .orElse(null); // Use orElse(null) to avoid throwing if TestCase was deleted
                   if (testCase != null) {
                        trialPhase.setTestName(testCase.getTestName());
                        logger.debug("Fetched TestCase {} and set missing testName for trial phase {}: {}", trialPhase.getTestCaseId(), trialPhase.getId(), testCase.getTestName());
                   } else {
                        logger.warn("TestCase {} not found for trial phase {}. Cannot set testName.", trialPhase.getTestCaseId(), trialPhase.getId());
                        trialPhase.setTestName(null);
                   }
              }
         });

        return trialPhases.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTrialPhase(Long id) {
        TestCaseTrialPhase trialPhase = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Trial Phase not found with id: " + id));
        
        // Delete associated trial phase observations first
        try {
            logger.info("Attempting to delete associated trial phase observations for trial phase ID: {}", id);
            repository.deleteTrialPhaseObservationsByTrialPhaseId(id);
            // Explicitly flush to synchronize native query deletion with the persistence context
            repository.flush(); 
            logger.info("Successfully deleted associated trial phase observations for trial phase ID: {}", id);
        } catch (Exception e) {
            // Log the error but continue with trial phase deletion
            logger.error("Failed to delete associated trial phase observations for trial phase {}: {}", id, e.getMessage());
        }

        // Delete associated media files using the dedicated service method
        try {
            logger.info("Attempting to delete associated media files for trial phase ID: {}", id);
            // Assuming the entity type for Trial Phases is "TRIAL_PHASE"
            fileStorageService.deleteFilesByEntityIdAndType(id, "TRIAL_PHASE");
            logger.info("Successfully requested deletion of associated media files for trial phase ID: {}", id);
        } catch (Exception e) {
            // Log the error but continue with trial phase deletion
            logger.error("Failed to delete associated media files for trial phase {} using deleteFilesByEntityIdAndType: {}", id, e.getMessage());
        }
        
        // Now delete the trial phase entity
        repository.delete(trialPhase);
        logger.info("Successfully deleted trial phase with ID: {}", id);
    }

    // Utility method to map request to entity
    private void mapRequestToEntity(TestCaseTrialPhaseRequest request, TestCaseTrialPhase trialPhase) {
        trialPhase.setTestCaseId(request.getTestCaseId());
        trialPhase.setPhaseName(request.getPhaseName());
        trialPhase.setPhaseDate(request.getPhaseDate());
        // Map observations and testDataEntries as single strings
        trialPhase.setObservations(request.getObservations());
        trialPhase.setTestDataEntry(request.getTestData());
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

        // NOTE: File attachments are now handled directly in createTrialPhase and updateTrialPhase
        //       This method only maps non-file fields.
    }

    // File attachment creation method (not used in this approach)
    private FileAttachment createFileAttachment(MultipartFile file) {
         throw new UnsupportedOperationException("createFileAttachment method is not used in this approach.");
    }

    // Mapping method from entity to response
    private TestCaseTrialPhaseResponse mapToResponse(TestCaseTrialPhase trialPhase) {
        TestCaseTrialPhaseResponse response = new TestCaseTrialPhaseResponse();
        response.setId(trialPhase.getId());
        response.setTestCaseId(trialPhase.getTestCaseId());
        response.setPhaseName(trialPhase.getPhaseName());
        response.setPhaseDate(trialPhase.getPhaseDate());
        response.setObservations(trialPhase.getObservations());
        response.setTestDataEntry(trialPhase.getTestDataEntry());
        response.setAdditionalComments(trialPhase.getAdditionalComments());
        response.setCreatedAt(trialPhase.getCreatedAt());
        response.setUpdatedAt(trialPhase.getUpdatedAt());

        // Set testName from the entity (it should now be populated before saving)
        response.setTestName(trialPhase.getTestName());

        // Map Weather Data
        if (trialPhase.getWeatherData() != null) {
            TestCaseTrialPhaseResponse.WeatherDataDto weatherDto = 
                new TestCaseTrialPhaseResponse.WeatherDataDto();
            weatherDto.setTemperature(trialPhase.getWeatherData().getTemperature());
            weatherDto.setHumidity(trialPhase.getWeatherData().getHumidity());
            weatherDto.setRainfall(trialPhase.getWeatherData().getRainfall());
            response.setWeatherData(weatherDto);
        }

         // Map File Attachments from the entity (assuming they are stored as FileAttachment entities)
         // If attachments are stored as a comma-separated string of filenames in imageVideo field:
        if (trialPhase.getImageVideo() != null && !trialPhase.getImageVideo().isEmpty()) {
             // Note: This assumes imageVideo is a comma-separated string of *filenames*
             // If you have a proper relationship to FileAttachment entities, you would fetch and map those here.
             // For now, we are just passing the imageVideo string to the frontend if needed.
             // Frontend would need to request the actual file content using the filename.
             // response.setImageVideo(trialPhase.getImageVideo()); // You might want to add imageVideo to the response DTO
        }
        // If you have a relationship to FileAttachment entities:
        if (trialPhase.getAttachments() != null) {
             List<TestCaseTrialPhaseResponse.FileAttachmentDto> attachmentDtos = trialPhase.getAttachments().stream()
                 .map(attachment -> {
                      TestCaseTrialPhaseResponse.FileAttachmentDto dto = new TestCaseTrialPhaseResponse.FileAttachmentDto();
                      dto.setId(attachment.getId());
                      dto.setFileName(attachment.getFileName());
                      dto.setFileType(attachment.getFileType());
                      dto.setFilePath(attachment.getFileName()); // Or URL to access it
                      // Add other relevant fields from FileAttachment entity
                      return dto;
                 })
                 .collect(Collectors.toList());
             response.setAttachments(attachmentDtos); // Assuming attachments field exists in Response DTO
        }


        return response;
    }
}