// File: src/main/java/com/AgriTest/controller/TestScheduleController.java
package com.AgriTest.controller;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.service.TestScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-schedules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestScheduleController {

    private final TestScheduleService testScheduleService;

    @Autowired
    public TestScheduleController(TestScheduleService testScheduleService) {
        this.testScheduleService = testScheduleService;
    }

    @PostMapping
    public ResponseEntity<TestSchedule> createTestSchedule(@Valid @RequestBody TestScheduleRequest request) {
        TestSchedule createdSchedule = testScheduleService.createTestSchedule(request);
        return ResponseEntity.ok(createdSchedule);
    }

    @GetMapping
    public ResponseEntity<List<TestSchedule>> getAllTestSchedules() {
        List<TestSchedule> schedules = testScheduleService.getAllTestSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestSchedule> getTestScheduleById(@PathVariable Long id) {
        TestSchedule schedule = testScheduleService.getTestScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestSchedule> updateTestSchedule(
            @PathVariable Long id,
            @Valid @RequestBody TestScheduleRequest request) {
        TestSchedule updatedSchedule = testScheduleService.updateTestSchedule(id, request);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestSchedule(@PathVariable Long id) {
        testScheduleService.deleteTestSchedule(id);
        return ResponseEntity.noContent().build();
    }
}