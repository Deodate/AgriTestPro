package com.AgriTest.service.impl;

import com.AgriTest.dto.FieldActivityRequest;
import com.AgriTest.dto.FieldActivityResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.exception.FileStorageException;
import com.AgriTest.mapper.FieldActivityMapper;
import com.AgriTest.model.FieldActivity;
import com.AgriTest.model.FieldActivityAttachment;
import com.AgriTest.model.User;
import com.AgriTest.repository.FieldActivityRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.FieldActivityService;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldActivityServiceImpl implements FieldActivityService {
    private static final Logger log = LoggerFactory.getLogger(FieldActivityServiceImpl.class);

    @Autowired
    private FieldActivityRepository fieldActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FieldActivityMapper fieldActivityMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    @Transactional
    public FieldActivityResponse createFieldActivity(FieldActivityRequest request) {
        // Validate input
        validateFieldActivityRequest(request);

        // Find responsible person
        User responsiblePerson = userRepository.findById(request.getResponsiblePersonId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getResponsiblePersonId()));

        // Create field activity entity
        FieldActivity fieldActivity = new FieldActivity();
        fieldActivity.setActivityName(request.getActivityName());
        fieldActivity.setLocation(request.getLocation());
        fieldActivity.setResponsiblePerson(responsiblePerson);
        fieldActivity.setActivityDateTime(request.getActivityDateTime() != null 
            ? request.getActivityDateTime() 
            : LocalDateTime.now());
        fieldActivity.setObservations(request.getObservations());
        fieldActivity.setStatus(request.getStatus() != null 
            ? request.getStatus() 
            : FieldActivity.FieldActivityStatus.PLANNED);

        // Save field activity first to generate ID
        FieldActivity savedActivity = fieldActivityRepository.save(fieldActivity);

        // Handle attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<FieldActivityAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                attachments.add(uploadAttachment(savedActivity, file));
            }
            savedActivity.setAttachments(attachments);
            
            // Save again to update attachments
            savedActivity = fieldActivityRepository.save(savedActivity);
        }
        
        // Return response
        return fieldActivityMapper.toResponse(savedActivity);
    }

    @Override
    @Transactional
    public FieldActivityResponse updateFieldActivity(Long id, FieldActivityRequest request) {
        // Find existing field activity
        FieldActivity existingActivity = fieldActivityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found with id: " + id));

        // Validate input
        validateFieldActivityRequest(request);

        // Find responsible person
        User responsiblePerson = userRepository.findById(request.getResponsiblePersonId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getResponsiblePersonId()));

        // Update field activity details
        existingActivity.setActivityName(request.getActivityName());
        existingActivity.setLocation(request.getLocation());
        existingActivity.setResponsiblePerson(responsiblePerson);
        existingActivity.setActivityDateTime(request.getActivityDateTime());
        existingActivity.setObservations(request.getObservations());
        
        // Update status if provided
        if (request.getStatus() != null) {
            existingActivity.setStatus(request.getStatus());
        }

        // Handle attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            // Clear existing attachments
            if (existingActivity.getAttachments() != null) {
                existingActivity.getAttachments().clear();
            }

            // Add new attachments
            List<FieldActivityAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                attachments.add(uploadAttachment(existingActivity, file));
            }
            existingActivity.setAttachments(attachments);
        }

        // Save updated field activity
        FieldActivity updatedActivity = fieldActivityRepository.save(existingActivity);
        
        // Return response
        return fieldActivityMapper.toResponse(updatedActivity);
    }

    @Override
    public FieldActivityResponse getFieldActivityById(Long id) {
        FieldActivity fieldActivity = fieldActivityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found with id: " + id));
        
        return fieldActivityMapper.toResponse(fieldActivity);
    }

    @Override
    public List<FieldActivityResponse> getAllFieldActivities() {
        return fieldActivityRepository.findAll().stream()
            .map(fieldActivityMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FieldActivityResponse> getFieldActivitiesByResponsiblePerson(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return fieldActivityRepository.findByResponsiblePerson(user).stream()
            .map(fieldActivityMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FieldActivityResponse> getFieldActivitiesByStatus(FieldActivity.FieldActivityStatus status) {
        return fieldActivityRepository.findByStatus(status).stream()
            .map(fieldActivityMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<FieldActivityResponse> getFieldActivitiesByDateRange(
        LocalDateTime startDateTime, 
        LocalDateTime endDateTime
    ) {
        return fieldActivityRepository.findByActivityDateTimeBetween(startDateTime, endDateTime).stream()
            .map(fieldActivityMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFieldActivity(Long id) {
        FieldActivity fieldActivity = fieldActivityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found with id: " + id));
        
        // Delete associated attachments
        if (fieldActivity.getAttachments() != null && !fieldActivity.getAttachments().isEmpty()) {
            for (FieldActivityAttachment attachment : fieldActivity.getAttachments()) {
                try {
                    // Assuming you have a method to delete files by their ID
                    fileStorageService.deleteFile(attachment.getId());
                } catch (Exception e) {
                    log.error("Could not delete attachment: " + attachment.getId(), e);
                }
            }
        }
        
        // Delete field activity
        fieldActivityRepository.delete(fieldActivity);
    }

    @Override
    @Transactional
    public FieldActivityResponse updateFieldActivityStatus(Long id, FieldActivity.FieldActivityStatus status) {
        FieldActivity fieldActivity = fieldActivityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Field Activity not found with id: " + id));
        
        fieldActivity.setStatus(status);
        FieldActivity updatedActivity = fieldActivityRepository.save(fieldActivity);
        
        return fieldActivityMapper.toResponse(updatedActivity);
    }

    // Helper method to validate field activity request
    private void validateFieldActivityRequest(FieldActivityRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Field Activity request cannot be null");
        }

        if (request.getActivityName() == null || request.getActivityName().trim().isEmpty()) {
            throw new IllegalArgumentException("Activity name is required");
        }

        if (request.getResponsiblePersonId() == null) {
            throw new IllegalArgumentException("Responsible person ID is required");
        }
    }

    // Helper method to upload attachment
    private FieldActivityAttachment uploadAttachment(FieldActivity fieldActivity, MultipartFile file) {
        try {
            // Store the file using FileStorageService
            com.AgriTest.dto.MediaFileResponse mediaFileResponse = fileStorageService.storeFile(
                file, 
                fieldActivity.getId(), 
                SecurityUtils.getCurrentUserId(), 
                "FIELD_ACTIVITY_ATTACHMENT"
            );

            // Create FieldActivityAttachment
            FieldActivityAttachment attachment = new FieldActivityAttachment();
            attachment.setFieldActivity(fieldActivity);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(mediaFileResponse.getFileDownloadUri());
            attachment.setFileType(file.getContentType());

            return attachment;
        } catch (Exception e) {
            log.error("Failed to upload attachment", e);
            throw new FileStorageException("Could not upload attachment", e);
        }
    }
}