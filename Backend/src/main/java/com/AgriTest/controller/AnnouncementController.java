package com.AgriTest.controller;

import com.AgriTest.dto.AnnouncementRequest;
import com.AgriTest.dto.AnnouncementResponse;
import com.AgriTest.model.Announcement;
import com.AgriTest.service.AnnouncementService;
import com.AgriTest.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // Create Announcement
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "messageBody", required = false) String messageBody,
            @RequestParam(value = "targetAudience", required = false) Announcement.TargetAudience targetAudience,
            @RequestParam(value = "priorityLevel", required = false) Announcement.PriorityLevel priorityLevel,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle(title);
        request.setMessageBody(messageBody);
        request.setTargetAudience(targetAudience);
        request.setPriorityLevel(priorityLevel);
        request.setAttachments(attachments);

        AnnouncementResponse createdAnnouncement = announcementService.createAnnouncement(request);
        return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
    }

    // Update Announcement
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "messageBody", required = false) String messageBody,
            @RequestParam(value = "targetAudience", required = false) Announcement.TargetAudience targetAudience,
            @RequestParam(value = "priorityLevel", required = false) Announcement.PriorityLevel priorityLevel,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        AnnouncementRequest request = new AnnouncementRequest();
        request.setTitle(title);
        request.setMessageBody(messageBody);
        request.setTargetAudience(targetAudience);
        request.setPriorityLevel(priorityLevel);
        request.setAttachments(attachments);

        AnnouncementResponse updatedAnnouncement = announcementService.updateAnnouncement(id, request);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    // Get Announcement by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<AnnouncementResponse> getAnnouncementById(@PathVariable Long id) {
        AnnouncementResponse announcement = announcementService.getAnnouncementById(id);
        return ResponseEntity.ok(announcement);
    }

    // Get All Announcements
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {
        List<AnnouncementResponse> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    // Delete Announcement
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    // Get Announcements by Target Audience
    @GetMapping("/audience/{targetAudience}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByTargetAudience(
            @PathVariable Announcement.TargetAudience targetAudience
    ) {
        List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByTargetAudience(targetAudience);
        return ResponseEntity.ok(announcements);
    }

    // Get Announcements by Priority Level
    @GetMapping("/priority/{priorityLevel}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByPriorityLevel(
            @PathVariable Announcement.PriorityLevel priorityLevel
    ) {
        List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByPriorityLevel(priorityLevel);
        return ResponseEntity.ok(announcements);
    }
}