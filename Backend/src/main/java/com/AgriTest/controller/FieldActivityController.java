package com.AgriTest.controller;

import com.AgriTest.dto.FieldActivityRequest;
import com.AgriTest.dto.FieldActivityResponse;
import com.AgriTest.model.FieldActivity;
import com.AgriTest.service.FieldActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/field-activities")
public class FieldActivityController {

    @Autowired
    private FieldActivityService fieldActivityService;

    // Create Field Activity
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<FieldActivityResponse> createFieldActivity(
        @Valid @ModelAttribute FieldActivityRequest request
    ) {
        FieldActivityResponse response = fieldActivityService.createFieldActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update Field Activity
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<FieldActivityResponse> updateFieldActivity(
        @PathVariable Long id,
        @Valid @ModelAttribute FieldActivityRequest request
    ) {
        FieldActivityResponse response = fieldActivityService.updateFieldActivity(id, request);
        return ResponseEntity.ok(response);
    }

    // Get Field Activity by ID
    @GetMapping("/{id}")
    public ResponseEntity<FieldActivityResponse> getFieldActivityById(@PathVariable Long id) {
        FieldActivityResponse response = fieldActivityService.getFieldActivityById(id);
        return ResponseEntity.ok(response);
    }

    // List All Field Activities
    @GetMapping
    public ResponseEntity<List<FieldActivityResponse>> getAllFieldActivities() {
        List<FieldActivityResponse> activities = fieldActivityService.getAllFieldActivities();
        return ResponseEntity.ok(activities);
    }

    // Get Field Activities by Responsible Person
    @GetMapping("/by-person/{userId}")
    public ResponseEntity<List<FieldActivityResponse>> getFieldActivitiesByResponsiblePerson(
        @PathVariable Long userId
    ) {
        List<FieldActivityResponse> activities = fieldActivityService.getFieldActivitiesByResponsiblePerson(userId);
        return ResponseEntity.ok(activities);
    }

    // Get Field Activities by Status
    @GetMapping("/by-status")
    public ResponseEntity<List<FieldActivityResponse>> getFieldActivitiesByStatus(
        @RequestParam FieldActivity.FieldActivityStatus status
    ) {
        List<FieldActivityResponse> activities = fieldActivityService.getFieldActivitiesByStatus(status);
        return ResponseEntity.ok(activities);
    }

    // Get Field Activities by Date Range
    @GetMapping("/by-date-range")
    public ResponseEntity<List<FieldActivityResponse>> getFieldActivitiesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime
    ) {
        List<FieldActivityResponse> activities = fieldActivityService.getFieldActivitiesByDateRange(startDateTime, endDateTime);
        return ResponseEntity.ok(activities);
    }

    // Update Field Activity Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<FieldActivityResponse> updateFieldActivityStatus(
        @PathVariable Long id,
        @RequestParam FieldActivity.FieldActivityStatus status
    ) {
        FieldActivityResponse response = fieldActivityService.updateFieldActivityStatus(id, status);
        return ResponseEntity.ok(response);
    }

    // Delete Field Activity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldActivity(@PathVariable Long id) {
        fieldActivityService.deleteFieldActivity(id);
        return ResponseEntity.noContent().build();
    }
}