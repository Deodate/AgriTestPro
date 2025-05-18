package com.AgriTest.service;

import com.AgriTest.model.TimeTracking;
import com.AgriTest.repository.TimeTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTrackingService {

    private final TimeTrackingRepository timeTrackingRepository;

    @Autowired
    public TimeTrackingService(TimeTrackingRepository timeTrackingRepository) {
        this.timeTrackingRepository = timeTrackingRepository;
    }

    public TimeTracking saveTimeTracking(TimeTracking timeTracking) {
        // Add any business logic or validation here before saving
        return timeTrackingRepository.save(timeTracking);
    }

    // Add other service methods as needed (e.g., find, findAll, delete)
} 