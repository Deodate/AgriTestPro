package com.AgriTest.controller;

import com.AgriTest.dto.AnnouncementRequest;
import com.AgriTest.dto.AnnouncementResponse;
import com.AgriTest.model.Announcement;
import com.AgriTest.service.AnnouncementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementController {
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    // Create Announcement
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createAnnouncement(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "messageBody") String messageBody,
            @RequestParam(value = "targetAudience", required = false) Announcement.TargetAudience targetAudience,
            @RequestParam(value = "priorityLevel", required = false) Announcement.PriorityLevel priorityLevel,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        try {
            logger.info("Creating new announcement with title: {}", title);

            // Validate required fields
            Map<String, String> validationErrors = new HashMap<>();
            if (title == null || title.trim().isEmpty()) {
                validationErrors.put("title", "Title is required");
            }
            if (messageBody == null || messageBody.trim().isEmpty()) {
                validationErrors.put("messageBody", "Message body is required");
            }
            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest().body(validationErrors);
            }

            // Create request object
            AnnouncementRequest request = new AnnouncementRequest();
            request.setTitle(title);
            request.setMessageBody(messageBody);
            request.setTargetAudience(targetAudience);
            request.setPriorityLevel(priorityLevel);
            request.setAttachments(attachments);

            // Create announcement
            AnnouncementResponse createdAnnouncement = announcementService.createAnnouncement(request);
            
            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("message", "Announcement created successfully");
            response.put("data", createdAnnouncement);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating announcement: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to create announcement: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Announcement
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "messageBody", required = false) String messageBody,
            @RequestParam(value = "targetAudience", required = false) Announcement.TargetAudience targetAudience,
            @RequestParam(value = "priorityLevel", required = false) Announcement.PriorityLevel priorityLevel,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        try {
            logger.info("Updating announcement with ID: {}", id);

            AnnouncementRequest request = new AnnouncementRequest();
            request.setTitle(title);
            request.setMessageBody(messageBody);
            request.setTargetAudience(targetAudience);
            request.setPriorityLevel(priorityLevel);
            request.setAttachments(attachments);

            AnnouncementResponse updatedAnnouncement = announcementService.updateAnnouncement(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("message", "Announcement updated successfully");
            response.put("data", updatedAnnouncement);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating announcement: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to update announcement: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Announcement by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<?> getAnnouncementById(@PathVariable Long id) {
        try {
            logger.info("Fetching announcement with ID: {}", id);
            AnnouncementResponse announcement = announcementService.getAnnouncementById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("data", announcement);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching announcement: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch announcement: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get All Announcements
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<?> getAllAnnouncements() {
        try {
            logger.info("Fetching all announcements");
            List<AnnouncementResponse> announcements = announcementService.getAllAnnouncements();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("data", announcements);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching announcements: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch announcements: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Announcement
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        try {
            logger.info("Deleting announcement with ID: {}", id);
            announcementService.deleteAnnouncement(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("message", "Announcement deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting announcement: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to delete announcement: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Announcements by Target Audience
    @GetMapping("/audience/{targetAudience}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> getAnnouncementsByTargetAudience(
            @PathVariable Announcement.TargetAudience targetAudience
    ) {
        try {
            logger.info("Fetching announcements for target audience: {}", targetAudience);
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByTargetAudience(targetAudience);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("data", announcements);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching announcements by target audience: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch announcements: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Announcements by Priority Level
    @GetMapping("/priority/{priorityLevel}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> getAnnouncementsByPriorityLevel(
            @PathVariable Announcement.PriorityLevel priorityLevel
    ) {
        try {
            logger.info("Fetching announcements for priority level: {}", priorityLevel);
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByPriorityLevel(priorityLevel);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("code", 200);
            response.put("data", announcements);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching announcements by priority level: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch announcements: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}