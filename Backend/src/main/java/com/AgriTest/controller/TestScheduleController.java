// File: src/main/java/com/AgriTest/controller/TestScheduleController.java
package com.AgriTest.controller;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.service.TestScheduleService;
import com.AgriTest.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@PreAuthorize("isAuthenticated()") // Ensures all endpoints require authentication
public class TestScheduleController {

    @Autowired
    private TestScheduleService testScheduleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')") // All authenticated users can view schedules
    public List<TestScheduleResponse> getAllTestSchedules() {
        return testScheduleService.getAllTestSchedules();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<TestScheduleResponse> getTestScheduleById(@PathVariable Long id) {
        return testScheduleService.getTestScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public TestScheduleResponse createTestSchedule(@Valid @RequestBody TestScheduleRequest testScheduleRequest) {
        System.out.println("Received a POST request to /api/schedules");
        Long userId = SecurityUtils.getCurrentUserId();
        return testScheduleService.createTestSchedule(testScheduleRequest, userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<TestScheduleResponse> updateTestSchedule(
            @PathVariable Long id, 
            @Valid @RequestBody TestScheduleRequest testScheduleRequest) {
        
        return ResponseEntity.ok(testScheduleService.updateTestSchedule(id, testScheduleRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTestSchedule(@PathVariable Long id) {
        testScheduleService.deleteTestSchedule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test-case/{testCaseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public List<TestScheduleResponse> getTestSchedulesByTestCase(@PathVariable Long testCaseId) {
        return testScheduleService.getTestSchedulesByTestCase(testCaseId);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public List<TestScheduleResponse> getActiveTestSchedules() {
        return testScheduleService.getActiveTestSchedules();
    }

    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public List<TestScheduleResponse> getTestSchedulesForToday() {
        return testScheduleService.getTestSchedulesForToday();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<TestScheduleResponse> activateTestSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(testScheduleService.activateTestSchedule(id));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<TestScheduleResponse> deactivateTestSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(testScheduleService.deactivateTestSchedule(id));
    }

    @PostMapping("/{id}/execute")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER')")
    public ResponseEntity<Void> executeSchedule(@PathVariable Long id) {
        testScheduleService.executeSchedule(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/execute-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> executeAllDueSchedules() {
        testScheduleService.executeAllDueSchedules();
        return ResponseEntity.ok().build();
    }
}