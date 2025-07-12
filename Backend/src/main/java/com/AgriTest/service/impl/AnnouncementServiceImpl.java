package com.AgriTest.service.impl;

import com.AgriTest.dto.AnnouncementRequest;
import com.AgriTest.dto.AnnouncementResponse;
import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Announcement;
import com.AgriTest.model.AnnouncementAttachment;
import com.AgriTest.repository.AnnouncementRepository;
import com.AgriTest.service.AnnouncementService;
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
public class AnnouncementServiceImpl implements AnnouncementService {
    private static final Logger log = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    @Transactional
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request) {
        // Create announcement entity
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setMessageBody(request.getMessageBody());
        announcement.setTargetAudience(
            request.getTargetAudience() != null 
            ? request.getTargetAudience() 
            : Announcement.TargetAudience.ALL_USERS
        );
        announcement.setPriorityLevel(
            request.getPriorityLevel() != null 
            ? request.getPriorityLevel() 
            : Announcement.PriorityLevel.NORMAL
        );
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setStatus(Announcement.AnnouncementStatus.DRAFT);

        // First save the announcement to get an ID
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Handle file attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<AnnouncementAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                // Store file and create attachment
                MediaFileResponse mediaFileResponse = fileStorageService.storeFile(
                    file, 
                    savedAnnouncement.getId(), 
                    SecurityUtils.getCurrentUserId(), 
                    "ANNOUNCEMENT"
                );
                
                AnnouncementAttachment attachment = new AnnouncementAttachment();
                attachment.setFileName(mediaFileResponse.getFileName());
                attachment.setFileUrl(mediaFileResponse.getFileDownloadUri());
                attachment.setFileType(mediaFileResponse.getFileType());
                attachment.setFileSize(mediaFileResponse.getSize());
                attachment.setUploadedBy(SecurityUtils.getCurrentUserId());
                attachment.setUploadedAt(LocalDateTime.now());
                attachment.setAnnouncement(savedAnnouncement);
                
                attachments.add(attachment);
            }
            savedAnnouncement.setAttachments(attachments);
        }

        // Save again with attachments
        savedAnnouncement = announcementRepository.save(savedAnnouncement);

        // Convert to response DTO
        return mapToAnnouncementResponse(savedAnnouncement);
    }

    @Override
    @Transactional
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request) {
        // Find existing announcement
        Announcement existingAnnouncement = announcementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));

        // Update basic details
        existingAnnouncement.setTitle(request.getTitle());
        existingAnnouncement.setMessageBody(request.getMessageBody());
        
        // Update target audience and priority
        if (request.getTargetAudience() != null) {
            existingAnnouncement.setTargetAudience(request.getTargetAudience());
        }
        if (request.getPriorityLevel() != null) {
            existingAnnouncement.setPriorityLevel(request.getPriorityLevel());
        }

        // Update attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            // Clear existing attachments
            existingAnnouncement.getAttachments().clear();

            // Add new attachments
            List<AnnouncementAttachment> newAttachments = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                MediaFileResponse mediaFileResponse = fileStorageService.storeFile(
                    file, 
                    existingAnnouncement.getId(), 
                    SecurityUtils.getCurrentUserId(), 
                    "ANNOUNCEMENT"
                );
                
                AnnouncementAttachment attachment = new AnnouncementAttachment();
                attachment.setFileName(mediaFileResponse.getFileName());
                attachment.setFileUrl(mediaFileResponse.getFileDownloadUri());
                attachment.setFileType(mediaFileResponse.getFileType());
                attachment.setFileSize(mediaFileResponse.getSize());
                attachment.setUploadedBy(SecurityUtils.getCurrentUserId());
                attachment.setUploadedAt(LocalDateTime.now());
                attachment.setAnnouncement(existingAnnouncement);
                
                newAttachments.add(attachment);
            }
            existingAnnouncement.setAttachments(newAttachments);
        }

        existingAnnouncement.setUpdatedAt(LocalDateTime.now());

        // Save updated announcement
        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);

        return mapToAnnouncementResponse(updatedAnnouncement);
    }

    @Override
    public AnnouncementResponse getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
        
        return mapToAnnouncementResponse(announcement);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
            .map(this::mapToAnnouncementResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        log.info("Deleting announcement with ID: {}", id);
        
        Announcement announcement = announcementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
        
        // First, handle the media files associated with this announcement
        // This will break the foreign key constraint before deletion
        log.info("Handling media files for announcement with ID: {}", id);
        fileStorageService.deleteFilesByEntityIdAndType(id, "ANNOUNCEMENT");
        
        // Then delete the announcement (this will cascade to announcement_attachments)
        log.info("Deleting announcement entity with ID: {}", id);
        announcementRepository.delete(announcement);
        
        log.info("Announcement with ID: {} deleted successfully", id);
    }

    @Override
    public List<AnnouncementResponse> getAnnouncementsByTargetAudience(Announcement.TargetAudience targetAudience) {
        return announcementRepository.findByTargetAudience(targetAudience).stream()
            .map(this::mapToAnnouncementResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementResponse> getAnnouncementsByPriorityLevel(Announcement.PriorityLevel priorityLevel) {
        return announcementRepository.findByPriorityLevel(priorityLevel).stream()
            .map(this::mapToAnnouncementResponse)
            .collect(Collectors.toList());
    }

    // Helper method to map Announcement to AnnouncementResponse
    private AnnouncementResponse mapToAnnouncementResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setMessageBody(announcement.getMessageBody());
        response.setTargetAudience(announcement.getTargetAudience());
        response.setPriorityLevel(announcement.getPriorityLevel());
        response.setStatus(announcement.getStatus());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        
        // Map attachment URLs
        if (announcement.getAttachments() != null) {
            response.setAttachmentUrls(
                announcement.getAttachments().stream()
                    .map(AnnouncementAttachment::getFileUrl)
                    .collect(Collectors.toList())
            );
        }

        return response;
    }
}