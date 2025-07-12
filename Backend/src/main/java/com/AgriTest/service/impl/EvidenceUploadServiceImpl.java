package com.AgriTest.service.impl;

import com.AgriTest.model.EvidenceUpload;
import com.AgriTest.model.TestCase;
import com.AgriTest.repository.EvidenceUploadRepository;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.service.EvidenceUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvidenceUploadServiceImpl implements EvidenceUploadService {

    private final EvidenceUploadRepository evidenceUploadRepository;
    private final TestCaseRepository testCaseRepository; // Assuming a TestCaseRepository exists

    // Define a directory for storing uploaded files (adjust as needed)
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Autowired
    public EvidenceUploadServiceImpl(EvidenceUploadRepository evidenceUploadRepository, TestCaseRepository testCaseRepository) {
        this.evidenceUploadRepository = evidenceUploadRepository;
        this.testCaseRepository = testCaseRepository;
         // Create the upload directory if it doesn't exist
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void uploadEvidence(
        MultipartFile file,
        Long testCaseId,
        String mediaType,
        String description,
        String takenBy,
        Date dateCaptured
    ) throws Exception {
        // 1. Store the file with a unique name
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName;
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        int counter = 0;
        String baseName = fileName;
        String extension = "";

        // Extract base name and extension if available
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            baseName = originalFileName.substring(0, dotIndex);
            extension = originalFileName.substring(dotIndex);
        } else if (dotIndex == 0) { // Handle dot files like .env
             baseName = originalFileName;
             extension = "";
        } else {
             baseName = originalFileName;
             extension = "";
        }

        // Generate unique filename if file already exists
        while (Files.exists(targetLocation)) {
            counter++;
            fileName = baseName + "_" + System.currentTimeMillis() + "_" + counter + extension;
            targetLocation = this.fileStorageLocation.resolve(fileName);
        }

        Files.copy(file.getInputStream(), targetLocation);

        // 2. Fetch the test name using testCaseId
        String testName = null;
        Optional<TestCase> testCaseOptional = testCaseRepository.findById(testCaseId);
        if (testCaseOptional.isPresent()) {
            testName = testCaseOptional.get().getTestName(); // Assuming TestCase entity has getTestName() method
        }

        // 3. Create and save the EvidenceUpload entity
        EvidenceUpload evidence = new EvidenceUpload();
        evidence.setTestName(testName); // Set fetched test name
        evidence.setMediaType(mediaType);
        evidence.setFileName(fileName); // Store the stored file name
        evidence.setDescription(description);
        evidence.setTakenBy(takenBy);
        evidence.setDateCaptured(dateCaptured);
        // created_at is set automatically via @PrePersist

        evidenceUploadRepository.save(evidence);

        // Optionally, you might want to return the saved EvidenceUpload entity or its ID
    }

    @Override
    public List<com.AgriTest.model.EvidenceUpload> getAllEvidence() {
        // Fetch all evidence records from the repository
        return evidenceUploadRepository.findAll();
    }

    @Override
    public void deleteEvidence(Long id) {
        // Find the evidence by ID and delete it
        // You might want to add checks here, e.g., if the evidence exists
        evidenceUploadRepository.deleteById(id);
    }

    @Override
    public void updateEvidence(
            Long id,
            MultipartFile file,
            Long testCaseId,
            String mediaType,
            String description,
            String takenBy,
            Date dateCaptured
    ) throws Exception {
        // Find the existing evidence by ID
        Optional<EvidenceUpload> existingEvidenceOptional = evidenceUploadRepository.findById(id);

        if (existingEvidenceOptional.isPresent()) {
            EvidenceUpload existingEvidence = existingEvidenceOptional.get();

            // Update the fields of the existing evidence with the new data
            existingEvidence.setTestName(testCaseId != null ? getTestNameById(testCaseId) : existingEvidence.getTestName()); // Fetch test name if testCaseId is provided
            existingEvidence.setMediaType(mediaType != null ? mediaType : existingEvidence.getMediaType());
            existingEvidence.setDescription(description != null ? description : existingEvidence.getDescription());
            existingEvidence.setTakenBy(takenBy != null ? takenBy : existingEvidence.getTakenBy());
            existingEvidence.setDateCaptured(dateCaptured != null ? dateCaptured : existingEvidence.getDateCaptured());

            // Handle file update if a new file is provided
            if (file != null && !file.isEmpty()) {
                // Delete the old file if it exists (optional, depending on requirements)
                if (existingEvidence.getFileName() != null) {
                    Path oldFilePath = this.fileStorageLocation.resolve(existingEvidence.getFileName());
                    try {
                        Files.deleteIfExists(oldFilePath);
                    } catch (Exception e) {
                        // Log error but continue with the update
                        e.printStackTrace();
                    }
                }

                // Store the new file with a unique name
                String originalFileName = file.getOriginalFilename();
                String fileName = originalFileName;
                Path targetLocation = this.fileStorageLocation.resolve(fileName);

                int counter = 0;
                String baseName = fileName;
                String extension = "";

                int dotIndex = originalFileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
                    baseName = originalFileName.substring(0, dotIndex);
                    extension = originalFileName.substring(dotIndex);
                } else if (dotIndex == 0) { // Handle dot files like .env
                     baseName = originalFileName;
                     extension = "";
                } else {
                     baseName = originalFileName;
                     extension = "";
                }

                while (Files.exists(targetLocation)) {
                    counter++;
                    fileName = baseName + "_" + System.currentTimeMillis() + "_" + counter + extension;
                    targetLocation = this.fileStorageLocation.resolve(fileName);
                }

                Files.copy(file.getInputStream(), targetLocation);
                existingEvidence.setFileName(fileName); // Update file name in the entity
            }

            // Save the updated evidence entity
            evidenceUploadRepository.save(existingEvidence);
        } else {
            // Handle the case where the evidence with the given ID is not found
            throw new RuntimeException("Evidence with ID " + id + " not found.");
        }
    }

    @Override
    public EvidenceUpload getEvidenceById(Long id) {
        // Find the evidence by ID
        return evidenceUploadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evidence with ID " + id + " not found."));
    }

    // Helper method to fetch test name by ID (assuming TestCaseRepository exists and has findById)
    private String getTestNameById(Long testCaseId) {
        return testCaseRepository.findById(testCaseId)
                .map(TestCase::getTestName)
                .orElse("Unknown Test"); // Provide a default or throw an exception if test case not found
    }

    // Add other service methods related to evidence if needed
} 