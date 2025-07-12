package com.AgriTest.controller;

import com.AgriTest.model.TimeTracking;
import com.AgriTest.service.TimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-tracking")
public class TimeTrackingController {

    private final TimeTrackingService timeTrackingService;

    @Autowired
    public TimeTrackingController(TimeTrackingService timeTrackingService) {
        this.timeTrackingService = timeTrackingService;
    }

    @PostMapping
    public ResponseEntity<TimeTracking> createTimeTracking(@RequestBody TimeTracking timeTracking) {
        TimeTracking savedTimeTracking = timeTrackingService.saveTimeTracking(timeTracking);
        return new ResponseEntity<>(savedTimeTracking, HttpStatus.CREATED);
    }

    // Add other controller methods as needed (e.g., GET for retrieving data)
} 