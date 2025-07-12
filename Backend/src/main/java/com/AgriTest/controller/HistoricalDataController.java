package com.AgriTest.controller;

import com.AgriTest.dto.HistoricalDataRequest;
import com.AgriTest.model.HistoricalData;
import com.AgriTest.service.HistoricalDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/historical-data")
public class HistoricalDataController {

    private static final Logger logger = LoggerFactory.getLogger(HistoricalDataController.class);

    @Autowired
    private HistoricalDataService historicalDataService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // Adjust roles as needed
    public ResponseEntity<HistoricalData> createHistoricalData(@Valid @RequestBody HistoricalDataRequest request) {
        logger.info("API /api/historical-data called");
        logger.info("Received request to create historical data");
        HistoricalData savedData = historicalDataService.saveHistoricalData(request);
        logger.info("Historical data saved successfully with ID: {}", savedData.getId());
        return new ResponseEntity<>(savedData, HttpStatus.CREATED);
    }

    // Add other endpoints (GET, PUT, DELETE) as needed in the future
} 