// File: src/main/java/com/AgriTest/controller/TestScheduleController.java
package com.AgriTest.controller;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.service.TestScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-schedules")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Test Schedule Management", description = "APIs for managing test schedules")
@RequiredArgsConstructor
public class TestScheduleController {

    private final TestScheduleService testScheduleService;

    @PostMapping
    @Operation(summary = "Create a new test schedule",
            description = "Creates a new test schedule with the provided details")
    @ApiResponse(responseCode = "200", description = "Test schedule created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<TestScheduleResponse> createTestSchedule(
            @Valid @RequestBody TestScheduleRequest request) {
        return ResponseEntity.ok(testScheduleService.createTestSchedule(request));
    }

    @GetMapping
    @Operation(summary = "Get all test schedules",
            description = "Retrieves a list of all test schedules")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved test schedules")
    public ResponseEntity<List<TestScheduleResponse>> getAllTestSchedules() {
        return ResponseEntity.ok(testScheduleService.getAllTestSchedules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test schedule by ID",
            description = "Retrieves a specific test schedule by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved test schedule")
    @ApiResponse(responseCode = "404", description = "Test schedule not found")
    public ResponseEntity<TestScheduleResponse> getTestScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(testScheduleService.getTestScheduleById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update test schedule",
            description = "Updates an existing test schedule with new information")
    @ApiResponse(responseCode = "200", description = "Test schedule updated successfully")
    @ApiResponse(responseCode = "404", description = "Test schedule not found")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<TestScheduleResponse> updateTestSchedule(
            @PathVariable Long id,
            @Valid @RequestBody TestScheduleRequest request) {
        return ResponseEntity.ok(testScheduleService.updateTestSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete test schedule",
            description = "Deletes a test schedule by its ID")
    @ApiResponse(responseCode = "204", description = "Test schedule deleted successfully")
    @ApiResponse(responseCode = "404", description = "Test schedule not found")
    public ResponseEntity<Void> deleteTestSchedule(@PathVariable Long id) {
        testScheduleService.deleteTestSchedule(id);
        return ResponseEntity.noContent().build();
    }
}