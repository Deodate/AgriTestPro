// File: src/main/java/com/AgriTest/service/impl/FileStorageServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.exception.FileNotFoundException;
import com.AgriTest.exception.FileStorageException;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.MediaFileMapper;
import com.AgriTest.model.MediaFile;
import com.AgriTest.model.TestResult;
import com.AgriTest.repository.MediaFileRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    private final MediaFileRepository mediaFileRepository;
    private final TestResultRepository testResultRepository;
    private final MediaFileMapper mediaFileMapper;

    @Autowired
    public FileStorageServiceImpl(
            @Value("${file.upload-dir}") String uploadDir,
            MediaFileRepository mediaFileRepository,
            TestResultRepository testResultRepository,
            MediaFileMapper mediaFileMapper) {
        
        this.mediaFileRepository = mediaFileRepository;
        this.testResultRepository = testResultRepository;
        this.mediaFileMapper = mediaFileMapper;
        
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public MediaFileResponse storeFile(MultipartFile file, Long testResultId, Long userId) {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }
        
        // Get the original file name and clean it
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Check if the filename contains invalid characters
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFilename);
        }
        
        // Generate a unique filename to prevent overwriting existing files
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        
        // File path relative to storage location
        String filePath = uniqueFilename;
        
        try {
            // Copy the file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Get the TestResult entity
            TestResult testResult = testResultRepository.findById(testResultId)
                    .orElseThrow(() -> new ResourceNotFoundException("TestResult not found with id: " + testResultId));
            
            // Create and save the MediaFile entity
            MediaFile mediaFile = mediaFileMapper.toEntity(file, filePath, testResult, userId);
            MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);
            
            // Convert and return the DTO
            return mediaFileMapper.toDto(savedMediaFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFilename + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found: " + fileName, ex);
        }
    }

    @Override
    public void deleteFile(Long id) {
        // Find the MediaFile entity
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaFile not found with id: " + id));
        
        try {
            // Delete the file from the filesystem
            Path filePath = this.fileStorageLocation.resolve(mediaFile.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
            
            // Delete the database record
            mediaFileRepository.delete(mediaFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + mediaFile.getFileName(), ex);
        }
    }
}